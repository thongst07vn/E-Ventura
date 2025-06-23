package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("vendorAccountController")
@RequestMapping("vendor/account")
public class AccountController  {
	
	/*===================== LOGIN / REGISTER =====================*/
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error, ModelMap modelMap) {
		if (error != null) {
			modelMap.put("msg", error);
		}
		return "vendor/pages/login/login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "vendor/pages/login/register";
	}
	
	
	
}
