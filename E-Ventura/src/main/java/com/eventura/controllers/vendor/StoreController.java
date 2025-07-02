package com.eventura.controllers.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.eventura.services.VendorProductCategoryService;
import com.eventura.services.VendorService;

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

	
	/*===================== STORE =====================*/
	@GetMapping("edit")
	public String storeEdit(HttpSession session,ModelMap modelMap) {
		modelMap.put("currentPage", "store");
		
		int id = (Integer) session.getAttribute("vendorId");
		modelMap.put("user", userService.findById(id));
		modelMap.put("vendor", vendorService.findById(id));

		return "vendor/pages/store/edit";
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
		
		int pageSize = 6;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPages = productService.findByVendorIdPage(vendorId, pageable);
		
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
		

		int pageSize = 6;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage;
		
		if(categoryId == 0) {
			productPage = productService.findByVendorIdPage(vendorId, pageable);
			modelMap.put("products", productPage.getContent());
		}else {
			productPage = productService.findByVendorCategoryPage(vendorId, categoryId, pageable);
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
	public String storeReview(ModelMap modelMap) {
		modelMap.put("currentPage", "review");

		return "vendor/pages/store/review";
	}
	
	
	
}
