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
import org.springframework.core.env.Environment;
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
import com.eventura.entities.Districts;
import com.eventura.entities.Orders;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.Provinces;
import com.eventura.entities.Roles;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;
import com.eventura.entities.Wards;
import com.eventura.helpers.FileHelper;
import com.eventura.services.AddressService;
import com.eventura.services.CategoryService;
import com.eventura.services.MailService;
import com.eventura.services.ProductService;
import com.eventura.services.RoleService;
import com.eventura.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping({ "customer", "/" })

public class UserController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private Environment environment;
	@Autowired
	private MailService mailService;
	@GetMapping({ "home", "/" })
	public String home(ModelMap modelMap,HttpSession session) {
		List<ProductCategories> categories = categoryService.findAll().subList(1, categoryService.findAll().size());
		List<Products> top10products = productService.findTopNewProduct();

		modelMap.put("categories", categories);
		modelMap.put("top10products", top10products);

		return "customer/pages/home/index";
	}

	@GetMapping({ "register" })
	public String register(ModelMap modelMap) {
		Users user = new Users();
		UserAddress userAddress = new UserAddress();

		modelMap.put("user", user);
		modelMap.put("userAddress", userAddress);
		modelMap.put("provinces", addressService.findAllProvinces());
		user.setRoles(roleService.findById(3));
		modelMap.put("user", user);
		return "customer/pages/login/register";
	}
	@PostMapping("register")
	public String register(@ModelAttribute("user") Users user, @ModelAttribute("userAddress") UserAddress userAddress,			
			@RequestParam("rePassword") String rePassword,
			@RequestParam(value = "provinceCode", required = false) String provinceCode,
			@RequestParam(value = "districtCode", required = false) String districtCode,
			@RequestParam(value = "wardCode", required = false) String wardCode, RedirectAttributes redirectAttributes,
			ModelMap modelMap) {

		// Check if first name and last name are empty
		if (user.getFirstName() == null || user.getFirstName().trim().isEmpty() || user.getLastName() == null
				|| user.getLastName().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("msgErrorName", "* Last name or First name cannot be empty.");
			return "redirect:/customer/register";
		}

		// Check if phone number is 10 digits
		if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("\\d{10}")) {
			redirectAttributes.addFlashAttribute("msgErrorPhone", "* Phone number must be 10 digits.");
			return "redirect:/customer/register";
		}

		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("msgErrorEmail", "* Email cannot be empty");
			return "redirect:/customer/register";
		} else {
			// Check if email already exists
			Users existingUser = userService.findByEmail(user.getEmail());
			if (existingUser != null) {
				redirectAttributes.addFlashAttribute("msgErrorEmail", "* Email is already in use.");
				return "redirect:/customer/register";
			}
		}

		if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("msgErrorUsername", "* Username cannot be empty");
			return "redirect:/customer/register";
		} else {
			// Check if username already exists
			Users existingUsername = userService.findByUsername(user.getUsername());
			if (existingUsername != null) {
				redirectAttributes.addFlashAttribute("msgErrorUsername", "* Username is already in use.");
				return "redirect:/customer/register";
			}
		}

		// Check if password is between 8 and 16 characters, contains at least 1
		// uppercase letter and 1 number
		String password = user.getPassword();
		String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$";

		if (!password.matches(passwordPattern)) {
			redirectAttributes.addFlashAttribute("msgErrorPasswordStrength",
					"Password must be between 8 and 16 characters, and include at least 1 uppercase letter, 1 special Character and 1 number.");
			return "redirect:/customer/register";
		}

		// Check if password and confirm password match
		if (!user.getPassword().equals(rePassword)) {
			redirectAttributes.addFlashAttribute("msgErrorPassword", "* Password and confirm password do not match.");
			return "redirect:/customer/register";
		}

		// Check if Provine / District / Ward is empty
		if (provinceCode == null || districtCode == null || wardCode == null) {
			redirectAttributes.addFlashAttribute("msgErrorPDW", "* Province-Districts-Ward cannot be empty");
			return "redirect:/customer/register";
		}

		// Check if address is empty
		if (userAddress.getAddress() == null || userAddress.getAddress().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("msgErrorAddress", "* Address cannot be empty.");
			return "redirect:/customer/register";
		}

		/* Create and save user */
		Roles role = new Roles();
		role.setId(2); // Assuming role ID 2 exists in the database
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		user.setAvatar("noimg.jpg");
		user.setCreatedAt(new Date());
		user.setDeletedAt(new Date());
		user.setRoles(role);

		// Save user first
		userService.save(user);

		/* Save user address */
		Provinces provinces = addressService.findProvinceById(provinceCode);
		Districts districts = addressService.findDistrictById(districtCode);
		Wards wards = addressService.findWardById(wardCode);
		userAddress.setUsers(user);
		userAddress.setCreatedAt(new Date());
		userAddress.setProvinces(provinces);
		userAddress.setDistricts(districts);
		userAddress.setWards(wards);
		userAddress.setName("Bao");

		// Save user address
		
		if(addressService.save(userAddress)) {
			// Send verification email
			String baseUrl = environment.getProperty("base_url");
			String url = baseUrl + "vendor/account/verify?email=" + user.getEmail();

			String from = environment.getProperty("spring.mail.username");
			String to = user.getEmail();
			String subject = "Verify Vendor";
			String body = "<div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4;'>"
					+ "<h2 style='color: #4CAF50;'>CUSTOMER INFORMATION:</h2>"
					+ "<div style='background-color: #fff; padding: 15px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>"
					+ "<p><strong>Fullname:</strong> " + user.getFirstName() + " " + user.getLastName() + "</p>"
					+ "<p><strong>Username:</strong> " + user.getUsername() + "</p>" + "<p><strong>Phone Number:</strong> "
					+ user.getPhoneNumber() + "</p>" + "<p><strong>Email:</strong> " + user.getEmail() + "</p>"
					+ "<p><strong>Address:</strong> " + userAddress.getAddress() + ", " + userAddress.getWards().getName()
					+ ", " + userAddress.getDistricts().getName() + ", " + userAddress.getProvinces().getName() + "</p>"
					+ "</div>" + "<p>Click <a href='" + url
					+ "' style='color: #4CAF50;'>here</a> to activate your Vendor Account.</p>" + "</div>";
			
			

			if (mailService.send(from, to, subject, body)) {
				redirectAttributes.addFlashAttribute("sweetAlert", "success");
				redirectAttributes.addFlashAttribute("message", "Register Sucessfully, Check your email to activate your account.");

			} else {
				redirectAttributes.addFlashAttribute("sweetAlert", "error");
				redirectAttributes.addFlashAttribute("message", "Register Failed");

			}

		}	
		return "redirect:/customer/register";
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
			@AuthenticationPrincipal UserDetails userDetails,@RequestParam(name="tab",required = false,defaultValue = "orders") String activeTab) {
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
		modelMap.addAttribute("activeTab",activeTab);
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
	    	return "redirect:/customer/profile?tab=setting";    	
	    } else {
	    	redirectAttributes.addFlashAttribute("msg","Edit profile failed");
	    	redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
	    	return "redirect:/customer/profile?tab=setting";    	    	
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
			return "redirect:/customer/profile?tab=setting";    	
		} else {
			redirectAttributes.addFlashAttribute("msg","Change password failed");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
			return "redirect:/customer/profile?tab=setting";    	    	
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
			return "redirect:/customer/profile?tab=setting";    	
		} else {
			redirectAttributes.addFlashAttribute("msg","Add address failed");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
			return "redirect:/customer/profile?tab=setting";    	    	
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
			return "redirect:/customer/profile?tab=setting";    	
		} else {
			redirectAttributes.addFlashAttribute("msg","Edit address failed");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
			return "redirect:/customer/profile?tab=setting";    	    	
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
			return "redirect:/customer/profile?tab=setting";    	
		} else {
			redirectAttributes.addFlashAttribute("msg","Delete address failed");
			redirectAttributes.addFlashAttribute("classedit","label-delivery label-cancel");
			return "redirect:/customer/profile?tab=setting";    	    	
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
