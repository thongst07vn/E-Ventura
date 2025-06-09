package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("vendor")
public class VendorController {
	
	/*===================== DASHBOARD =====================*/
	@GetMapping("dashboard/index")
	public String home() {
		return "vendor/dashboard/index";
	}
	
	/*===================== PRODUCT =====================*/
	@GetMapping("product/list")
	public String productList() {
		return "vendor/product/list";
	}
	
	@GetMapping("product/add")
	public String productAdd() {
		return "vendor/product/add";
	}
	
	@GetMapping("product/edit")
	public String productEdit() {
		return "vendor/product/edit";
	}
	
	@GetMapping("product/delete")
	public String productDelete() {
		return "vendor/product/delete";
	}
	
	@GetMapping("product/review")
	public String productReview() {
		return "vendor/product/review";
	}
	
	@GetMapping("product/reviewDetail")
	public String productReviewDetail() {
		return "vendor/product/reviewDetail";
	}
	
	/*===================== CATEGORY =====================*/
	@GetMapping("category/list")
	public String categoryList() {
		return "vendor/category/list";
	}
	
	@GetMapping("category/add")
	public String categoryAdd() {
		return "vendor/category/add";
	}
	
	@GetMapping("category/edit")
	public String categoryEdit() {
		return "vendor/category/edit";
	}
	
	/*===================== ORDER =====================*/
	@GetMapping("order/list")
	public String orderList() {
		return "vendor/order/list";
	}
	
	@GetMapping("order/detail")
	public String orderDetail() {
		return "vendor/order/detail";
	}
	
	@GetMapping("order/tracking")
	public String orderTracking() {
		return "vendor/order/tracking";
	}
	
	@GetMapping("order/returnList")
	public String orderReturnList() {
		return "vendor/order/return_dispute_list";
	}
	
	@GetMapping("order/returnDetail")
	public String orderReturnDetail() {
		return "vendor/order/return_dispute_detail";
	}
	
	/*===================== PROMOTION =====================*/
	@GetMapping("promotion/list")
	public String promotionList() {
		return "vendor/promotion/list";
	}
	
	@GetMapping("promotion/add")
	public String promotionAdd() {
		return "vendor/promotion/add";
	}
	
	@GetMapping("promotion/edit")
	public String promotionEdit() {
		return "vendor/promotion/edit";
	}
	
	/*===================== PROFILE =====================*/
	@GetMapping("profile/edit")
	public String editProfile() {
		return "vendor/profile/edit";
	}
	
	/*===================== STORE =====================*/
	@GetMapping("store/edit")
	public String storeEdit() {
		return "vendor/store/edit";
	}
	
	@GetMapping("store/view")
	public String storeView() {
		return "vendor/store/view";
	}
	
	@GetMapping("store/review")
	public String storeReview() {
		return "vendor/store/review";
	}
	
	
	
}
