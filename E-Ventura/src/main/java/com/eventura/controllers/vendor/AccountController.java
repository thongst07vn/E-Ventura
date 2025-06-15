package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("vendorAccountController")
@RequestMapping("vendor/account")
public class AccountController  {
	
	/*===================== LOGIN / REGISTER =====================*/
	@GetMapping("/login")
	public String login() {
		return "vendor/pages/login/login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "vendor/pages/login/register";
	}
	
	
	
}
