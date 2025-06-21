package com.eventura.controllers.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventura.entities.Users;
import com.eventura.services.AddressService;
import com.eventura.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorProfileController")
@RequestMapping("vendor/profile")
public class ProfileController  {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AddressService addressService;
	/*===================== PROFILE =====================*/
	@GetMapping("edit")
	public String editProfile(HttpSession session, ModelMap modelMap) {
		modelMap.put("currentPage", "profile");

		Integer vendorId = (Integer) session.getAttribute("vendorId");
		Users user = userService.findById(vendorId);
		modelMap.put("user", user);
		modelMap.put("provinces", addressService.findAllProvinces());
		modelMap.put("districts", addressService.findDistrictsByProvinceCode("34"));
		modelMap.put("wards", addressService.findWardsByDistrictCode("034"));


		return "vendor/pages/profile/edit";
	}

	
}
