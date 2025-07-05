package com.eventura.controllers.vendor;

import java.time.DateTimeException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.VendorReviews;
import com.eventura.entities.Vendors;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.eventura.services.VendorProductCategoryService;
import com.eventura.services.VendorReviewService;
import com.eventura.services.VendorService;
import com.eventura.services.VendorSettingService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorStoreController")
@RequestMapping("vendor/store")
public class StoreController  {
	
	@Autowired
	private UserService userService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private VendorReviewService vendorReviewService;
	@Autowired
	private VendorSettingService vendorSettingService;

	
	/*===================== STORE =====================*/
	@GetMapping("edit")
	public String storeEdit(HttpSession session,ModelMap modelMap) {
		modelMap.put("currentPage", "store");
		
		int id = (Integer) session.getAttribute("vendorId");
		modelMap.put("user", userService.findById(id));
		modelMap.put("vendor", vendorService.findById(id));
		modelMap.put("vendorSettings", vendorSettingService.findAll());

		return "vendor/pages/store/edit";
	}
	
	@PostMapping("edit")
	public String storeEdit(@ModelAttribute("vendor") Vendors vendor,
							RedirectAttributes redirectAttributes) {
		
		vendor.setVendorSettings(vendorSettingService.findById(vendor.getVendorSettings().getId()));
		vendor.setUpdatedAt(new Date());
		
		if(vendorService.save(vendor)) {
			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Save Vendor Profile Successfully");
		}else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Save Vendor Profile Failed");
		}

		return "redirect:/vendor/store/edit";
	}
	
	@GetMapping("view")
	public String storeView(HttpSession session, ModelMap modelMap,
							@RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "store");
		int vendorId = (Integer) session.getAttribute("vendorId");
		/* Thông tin user, vendor, revenue */
		modelMap.put("user", userService.findById(vendorId));
		modelMap.put("vendor", vendorService.findById(vendorId));
		modelMap.put("revenue", vendorService.sumByVendorId(vendorId));
		modelMap.put("sale", vendorService.countByVendorId(vendorId));

		
		int pageSize = 12;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPages = productService.findProductByVendorAndDeletePage(vendorId, pageable);
		
		modelMap.put("products", productPages.getContent());
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", productPages.getTotalPages());
		modelMap.put("lastPageIndex", productPages.getTotalPages() - 1);
		
		modelMap.put("categories", categoryService.findAll());



		return "vendor/pages/store/view";
	}
	
	@GetMapping("searchByVendorCategory")
	public String searchByVendorCategory(ModelMap modelMap, HttpSession session,
										@RequestParam("categoryId") int categoryId, 
										@RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "store");
		Integer vendorId = (Integer) session.getAttribute("vendorId");
		
		/* Thông tin user, vendor, revenue */
		modelMap.put("user", userService.findById(vendorId));
		modelMap.put("vendor", vendorService.findById(vendorId));
		modelMap.put("revenue", vendorService.sumByVendorId(vendorId));
		

		int pageSize = 12;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage;
		
		if(categoryId == 0) {
			productPage = productService.findProductByVendorAndDeletePage(vendorId, pageable);
			modelMap.put("products", productPage.getContent());
		}else {
			productPage = productService.findProductByVendorAndDeleteAndCategoryPage(vendorId, categoryId, pageable);
			modelMap.put("products", productPage.getContent());
		}
		
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", productPage.getTotalPages());
		modelMap.put("lastPageIndex", productPage.getTotalPages() - 1);
		
		modelMap.put("selectedCategoryId", categoryId);
		
		modelMap.put("categories", categoryService.findAll());
		return "vendor/pages/store/view";
	}
	
	@GetMapping("review")
	public String storeReview(ModelMap modelMap, HttpSession session,
							 @RequestParam(defaultValue = "0") int page,
							 @RequestParam(defaultValue = "10") int pageSize) {
		modelMap.put("currentPage", "review");
		Integer vendorId = (Integer) session.getAttribute("vendorId");
		
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<VendorReviews> vendorReviewsPage = vendorReviewService.findVendorReviewByVendorId(vendorId, pageable);
		
		modelMap.put("vendorReviews", vendorReviewsPage.getContent());
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", vendorReviewsPage.getTotalPages());
		modelMap.put("lastPageIndex", vendorReviewsPage.getTotalPages() - 1);

		modelMap.put("vendor", vendorService.findById(vendorId));
		modelMap.put("avgRating", vendorReviewService.avgVendorReview(vendorId));
		modelMap.put("count", vendorReviewService.countVendorReview(vendorId));
		

		return "vendor/pages/store/review";
	}
	
	
	
}
