package com.eventura.controllers.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventura.services.RoleService;

@Controller("vendorAccountController")
@RequestMapping("vendor/account")
public class AccountController  {
	
	@Autowired
	private RoleService roleService;
	/*===================== LOGIN / REGISTER =====================*/
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
						ModelMap modelMap) {
		if(error != null) {
			modelMap.put("msg", "Failed");
		}
		modelMap.put("roles", roleService.findAll());
		return "vendor/pages/login/login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "vendor/pages/login/register";
	}
	
	
	
}
