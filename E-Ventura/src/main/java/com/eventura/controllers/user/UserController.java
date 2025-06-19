package com.eventura.controllers.user;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;

@Controller
@RequestMapping({"customer","/"})

public class UserController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping({"home","/"})
	public String home(ModelMap modelMap) {
		List<ProductCategories> categories = categoryService.findAll();
		List<Products> top10products = productService.findTopNewProduct();
		for(Products pro : top10products) {
			System.out.println(pro.getName());
		}
		modelMap.put("categories", categories);
		modelMap.put("top10products", top10products);
		
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
