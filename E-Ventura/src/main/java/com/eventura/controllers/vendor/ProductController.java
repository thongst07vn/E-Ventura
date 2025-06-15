package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("vendorProductController")
@RequestMapping("vendor/product")
public class ProductController  {
	
	
	/*===================== PRODUCT =====================*/
	@GetMapping("list")
	public String productList() {
		return "vendor/pages/product/list";
	}
	
	@GetMapping("add")
	public String productAdd() {
		return "vendor/pages/product/add";
	}
	
	@GetMapping("edit")
	public String productEdit() {
		return "vendor/pages/product/edit";
	}
	
	@GetMapping("delete")
	public String productDelete() {
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
