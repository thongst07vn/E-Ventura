package com.eventura.controllers.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.eventura.services.VendorService;

@Controller
@RequestMapping({"product"})

public class ProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;
	@Autowired
	private VendorService vendorService;
	
	@GetMapping({"productdetails/{id}"})
	public String prouctDetails(@PathVariable("id") int id,ModelMap modelMap, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails) {
		Products product = productService.findById(id);
		modelMap.put("product", product);
		modelMap.put("vendorAddresses", userService.findAddressUser(product.getVendors().getId()));
		modelMap.put("productReviews", productService.findProductReview(id));
		
		if (!productService.findProductReview(id).isEmpty()) {
			modelMap.put("countProductReview", productService.countProductReview(id));
			modelMap.put("avgProductReview", productService.avgProductReview(id));
			modelMap.put("widthProduct", Math.min(productService.avgProductReview(id), 5) * 20);
		} else {
			modelMap.put("countProductReview", 0);
			modelMap.put("avgProductReview", 0);
			modelMap.put("widthProduct", Math.min(0, 5) * 20);

		}
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
		modelMap.put("currentPage", "product");
		
		return "customer/pages/product/productdetails";
	}
	@GetMapping({"findbycategory/{id}"})
	public String findByCategory(@PathVariable("id") int id,ModelMap modelMap,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "30") int pageSize, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails) {
		
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		Page<Products> products = productService.findByCategoryIdPage(id,pageable);
		List<ProductCategories> categories = categoryService.findAll();
		modelMap.put("products", products.getContent());
		modelMap.put("categories", categories);
		modelMap.put("categoryId", id);
		
		modelMap.addAttribute("currentPages", page);
		modelMap.addAttribute("totalPage", products.getTotalPages());
		modelMap.addAttribute("lastPageIndex", products.getTotalPages() - 1);
		modelMap.addAttribute("pageSize", pageSize);
		return "customer/pages/home/shopgrid";
	}

	@GetMapping({"search"})
	public String searchByKeyword(@RequestParam("keyword") String keyword,ModelMap modelMap,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "30") int pageSize, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> products = productService.findByKeywordPage(keyword,pageable);
		List<ProductCategories> categories = categoryService.findAll();
		modelMap.put("products", products.getContent());
		modelMap.put("categories", categories);
		modelMap.put("keyword", keyword);
		
		modelMap.addAttribute("currentPages", page);
		modelMap.addAttribute("totalPage", products.getTotalPages());
		modelMap.addAttribute("lastPageIndex", products.getTotalPages() - 1);
		modelMap.addAttribute("pageSize", pageSize);
		return "customer/pages/home/shopgrid";
	}
	

}
