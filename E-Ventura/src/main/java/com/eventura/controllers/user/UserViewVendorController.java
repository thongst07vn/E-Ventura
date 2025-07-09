package com.eventura.controllers.user;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventura.configurations.AccountOAuth2User;
import com.eventura.dtos.ProductDTO;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.VendorReviews;
import com.eventura.entities.Vendors;
import com.eventura.services.AddressService;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.eventura.services.VendorProductCategoryService;
import com.eventura.services.VendorReviewService;
import com.eventura.services.VendorService;
import com.eventura.helpers.FileHelper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping({ "customer" })

public class UserViewVendorController {

	@Autowired
	private UserService userService;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private ProductService productService;

	@Autowired
	private VendorProductCategoryService vendorProductCategoryService;

	@Autowired
	private VendorReviewService vendorReviewService;

	@GetMapping({ "vendor/{id}" })
	public String home(ModelMap modelMap, HttpSession session, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "12") int pageSize, @PathVariable("id") int id, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage = productService.findByVendorIdPage(id, pageable);
		// Khởi tạo một List để chứa các ProductDTO
		List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

		// Lặp qua productPage và thêm các ProductDTO vào List
		for (Products product : productPage) {
			if (product.getDeletedAt() == null) {
				if (!productService.findProductReview(product.getId()).isEmpty()) {
					productDTOList.add(new ProductDTO(product, productService.avgProductReview(product.getId()),
							productService.countProductReview(product.getId())));
				} else {
					productDTOList.add(new ProductDTO(product, 0, 0));
				}
			}

		}

		// Tạo một PageImpl mới từ List các ProductDTO, Pageable và tổng số phần tử
		Page<ProductDTO> productDTOPage = new PageImpl<ProductDTO>(productDTOList, productPage.getPageable(),
				productPage.getTotalElements() // Lấy tổng số phần tử từ productPage
		);
		if (!vendorService.findVendorReview(productService.findById(id).getVendors().getId()).isEmpty()) {
			modelMap.put("countVendorReview",
					vendorService.countVendorReview(productService.findById(id).getVendors().getId()));
			modelMap.put("avgVendorReview",
					vendorService.avgVendorReview(productService.findById(id).getVendors().getId()));
			modelMap.put("widthVendor",
					Math.min(vendorService.avgVendorReview(productService.findById(id).getVendors().getId()), 5) * 20);
		} else {
			modelMap.put("countVendorReview", 0);
			modelMap.put("avgVendorReview", 0);
			modelMap.put("widthVendor", Math.min(0, 5) * 20);
		}
		VendorReviews vendorReviews = null;
        Users currentUser = null;

        if (userDetails != null) {
            currentUser = userService.findByEmail(userDetails.getUsername());
        } else if (authentication != null && authentication.getPrincipal() instanceof AccountOAuth2User) {
            AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
            currentUser = userService.findByEmail(accountOAuth2User.getEmail());
        }

        if (currentUser != null) {
            // Lấy vendorReview dựa trên vendorId và userId
            // Cần vendorId, không phải productId như bạn đang dùng productService.findById(id).getVendors().getId()
            // Tham số 'id' ở đây là vendorId trực tiếp từ URL.
            vendorReviews = vendorReviewService.findVendorReviewByUserAndVendorId(id, currentUser.getId());
        }

        modelMap.put("vendorReviews", vendorReviews);
		modelMap.put("products", productDTOPage.getContent());
		modelMap.put("currentPages", page);
		modelMap.put("totalPages", productPage.getTotalPages());
		modelMap.put("lastPageIndex", productPage.getTotalPages() - 1);
		modelMap.put("vendor", vendorService.findById(id));
		modelMap.put("vendorAddresses", userService.findAddressUser(id));
		return "customer/pages/vendor/index";
	}

	@PostMapping("toggleFollow")
	public String followVendor(@RequestParam("vendorId") int vendorId, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails, @RequestParam("actionType") String actionType) {
		Vendors vendor = vendorService.findById(vendorId);
		if (userDetails != null) {
			Users user = userService.findByEmail(userDetails.getUsername());
			VendorReviews vendorReviews = vendorReviewService.findVendorReviewByUserAndVendorId(vendorId, user.getId());
			if (vendorReviews != null) {
				if (vendorReviews.isFollow()) {
					vendorReviews.setFollow(false);
				} else {
					vendorReviews.setFollow(true);
				}
			} else {
				if ("follow".equals(actionType)) {
					vendorReviews =	new VendorReviews(user,vendor,0,false,new Date(), new Date());
				} else if ("unfollow".equals(actionType)) {
					vendorReviews = new VendorReviews(user,vendor,0,true,new Date(), new Date());
				}
			}
			vendorReviewService.save(vendorReviews);
		} else if (authentication != null) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			Users user = userService.findByEmail(accountOAuth2User.getEmail());
			VendorReviews vendorReviews = vendorReviewService.findVendorReviewByUserAndVendorId(vendorId, user.getId());
			if (vendorReviews != null) {
				if (!vendorReviews.isFollow()) {
					vendorReviews.setFollow(true);
				} else {
					vendorReviews.setFollow(false);
				}
			} else {
				if ("follow".equals(actionType)) {
					vendorReviews = new VendorReviews(user,vendor,0,false,new Date(), new Date());
				} else if ("unfollow".equals(actionType)) {
					vendorReviews = new VendorReviews(user,vendor,0,true,new Date(), new Date());
				}
			}
			vendorReviewService.save(vendorReviews);
		}

		return "redirect:/customer/vendor/" + vendorId;
	}
}
