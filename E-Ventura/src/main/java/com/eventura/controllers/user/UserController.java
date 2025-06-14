package com.eventura.controllers.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"customer","/"})

public class UserController {
	@GetMapping({"home","/"})
	public String home() {
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
