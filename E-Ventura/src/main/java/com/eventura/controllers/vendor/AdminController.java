package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminController {
	
	@GetMapping({"dashboard"})
	public String home() {
		return "admin/dashboard/index";
	}
	
	@GetMapping("product/list")
	public String productList() {
		return "admin/product/list";
	}
	
	@GetMapping("product/add")
	public String addProduct() {
		return "admin/product/add";
	}
	
	@GetMapping("order/list")
	public String orderList() {
		return "admin/order/list";
	}
	
	@GetMapping("order/detail")
	public String orderDetail() {
		return "admin/order/detail";
	}
	
	@GetMapping("order/tracking")
	public String orderTracking() {
		return "vendor/order/tracking";
	}
}
