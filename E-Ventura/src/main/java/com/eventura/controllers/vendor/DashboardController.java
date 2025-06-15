package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("vendorDashboardController")
@RequestMapping("vendor/dashboard")
public class DashboardController  {
	
	
	/*===================== DASHBOARD =====================*/
	@GetMapping("home")
	public String home() {
		return "vendor/pages/dashboard/index";
	}
	
	
}
