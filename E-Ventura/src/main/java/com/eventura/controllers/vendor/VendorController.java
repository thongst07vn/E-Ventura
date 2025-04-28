package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("vendor")
public class VendorController {
	
	@GetMapping("dashboard/index")
	public String home() {
		return "vendor/dashboard/index";
	}
	
	@GetMapping("product/list")
	public String productList() {
		return "vendor/product/list";
	}
	
	@GetMapping("product/add")
	public String addProduct() {
		return "vendor/product/add";
	}
	
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
}
