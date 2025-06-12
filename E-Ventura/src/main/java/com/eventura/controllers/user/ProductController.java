package com.eventura.controllers.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"product"})

public class ProductController {
	@GetMapping({"productdetails"})
	public String prouctDetails() {
		return "customer/pages/product/productdetails";
	}
}
