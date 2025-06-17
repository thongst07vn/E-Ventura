package com.eventura.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventura.services.CategoryService;

@Controller
@RequestMapping({"customer","/"})

public class UserController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping({"home","/"})
	public String home(ModelMap modelMap) {
		modelMap.put("categories", categoryService.findAll());
		return "customer/pages/home/index";
	}
	
	@GetMapping({"register"})
	public String register() {
		return "customer/pages/login/register";
	}
	
	@GetMapping({"login"})
	public String login() {
		return "customer/pages/login/login";
	}
	// User Cart
	@GetMapping({"cart"})
	public String cartList() {
		return "customer/pages/cart/cartlist";
	}
	
	// User profile
	@GetMapping({"profile"})
	public String profile() {
		return "customer/pages/account/profile";
	}
	
}
