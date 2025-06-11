package com.eventura.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminController {
	
	@GetMapping({"dashboard"})
	public String home(Model model) {
        model.addAttribute("currentPage", "dashboard");
		return "admin/page/dashboard/index";
	}
	
	//======= CATEGORY ========	
	@GetMapping("category/list")
	public String categoryList(Model model) {
		model.addAttribute("currentPage", "category");
		return "admin/page/category/list";
	}
	
	@GetMapping("category/add")
	public String addCategory(Model model) {
		model.addAttribute("currentPage", "category");
		return "admin/page/category/add";
	}
	@GetMapping("category/edit")
	public String editCategory(Model model) {
		model.addAttribute("currentPage", "category");
		return "admin/page/category/edit";
	}
	
	//======= PRODUCT ========
	@GetMapping("product/list")
	public String productList(Model model) {
		model.addAttribute("currentPage", "product");
		return "admin/page/product/list";
	}
	
	@GetMapping("product/add")
	public String productAdd(Model model) {
		model.addAttribute("currentPage", "product");
		return "admin/page/product/add";
	}
	
	@GetMapping("product/edit")
	public String productEdit(Model model) {
		model.addAttribute("currentPage", "product");
		return "admin/page/product/edit";
	}
	
	//======= Order ========
	@GetMapping("order/list")
	public String orderList(Model model) {
		model.addAttribute("currentPage", "order");
		return "admin/page/order/list";
	}
	@GetMapping("order/detail")
	public String orderDetail(Model model) {
		model.addAttribute("currentPage", "order");
		return "admin/page/order/detail";
	}
	
	//======= USER_CUSTOMER ========
	@GetMapping("customer/list")
	public String customerList(Model model) {
		model.addAttribute("currentPage", "user");
		return "admin/page/user/customer/list";
	}
	@GetMapping("customer/detail")
	public String customerDetail(Model model) {
		model.addAttribute("currentPage", "user");
		return "admin/page/user/customer/detail";
	}
	
	//======= USER_VENDOR ========
	@GetMapping("vendor/list")
	public String vendorList(Model model) {
		model.addAttribute("currentPage", "user");
		return "admin/page/user/vendor/list";
	}
	@GetMapping("vendor/detail")
	public String vendorDetail(Model model) {
		model.addAttribute("currentPage", "user");
		return "admin/page/user/vendor/detail";
	}
	
	//======= CATEGORY ========	
	@GetMapping("coupon/list")
	public String couponList(Model model) {
		model.addAttribute("currentPage", "coupon");
		return "admin/page/coupon/list";
	}
	
	@GetMapping("coupon/add")
	public String addCoupon(Model model) {
		model.addAttribute("currentPage", "coupon");
		return "admin/page/coupon/add";
	}
	@GetMapping("coupon/edit")
	public String editCoupon(Model model) {
		model.addAttribute("currentPage", "coupon");
		return "admin/page/coupon/edit";
	}
}
