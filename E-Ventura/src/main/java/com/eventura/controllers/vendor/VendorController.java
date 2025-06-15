package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("vendor")
public class VendorController {
	
	/*===================== CUSTOMER =====================*/
	@GetMapping("/login")
	public String login() {
		return "vendor/pages/login/login";
	}
	
	/*===================== DASHBOARD =====================*/
	@GetMapping("/dashboard/index")
	public String home() {
		return "vendor/pages/dashboard/index";
	}
	
	/*===================== PRODUCT =====================*/
	@GetMapping("product/list")
	public String productList() {
		return "vendor/pages/product/list";
	}
	
	@GetMapping("product/add")
	public String productAdd() {
		return "vendor/pages/product/add";
	}
	
	@GetMapping("product/edit")
	public String productEdit() {
		return "vendor/pages/product/edit";
	}
	
	@GetMapping("product/delete")
	public String productDelete() {
		return "vendor/pages/product/delete";
	}
	
	@GetMapping("product/review")
	public String productReview() {
		return "vendor/pages/product/review";
	}
	
	@GetMapping("product/reviewDetail")
	public String productReviewDetail() {
		return "vendor/pages/product/reviewDetail";
	}
	
	/*===================== CATEGORY =====================*/
	@GetMapping("category/list")
	public String categoryList() {
		return "vendor/pages/category/list";
	}
	
	@GetMapping("category/add")
	public String categoryAdd() {
		return "vendor/pages/category/add";
	}
	
	@GetMapping("category/edit")
	public String categoryEdit() {
		return "vendor/pages/category/edit";
	}
	
	/*===================== ORDER =====================*/
	@GetMapping("order/list")
	public String orderList() {
		return "vendor/pages/order/list";
	}
	
	@GetMapping("order/detail")
	public String orderDetail() {
		return "vendor/pages/order/detail";
	}
	
	@GetMapping("order/tracking")
	public String orderTracking() {
		return "vendor/pages/order/tracking";
	}
	
	@GetMapping("order/returnList")
	public String orderReturnList() {
		return "vendor/pages/order/return_dispute_list";
	}
	
	@GetMapping("order/returnDetail")
	public String orderReturnDetail() {
		return "vendor/pages/order/return_dispute_detail";
	}
	
	/*===================== PROMOTION =====================*/
	@GetMapping("promotion/list")
	public String promotionList() {
		return "vendor/pages/promotion/list";
	}
	
	@GetMapping("promotion/add")
	public String promotionAdd() {
		return "vendor/pages/promotion/add";
	}
	
	@GetMapping("promotion/edit")
	public String promotionEdit() {
		return "vendor/pages/promotion/edit";
	}
	
	/*===================== PROFILE =====================*/
	@GetMapping("profile/edit")
	public String editProfile() {
		return "vendor/pages/profile/edit";
	}
	
	/*===================== STORE =====================*/
	@GetMapping("store/edit")
	public String storeEdit() {
		return "vendor/pages/store/edit";
	}
	
	@GetMapping("store/view")
	public String storeView() {
		return "vendor/pages/store/view";
	}
	
	@GetMapping("store/review")
	public String storeReview() {
		return "vendor/pages/store/review";
	}
	
	
	
}
