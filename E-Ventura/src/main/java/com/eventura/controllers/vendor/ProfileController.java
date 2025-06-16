package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("vendorProfileController")
@RequestMapping("vendor/profile")
public class ProfileController  {
	
	/*===================== PROFILE =====================*/
	@GetMapping("edit")
	public String editProfile() {
		return "vendor/pages/profile/edit";
	}

	
}
