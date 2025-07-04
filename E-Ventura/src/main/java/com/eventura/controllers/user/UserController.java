package com.eventura.controllers.user;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventura.configurations.AccountOAuth2User;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.helpers.FileHelper;
import com.eventura.services.AddressService;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping({ "customer", "/" })

public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private AddressService addressService;

	@GetMapping({ "home", "/" })
	public String home(ModelMap modelMap,HttpSession session) {
		List<ProductCategories> categories = categoryService.findAll().subList(1, categoryService.findAll().size());
		List<Products> top10products = productService.findTopNewProduct();

		modelMap.put("categories", categories);
		modelMap.put("top10products", top10products);

		return "customer/pages/home/index";
	}

	@GetMapping({ "register" })
	public String register() {
		return "customer/pages/login/register";
	}

	@GetMapping({ "login" })
	public String login(@RequestParam(value = "error", required = false) String error, ModelMap modelMap) {
		if (error != null) {
			modelMap.put("msg", error);
		}
		return "customer/pages/login/login";
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
		modelMap.put("addAddressVariable", new UserAddress());
		modelMap.put("provinces", addressService.findAllProvinces());
		return "customer/pages/account/profile";
	}

	@PostMapping({ "edit/{id}" })
	public String editProfile(@ModelAttribute("user") Users user, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		Users currentUser = null;

	    // 1. Centralize currentUser retrieval
	    if (userDetails != null) {
	        currentUser = userService.findByEmail(userDetails.getUsername());
	    } else if (authentication != null && authentication.getPrincipal() instanceof AccountOAuth2User) {
	        AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
	        currentUser = userService.findByEmail(accountOAuth2User.getEmail());
	        if(currentUser.getPassword()==null) {
	        	user.setPassword(BCrypt.hashpw("123456789@T", BCrypt.gensalt()));
	        }
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
	        user.setEmail(currentUser.getEmail());
	        user.setRoles(currentUser.getRoles());
	    }
	    
	    if(userService.save(user)) {
	    	redirectAttributes.addFlashAttribute("msg","Edit profile success");
	    	redirectAttributes.addFlashAttribute("classedit","label-delivery label-delivered");
	    	return "redirect:/customer/profile";    	
	    } else {
	    	redirectAttributes.addFlashAttribute("msg","Edit profile failed");
	    	redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
	    	return "redirect:/customer/profile";    	    	
	    }
	}
	
	@PostMapping({ "changepassword" })
	public String ChangePassword(@RequestParam("newPassword") String newPassword, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
		
		Users currentUser = null;
		
		if (userDetails != null) {
			currentUser = userService.findByEmail(userDetails.getUsername());
		} else if (authentication != null && authentication.getPrincipal() instanceof AccountOAuth2User) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			currentUser = userService.findByEmail(accountOAuth2User.getEmail());
		}
		if (currentUser != null) {
			currentUser.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
		}
		
		if(userService.save(currentUser)) {
			redirectAttributes.addFlashAttribute("msg","Change password success");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-delivered");
			return "redirect:/customer/profile";    	
		} else {
			redirectAttributes.addFlashAttribute("msg","Change password failed");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
			return "redirect:/customer/profile";    	    	
		}  	    	
	}
		
	@PostMapping({"addaddress"})
	public String AddAddress(@RequestParam("userId") int userId, @ModelAttribute("addAddressVariable") UserAddress addAddressVariable, RedirectAttributes redirectAttributes) {
		Users user = userService.findById(userId);
		addAddressVariable.setUsers(user);
		addAddressVariable.setCreatedAt(new Date());
		if(addAddressVariable.getName()==null || addAddressVariable.getName().trim().isEmpty()) {
			addAddressVariable.setName(user.getUsername());			
		}
		if(addressService.save(addAddressVariable)) {
			redirectAttributes.addFlashAttribute("msg","Add address success");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-delivered");
			return "redirect:/customer/profile";    	
		} else {
			redirectAttributes.addFlashAttribute("msg","Add address failed");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
			return "redirect:/customer/profile";    	    	
		} 		    	
	}

	
	@PostMapping({"editaddress"})
	public String EditAddress(@RequestParam("userId") int userId,@RequestParam("editProvince") String editProvince,@RequestParam("editDistrict") String editDistrict,@RequestParam("editWard") String editWard, @ModelAttribute("addAddressVariable") UserAddress addAddressVariable, RedirectAttributes redirectAttributes) {
		Users user = userService.findById(userId);
		addAddressVariable.setUsers(user);
		addAddressVariable.setCreatedAt(new Date());
		addAddressVariable.setProvinces(addressService.findProvinceById(editProvince));
		addAddressVariable.setDistricts(addressService.findDistrictById(editDistrict));
		addAddressVariable.setWards(addressService.findWardById(editWard));


		if(addressService.save(addAddressVariable)) {
			redirectAttributes.addFlashAttribute("msg","Edit address success");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-delivered");
			return "redirect:/customer/profile";    	
		} else {
			redirectAttributes.addFlashAttribute("msg","Edit address failed");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
			return "redirect:/customer/profile";    	    	
		} 	
//		return "redirect:/customer/profile";    	    	
		
	}
	
	@PostMapping({"deleteaddress"})
	public String DeleteAddress(@RequestParam("addressId") int addressId,RedirectAttributes redirectAttributes) {
		UserAddress address = addressService.findById(addressId);
		address.setDeletedAt(new Date());
		System.out.println(address.getName());
		if(addressService.save(address)) {
			redirectAttributes.addFlashAttribute("msg","Delete address success");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-delivered");
			return "redirect:/customer/profile";    	
		} else {
			redirectAttributes.addFlashAttribute("msg","Delete address failed");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
			return "redirect:/customer/profile";    	    	
		} 	
//		return "redirect:/customer/profile";    	    	
		
	}
	//Check out Address
	@PostMapping({"addaddressincheckout"})
	public String AddAddressIncheckout(@RequestParam("userId") int userId, 
			@ModelAttribute("addAddressVariable") UserAddress addAddressVariable, 
			RedirectAttributes redirectAttributes,
			@RequestParam("checkoutItems") String checkoutItems) {
		Users user = userService.findById(userId);
		addAddressVariable.setUsers(user);
		addAddressVariable.setCreatedAt(new Date());
		if(addAddressVariable.getName()==null || addAddressVariable.getName().trim().isEmpty()) {
			addAddressVariable.setName(user.getUsername());			
		}
		if(addressService.save(addAddressVariable)) {
			redirectAttributes.addFlashAttribute("msg","Add address success");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-delivered");
			return "redirect:/customer/cart/checkout?checkoutItems="+ checkoutItems;    	
		} else {
			redirectAttributes.addFlashAttribute("msg","Add address failed");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
			return "redirect:/customer/cart/checkout?checkoutItems="+ checkoutItems;    	    	
		} 		    	
	}
	
	@PostMapping({"editaddressincheckout"})
	public String EditAddressIncheckout(@RequestParam("userId") int userId,
			@RequestParam("editProvince") String editProvince,
			@RequestParam("editDistrict") String editDistrict,
			@RequestParam("editWard") String editWard, 
			@ModelAttribute("addAddressVariable") UserAddress addAddressVariable, 
			RedirectAttributes redirectAttributes,
			@RequestParam("checkoutItems") String checkoutItems) {
		Users user = userService.findById(userId);
		addAddressVariable.setUsers(user);
		addAddressVariable.setCreatedAt(new Date());
		addAddressVariable.setProvinces(addressService.findProvinceById(editProvince));
		addAddressVariable.setDistricts(addressService.findDistrictById(editDistrict));
		addAddressVariable.setWards(addressService.findWardById(editWard));


		if(addressService.save(addAddressVariable)) {
			redirectAttributes.addFlashAttribute("msg","Edit address success");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-delivered");
			return "redirect:/customer/cart/checkout?checkoutItems="+ checkoutItems;    	
		} else {
			redirectAttributes.addFlashAttribute("msg","Edit address failed");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
			return "redirect:/customer/cart/checkout?checkoutItems="+ checkoutItems;     	    	
		} 	
//		return "redirect:/customer/profile";    	    			
	}
	
	@PostMapping({"deleteaddressincheckout"})
	public String DeleteAddressIncheckout(@RequestParam("addressId") int addressId,
			RedirectAttributes redirectAttributes,
			@RequestParam("checkoutItems") String checkoutItems) {
		UserAddress address = addressService.findById(addressId);
		address.setDeletedAt(new Date());
		System.out.println(address.getName());
		if(addressService.save(address)) {
			redirectAttributes.addFlashAttribute("msg","Delete address success");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-delivered");
			return "redirect:/customer/cart/checkout?checkoutItems="+ checkoutItems;    	
		} else {
			redirectAttributes.addFlashAttribute("msg","Delete address failed");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
			return "redirect:/customer/cart/checkout?checkoutItems="+ checkoutItems;     	    	
		} 	
//		return "redirect:/customer/profile";    	    	
		
	}
}
