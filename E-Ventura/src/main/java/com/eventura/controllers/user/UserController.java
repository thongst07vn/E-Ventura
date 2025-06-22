package com.eventura.controllers.user;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eventura.configurations.AccountOAuth2User;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.example.demo.helpers.FileHelper;

@Controller
@RequestMapping({ "customer", "/" })

public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@GetMapping({ "home", "/" })
	public String home(ModelMap modelMap, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails) {
		List<ProductCategories> categories = categoryService.findAll();
		List<Products> top10products = productService.findTopNewProduct();

		modelMap.put("categories", categories);
		modelMap.put("top10products", top10products);
		if (userDetails != null) {
			Users user = userService.findByEmail(userDetails.getUsername());
			modelMap.put("user", user);
		} else if (authentication != null) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			Users user = userService.findByEmail(accountOAuth2User.getEmail());
			modelMap.put("user", user);
		}

		return "customer/pages/home/index";
	}

	@GetMapping({ "register" })
	public String register() {
		return "customer/pages/login/register";
	}

	@GetMapping({ "login" })
	public String login(@RequestParam(value = "error", required = false) String error, ModelMap modelMap) {
		if (error != null) {
			modelMap.put("msg", "Login failed");
		}
		return "customer/pages/login/login";
	}

	// User Cart
	@GetMapping({ "cart" })
	public String cartList(Authentication authentication, ModelMap modelMap) {
		if (authentication != null) {
			modelMap.put("user", userService.findByEmail(authentication.getName()));
		}
		return "customer/pages/cart/cartlist";
	}

	// User profile
	@GetMapping({ "profile" })
	public String profile(Authentication authentication, ModelMap modelMap,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails != null) {
			Users user = userService.findByEmail(userDetails.getUsername());
			List<UserAddress> userAdresses = userService.findAddressUser(user.getId());
			modelMap.put("userAdresses", userAdresses);
			modelMap.put("user", user);
		} else if (authentication != null) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			Users user = userService.findByEmail(accountOAuth2User.getEmail());
			List<UserAddress> userAdresses = userService.findAddressUser(user.getId());
			modelMap.put("userAdresses", userAdresses);
			modelMap.put("user", user);
		}
		return "customer/pages/account/profile";
	}

	@PostMapping({ "edit/{id}" })
	public String editProfile(@ModelAttribute("user") Users user, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails, @RequestParam("file") MultipartFile file) {

		Users currentUser = null;

	    // 1. Centralize currentUser retrieval
	    if (userDetails != null) {
	        currentUser = userService.findByEmail(userDetails.getUsername());
	    } else if (authentication != null && authentication.getPrincipal() instanceof AccountOAuth2User) {
	        AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
	        currentUser = userService.findByEmail(accountOAuth2User.getEmail());
	    }

	    // 2. Handle file upload for avatar
	    if (file != null && !file.isEmpty()) {
	        try {
	            String fileName = FileHelper.generateFileName(file.getOriginalFilename());
	            // Using getFile() might fail if running from a JAR; consider using getResourceAsStream() and Files.copy()
	            // Or get a path outside of the JAR for persistent storage
	            File imagesFolder = new ClassPathResource("static/assets/imgs/avatars/").getFile();
	            Path path = Paths.get(imagesFolder.getAbsolutePath() + File.separator + fileName);
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            user.setAvatar(fileName);
	        } catch (Exception e) {
	            e.printStackTrace();
	            // If file upload fails, fall back to existing avatar if available
	            if (currentUser != null) {
	                user.setAvatar(currentUser.getAvatar());
	            }
	            // Optionally, rethrow a custom exception or add a logging message
	        }
	    } else {
	        // If no new file is uploaded, keep the existing avatar
	        if (currentUser != null) {
	            user.setAvatar(currentUser.getAvatar());
	        }
	        // If currentUser is null here, it means no existing user found, avatar would remain null or default
	    }

	    // 3. Preserve rememberToken and password if currentUser was found
	    if (currentUser != null) {
	        user.setRememberToken(currentUser.getRememberToken());
	        user.setPassword(currentUser.getPassword());
	    }
		return "redirect:/customer/profile";
	}

}
