package com.eventura.controllers.admin;

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
	
	@GetMapping("category/list")
	public String categoryList() {
		return "admin/category/list";
	}
	
	@GetMapping("category/add")
	public String addCategory() {
		return "admin/category/add";
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
