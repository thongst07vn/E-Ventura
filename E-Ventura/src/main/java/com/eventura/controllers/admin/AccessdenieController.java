package com.eventura.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.VendorService;

@Controller
@RequestMapping("accessdenied")
public class AccessdenieController {
	@GetMapping("404")
	public String editCoupon(Model model) {
		return "404";
	}
}
