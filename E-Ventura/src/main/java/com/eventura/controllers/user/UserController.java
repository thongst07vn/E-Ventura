package com.eventura.controllers.user;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventura.configurations.AccountOAuth2User;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.Users;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;


@Controller
@RequestMapping({"customer","/"})

public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping({"home","/"})
	public String home(ModelMap modelMap,Authentication authentication,@AuthenticationPrincipal UserDetails userDetails) {
		List<ProductCategories> categories = categoryService.findAll();
		List<Products> top10products = productService.findTopNewProduct();

		modelMap.put("categories", categories);
		modelMap.put("top10products", top10products);
		if(userDetails != null) {
			Users user = userService.findByEmail(userDetails.getUsername());
			modelMap.put("user", user);
		} else if(authentication !=null) {	
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			Users user = userService.findByEmail(accountOAuth2User.getEmail());
			modelMap.put("user", user);
		}
		
		return "customer/pages/home/index";
	}
	
	@GetMapping({"register"})
	public String register() {
		return "customer/pages/login/register";
	}
	
	@GetMapping({"login"})
	public String login(@RequestParam(value = "error", required =false)String error,ModelMap modelMap) {
		if(error!=null) {
			modelMap.put("msg", "Login failed");
		}
		return "customer/pages/login/login";
	}
	// User Cart
	@GetMapping({"cart"})
	public String cartList(Authentication authentication,ModelMap modelMap) {
		if(authentication !=null) {			
			modelMap.put("user", userService.findByEmail(authentication.getName()));
		}
		return "customer/pages/cart/cartlist";
	}
	
	// User profile
	@GetMapping({"profile"})
	public String profile(Authentication authentication,ModelMap modelMap, @AuthenticationPrincipal UserDetails userDetails) {
		if(userDetails != null) {
			Users user = userService.findByEmail(userDetails.getUsername());
			modelMap.put("user", user);
		} else if(authentication !=null) {	
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			Users user = userService.findByEmail(accountOAuth2User.getEmail());
			modelMap.put("user", user);
		}
		return "customer/pages/account/profile";
	}
	
}
