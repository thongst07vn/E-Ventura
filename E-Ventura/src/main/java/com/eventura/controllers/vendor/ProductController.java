package com.eventura.controllers.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorProductController")
@RequestMapping("vendor/product")
public class ProductController  {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	
	/*===================== PRODUCT =====================*/
	@GetMapping("list")
	public String productList(ModelMap modelMap, HttpSession session) {
		Integer vendorId = (Integer) session.getAttribute("vendorId");
		System.out.println("Vendor Id:" + vendorId);
		modelMap.put("products", productService.findByVendorId(vendorId));
		modelMap.put("categories", categoryService.findAll());
		return "vendor/pages/product/list";
	}
	
	@GetMapping("details/{id}")
	public String productDetail(@PathVariable("id") int id) {
		return "vendor/pages/product/details";
	}
	
	@GetMapping("add")
	public String productAdd(ModelMap modelMap) {
		modelMap.put("categories", categoryService.findAll());
		return "vendor/pages/product/add";
	}
	
	@GetMapping("edit/{id}")
	public String productEdit(@PathVariable("id") int id, ModelMap modelMap) {
		modelMap.put("product", productService.findById(id));
		modelMap.put("categories", categoryService.findAll());
		return "vendor/pages/product/edit";
	}
	
	@GetMapping("delete/{id}")
	public String productDelete(@PathVariable("id") int id) {
		return "vendor/pages/product/delete";
	}
	
	@GetMapping("review")
	public String productReview() {
		return "vendor/pages/product/review";
	}
	
	@GetMapping("reviewDetail")
	public String productReviewDetail() {
		return "vendor/pages/product/reviewDetail";
	}
	
}
