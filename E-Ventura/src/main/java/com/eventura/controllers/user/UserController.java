package com.eventura.controllers.user;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderItemsOrderStatus;

import com.eventura.entities.Orders;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.ProductReviews;
import com.eventura.entities.Products;
import com.eventura.entities.Provinces;
import com.eventura.entities.Roles;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.VendorReviews;
import com.eventura.entities.Vendors;
import com.eventura.entities.Wards;
import com.eventura.helpers.FileHelper;
import com.eventura.helpers.RandomStringCode;
import com.eventura.services.AddressService;
import com.eventura.services.CategoryService;

import com.eventura.services.MailService;

import com.eventura.services.OrderItemService;
import com.eventura.services.OrderService;
import com.eventura.services.OrderStatusService;

import com.eventura.services.ProductService;
import com.eventura.services.RoleService;
import com.eventura.services.UserService;
import com.eventura.services.VendorReviewService;
import com.eventura.services.VendorService;

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
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private OrderStatusService orderStatusService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private VendorReviewService vendorReviewService;

	@GetMapping({ "home", "/" })
	public String home(ModelMap modelMap, HttpSession session) {
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
				redirectAttributes.addFlashAttribute("sweetAlert", "error");
				redirectAttributes.addFlashAttribute("message", "* Email is already in use.");
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
				redirectAttributes.addFlashAttribute("sweetAlert", "error");
				redirectAttributes.addFlashAttribute("message", "* Username is already in use.");
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

		if (addressService.save(userAddress)) {
			// Send verification email
			String baseUrl = environment.getProperty("base_url");
			String url = baseUrl + "customer/verify?email=" + user.getEmail();

			String from = environment.getProperty("spring.mail.username");
			String to = user.getEmail();
			String subject = "Verify Vendor";
			String body = "<div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4;'>"
					+ "<h2 style='color: #4CAF50;'>CUSTOMER INFORMATION:</h2>"
					+ "<div style='background-color: #fff; padding: 15px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>"
					+ "<p><strong>Fullname:</strong> " + user.getFirstName() + " " + user.getLastName() + "</p>"
					+ "<p><strong>Username:</strong> " + user.getUsername() + "</p>"
					+ "<p><strong>Phone Number:</strong> " + user.getPhoneNumber() + "</p>"
					+ "<p><strong>Email:</strong> " + user.getEmail() + "</p>" + "<p><strong>Address:</strong> "
					+ userAddress.getAddress() + ", " + userAddress.getWards().getName() + ", "
					+ userAddress.getDistricts().getName() + ", " + userAddress.getProvinces().getName() + "</p>"
					+ "</div>" + "<p>Click <a href='" + url
					+ "' style='color: #4CAF50;'>here</a> to activate your Vendor Account.</p>" + "</div>";

			if (mailService.send(from, to, subject, body)) {
				redirectAttributes.addFlashAttribute("sweetAlert", "success");
				redirectAttributes.addFlashAttribute("message",
						"Register Sucessfully, Check your email to activate your account.");

			} else {
				redirectAttributes.addFlashAttribute("sweetAlert", "error");
				redirectAttributes.addFlashAttribute("message", "Register Failed");

			}

		}
		return "redirect:/customer/register";
	}
	//reset-pass
	@PostMapping("forgot-password")
	public String forgotPassword(@RequestParam("forgotEmailInput") String forgotEmailInput, RedirectAttributes redirectAttributes,
	        ModelMap modelMap) {

	    // 1. Find the user by email (assuming a UserService or UserRepository)
	    // You need to implement this part based on your user management.
	    // For example: User user = userService.findByEmail(forgotEmailInput);
	    // If user is not found, you might want to return a specific message,
	    // but for security, often you'd send a generic "if an account exists..." message
	    // to prevent email enumeration.

	    // For demonstration, let's assume the email is valid and we're just sending the email.
	    // In a real app, you'd check if a user exists for forgotEmailInput.

	    // 2. Generate a unique, time-limited token
	    // This token should be stored in your database along with its expiry date,
	    // linked to the user's account.
	    String resetToken = RandomStringCode.generateRandomAlphaNumeric(10); // Basic token generation
	    Users user = userService.findByEmail(forgotEmailInput);
	    user.setRememberToken(resetToken);
	    userService.save(user);
	    // You would save this token to the database along with its expiry.
	    // E.g., userService.createPasswordResetTokenForUser(user, resetToken);

	    // 3. Construct the reset URL with the token
	    String baseUrl = environment.getProperty("base_url");
	    // Ensure baseUrl ends with a slash if not already handled.
	    String url = baseUrl + "customer/change-password?token=" + resetToken+"&email="+forgotEmailInput; // Changed to token

	    String from = environment.getProperty("spring.mail.username");
	    String to = forgotEmailInput;
	    String subject = "Password Reset Request for Your Account";
	    String body = "<div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4;'>"
	            + "<h2 style='color: #4CAF50;'>PASSWORD RESET REQUEST:</h2>"
	            + "<div style='background-color: #fff; padding: 15px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>"
	            + "<p>Hello,</p>" // You might not have the user's name readily here, or fetch it
	            + "<p>We received a request to reset the password for your account associated with the email address: <strong>" + forgotEmailInput + "</strong>.</p>"
	            + "<p>If you made this request, please click the link below to reset your password:</p>"
	            + "<p style='text-align: center; margin: 20px 0;'>"
	            + "<a href='" + url + "' style='display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: #ffffff; text-decoration: none; border-radius: 5px;'>"
	            + "Reset Your Password"
	            + "</a>"
	            + "</p>"
	            + "<p>This link will expire in **24 hours**. (Make sure to implement this expiry check in your backend)</p>"
	            + "<p>If you did not request a password reset, please ignore this email. Your password will remain unchanged.</p>"
	            + "</div>"
	            + "<p style='font-size: 12px; color: #888; text-align: center; margin-top: 20px;'>&copy; Your Company Name " + java.time.Year.now().getValue() + "</p>"
	            + "</div>";

	    if (mailService.send(from, to, subject, body)) {
	        redirectAttributes.addFlashAttribute("sweetAlert", "success");
	        // Correct message for password reset request
	        redirectAttributes.addFlashAttribute("message",
	                "If an account with that email exists, a password reset link has been sent to " + forgotEmailInput + ".");
	        
	    } else {
	        redirectAttributes.addFlashAttribute("sweetAlert", "error");
	        redirectAttributes.addFlashAttribute("message", "Failed to send password reset email. Please try again later.");
	    }
	    
	    // Redirect to a more appropriate page, e.g., login or a confirmation page
	    return "redirect:/customer/login"; // Or a page that just says "Check your email"
	}
	@GetMapping("change-password")
	public String changePassword(@RequestParam("token") String resetToken,@RequestParam("email") String email, RedirectAttributes redirectAttributes,
	        ModelMap modelMap) {

	    // 1. Validate the resetToken
	    // You'd have a service method to find and validate the token.
	    // For example: PasswordResetToken token = userService.getPasswordResetToken(resetToken);
	    // If token is null, expired, or invalid, redirect with an error.

	    // For demonstration:
	    boolean isValidToken = true; // Replace with actual token validation logic
	    // You'd also get the user associated with this token to display their email or username
	    // String userEmail = token.getUser().getEmail(); // If you stored user with token

	    if (isValidToken) { // Replace with actual token validation
	        modelMap.addAttribute("resetToken", resetToken); // Pass token to the view
	        modelMap.addAttribute("email",email);
	        // Optionally, pass the email associated with the token to pre-fill or display
	        // modelMap.addAttribute("userEmail", userEmail);
	        return "customer/pages/account/changepassword";
	    } else {
	        redirectAttributes.addFlashAttribute("sweetAlert", "error");
	        redirectAttributes.addFlashAttribute("message", "Invalid or expired password reset link.");
	        return "redirect:/customer/login"; // Or another appropriate page
	    }
	}
	@PostMapping("update-password")
	public String updatePassword(@RequestParam("token") String resetToken,@RequestParam("email") String email,
	                             @RequestParam("newPassword") String newPassword,
	                             @RequestParam("confirmNewPassword") String confirmNewPassword,
	                             RedirectAttributes redirectAttributes) {
		
	    // 1. Validate the token again (important for security)
	    // 2. Validate that newPassword and confirmNewPassword match
	    // 3. Update the user's password in the database (encrypt it!)
	    // 4. Invalidate the reset token after successful password change
		System.out.println("cc");
	    if (!newPassword.equals(confirmNewPassword)) {
	        redirectAttributes.addFlashAttribute("sweetAlert", "error");
	        redirectAttributes.addFlashAttribute("message", "New password and confirmation do not match.");
	        // Redirect back to the change password page with the token
	        return "redirect:/customer/change-password?token=" + resetToken;
	    }
	    Users user = userService.findByEmail(email);
	    if(resetToken.equals(user.getRememberToken())) {
	    	user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
	    	user.setRememberToken(null);
	    	userService.save(user);
	    	redirectAttributes.addFlashAttribute("sweetAlert", "success");
	 	    redirectAttributes.addFlashAttribute("message", "Your password has been successfully reset. Please login with your new password.");
	 	   return "redirect:/customer/login";
	    }else {
	    	redirectAttributes.addFlashAttribute("sweetAlert", "error");
	        redirectAttributes.addFlashAttribute("message", "token wrong.");
	        return "redirect:/customer/change-password?token=" + resetToken;
	    }
	    
	    // Example of token validation and password update (you need to implement this fully)
	    // PasswordResetToken token = userService.getPasswordResetToken(resetToken);
	    // if (token == null || token.isExpired() || !token.isValid()) { ... error ... }
	    // User user = token.getUser();
	    // userService.updatePassword(user, newPassword); // Encrypt password here!
	    // userService.invalidateToken(token); // Mark token as used/invalidated

	   
  }
	
	/* Verify */
	@GetMapping({ "verify" })
	public String verify(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {

		Users user = userService.findByEmail(email);
		if (user == null) {
			redirectAttributes.addFlashAttribute("msgActive", "Tài khoản không hợp lệ1");
			return "redirect:/vendor/account/login";
		} else {
			if (userService.save(user)) {
				redirectAttributes.addFlashAttribute("sweetAlert", "success");
				redirectAttributes.addFlashAttribute("message", "Verify Successfully");
			} else {
				redirectAttributes.addFlashAttribute("sweetAlert", "error");
				redirectAttributes.addFlashAttribute("message", "Failed");
				
			}
		}
		return "redirect:/customer/login";

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
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam(name = "tab", required = false, defaultValue = "orders") String activeTab) {
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
		modelMap.addAttribute("activeTab", activeTab);

		return "customer/pages/account/profile";
	}

	@PostMapping({ "edit/{id}" })
	public String editProfile(@ModelAttribute("user") Users user, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails, @RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		Users currentUser = null;

		// 1. Centralize currentUser retrieval
		if (userDetails != null) {
			currentUser = userService.findByEmail(userDetails.getUsername());
		} else if (authentication != null && authentication.getPrincipal() instanceof AccountOAuth2User) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			currentUser = userService.findByEmail(accountOAuth2User.getEmail());
			if (currentUser.getPassword() == null) {
				user.setPassword(BCrypt.hashpw("123456789@T", BCrypt.gensalt()));
			}
		}

		// 2. Handle file upload for avatar
		if (file != null && !file.isEmpty()) {
			try {
				String fileName = FileHelper.generateFileName(file.getOriginalFilename());
				// Using getFile() might fail if running from a JAR; consider using
				// getResourceAsStream() and Files.copy()
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
			// If currentUser is null here, it means no existing user found, avatar would
			// remain null or default
		}

		// 3. Preserve rememberToken and password if currentUser was found
		if (currentUser != null) {
			user.setRememberToken(currentUser.getRememberToken());
			user.setPassword(currentUser.getPassword());
			user.setEmail(currentUser.getEmail());
			user.setRoles(currentUser.getRoles());
		}

		if (userService.save(user)) {
			redirectAttributes.addFlashAttribute("msg", "Edit profile success");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-delivered");
			return "redirect:/customer/profile?tab=setting";
		} else {
			redirectAttributes.addFlashAttribute("msg", "Edit profile failed");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-cancel");
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

		if (userService.save(currentUser)) {
			redirectAttributes.addFlashAttribute("msg", "Change password success");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-delivered");
			return "redirect:/customer/profile?tab=setting";
		} else {
			redirectAttributes.addFlashAttribute("msg", "Change password failed");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-cancel");
			return "redirect:/customer/profile?tab=setting";
		}
	}

	@PostMapping({ "addaddress" })
	public String AddAddress(@RequestParam("userId") int userId,
			@ModelAttribute("addAddressVariable") UserAddress addAddressVariable,
			RedirectAttributes redirectAttributes) {
		Users user = userService.findById(userId);
		addAddressVariable.setUsers(user);
		addAddressVariable.setCreatedAt(new Date());
		if (addAddressVariable.getName() == null || addAddressVariable.getName().trim().isEmpty()) {
			addAddressVariable.setName(user.getUsername());
		}
		if (addressService.save(addAddressVariable)) {
			redirectAttributes.addFlashAttribute("msg", "Add address success");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-delivered");
			return "redirect:/customer/profile?tab=setting";
		} else {
			redirectAttributes.addFlashAttribute("msg", "Add address failed");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-cancel");
			return "redirect:/customer/profile?tab=setting";
		}
	}

	@PostMapping({ "editaddress" })
	public String EditAddress(@RequestParam("userId") int userId, @RequestParam("editProvince") String editProvince,
			@RequestParam("editDistrict") String editDistrict, @RequestParam("editWard") String editWard,
			@ModelAttribute("addAddressVariable") UserAddress addAddressVariable,
			RedirectAttributes redirectAttributes) {
		Users user = userService.findById(userId);
		addAddressVariable.setUsers(user);
		addAddressVariable.setCreatedAt(new Date());
		addAddressVariable.setProvinces(addressService.findProvinceById(editProvince));
		addAddressVariable.setDistricts(addressService.findDistrictById(editDistrict));
		addAddressVariable.setWards(addressService.findWardById(editWard));

		if (addressService.save(addAddressVariable)) {
			redirectAttributes.addFlashAttribute("msg", "Edit address success");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-delivered");
			return "redirect:/customer/profile?tab=setting";
		} else {
			redirectAttributes.addFlashAttribute("msg", "Edit address failed");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-cancel");
			return "redirect:/customer/profile?tab=setting";
		}
//		return "redirect:/customer/profile";    	    	

	}

	@PostMapping({ "deleteaddress" })
	public String DeleteAddress(@RequestParam("addressId") int addressId, RedirectAttributes redirectAttributes) {
		UserAddress address = addressService.findById(addressId);
		address.setDeletedAt(new Date());
		System.out.println(address.getName());
		if (addressService.save(address)) {
			redirectAttributes.addFlashAttribute("msg", "Delete address success");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-delivered");
			return "redirect:/customer/profile?tab=setting";
		} else {
			redirectAttributes.addFlashAttribute("msg", "Delete address failed");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-cancel");
			return "redirect:/customer/profile?tab=setting";
		}
//		return "redirect:/customer/profile";    	    	

	}

	// Check out Address
	@PostMapping({ "addaddressincheckout" })
	public String AddAddressIncheckout(@RequestParam("userId") int userId,
			@ModelAttribute("addAddressVariable") UserAddress addAddressVariable, RedirectAttributes redirectAttributes,
			@RequestParam("checkoutItems") String checkoutItems) {
		Users user = userService.findById(userId);
		addAddressVariable.setUsers(user);
		addAddressVariable.setCreatedAt(new Date());
		if (addAddressVariable.getName() == null || addAddressVariable.getName().trim().isEmpty()) {
			addAddressVariable.setName(user.getUsername());
		}
		if (addressService.save(addAddressVariable)) {
			redirectAttributes.addFlashAttribute("msg", "Add address success");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-delivered");
			return "redirect:/customer/cart/checkout?checkoutItems=" + checkoutItems;
		} else {
			redirectAttributes.addFlashAttribute("msg", "Add address failed");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-cancel");
			return "redirect:/customer/cart/checkout?checkoutItems=" + checkoutItems;
		}
	}

	@PostMapping({ "editaddressincheckout" })
	public String EditAddressIncheckout(@RequestParam("userId") int userId,
			@RequestParam("editProvince") String editProvince, @RequestParam("editDistrict") String editDistrict,
			@RequestParam("editWard") String editWard,
			@ModelAttribute("addAddressVariable") UserAddress addAddressVariable, RedirectAttributes redirectAttributes,
			@RequestParam("checkoutItems") String checkoutItems) {
		Users user = userService.findById(userId);
		addAddressVariable.setUsers(user);
		addAddressVariable.setCreatedAt(new Date());
		addAddressVariable.setProvinces(addressService.findProvinceById(editProvince));
		addAddressVariable.setDistricts(addressService.findDistrictById(editDistrict));
		addAddressVariable.setWards(addressService.findWardById(editWard));

		if (addressService.save(addAddressVariable)) {
			redirectAttributes.addFlashAttribute("msg", "Edit address success");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-delivered");
			return "redirect:/customer/cart/checkout?checkoutItems=" + checkoutItems;
		} else {
			redirectAttributes.addFlashAttribute("msg", "Edit address failed");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-cancel");
			return "redirect:/customer/cart/checkout?checkoutItems=" + checkoutItems;
		}
//		return "redirect:/customer/profile";    	    			
	}

	@PostMapping({ "deleteaddressincheckout" })
	public String DeleteAddressIncheckout(@RequestParam("addressId") int addressId,
			RedirectAttributes redirectAttributes, @RequestParam("checkoutItems") String checkoutItems) {
		UserAddress address = addressService.findById(addressId);
		address.setDeletedAt(new Date());
		System.out.println(address.getName());
		if (addressService.save(address)) {
			redirectAttributes.addFlashAttribute("msg", "Delete address success");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-delivered");
			return "redirect:/customer/cart/checkout?checkoutItems=" + checkoutItems;
		} else {
			redirectAttributes.addFlashAttribute("msg", "Delete address failed");
			redirectAttributes.addFlashAttribute("classedit", "label-delivery label-cancel");
			return "redirect:/customer/cart/checkout?checkoutItems=" + checkoutItems;
		}
//		return "redirect:/customer/profile";    	    	

	}

	// User Orders
	@GetMapping({ "orders" })
	public String orders(Authentication authentication, ModelMap modelMap, Model model,
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam(name = "tab", required = false, defaultValue = "orders") String activeTab,
			@RequestParam(defaultValue = "0") int page) {
		Users user = new Users();

		if (userDetails != null) {
			user = userService.findByEmail(userDetails.getUsername());
			List<UserAddress> userAdresses = userService.findAddressUser(user.getId());
			modelMap.put("userAdresses", userAdresses);
			modelMap.put("user", user);
		} else if (authentication != null) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			user = userService.findByEmail(accountOAuth2User.getEmail());
			List<UserAddress> userAdresses = userService.findAddressUser(user.getId());
			modelMap.put("userAdresses", userAdresses);
			modelMap.put("user", user);
		}
		modelMap.put("addAddressVariable", new UserAddress());
		modelMap.put("provinces", addressService.findAllProvinces());
		modelMap.addAttribute("activeTab", activeTab);
		/* ORDER CỦA BẢO */
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Orders> orderPage = orderService.findOrdersByUserId(user.getId(), pageable);

		// Map này sẽ lưu trạng thái mới nhất cho mỗi nhóm vendor trong mỗi Order
		// Key: Order ID -> Value: (Key: Vendor ID -> Value: Trạng thái mới nhất của
		// nhóm đó)
		Map<Integer, Map<Integer, OrderItemsOrderStatus>> latestStatusesByOrderAndVendor = new LinkedHashMap<>();
		// Map này vẫn giữ nguyên cấu trúc cũ để truyền danh sách OrderItems
		Map<Integer, Map<Integer, List<OrderItems>>> allGroupedOrderItems = new LinkedHashMap<>();

		for (Orders order : orderPage) {
			List<OrderItems> orderItems = orderItemService.findAllOrderItemsByOrderId(order.getId());
			Map<Integer, List<OrderItems>> groupedByVendor = orderItemService.groupOrderItemsByVendor(orderItems);

			// Xử lý để lấy trạng thái mới nhất cho mỗi nhóm vendor
			Map<Integer, OrderItemsOrderStatus> vendorLatestStatuses = new LinkedHashMap<>();
			for (Map.Entry<Integer, List<OrderItems>> vendorEntry : groupedByVendor.entrySet()) {
				List<OrderItems> itemsForVendor = vendorEntry.getValue();
				if (!itemsForVendor.isEmpty()) {
					OrderItems firstItemInGroup = itemsForVendor.get(0); // Lấy một item bất kỳ trong nhóm
					// Tìm trạng thái mới nhất cho item này
					OrderItemsOrderStatus latestStatus = firstItemInGroup.getOrderItemsOrderStatuses().stream()
							.max(Comparator.comparing(OrderItemsOrderStatus::getCreatedAt)).orElse(null);
					vendorLatestStatuses.put(vendorEntry.getKey(), latestStatus);
				}
			}
			latestStatusesByOrderAndVendor.put(order.getId(), vendorLatestStatuses);
			allGroupedOrderItems.put(order.getId(), groupedByVendor);
		}

		modelMap.put("groupedOrderItemsByOrder", allGroupedOrderItems);
		modelMap.put("latestStatusesByOrderAndVendor", latestStatusesByOrderAndVendor); // THÊM MAP NÀY VÀO MODEL

		modelMap.put("orders", orderPage);
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", orderPage.getTotalPages());
		model.addAttribute("lastPageIndex", orderPage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);

		return "customer/pages/account/profile";
	}

	// User orderTracking
	@GetMapping({ "order-tracking/{orderId}/{vendorId}" })
	public String orderTracking(Authentication authentication, ModelMap modelMap,
			@AuthenticationPrincipal UserDetails userDetails, @PathVariable("orderId") int orderId,
			@PathVariable("vendorId") int vendorId,
			@RequestParam(name = "tab", required = false, defaultValue = "order-tracking") String activeTab) {

		Users user = new Users();

		if (userDetails != null) {
			user = userService.findByEmail(userDetails.getUsername());
		} else if (authentication != null) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			user = userService.findByEmail(accountOAuth2User.getEmail());
		}

		List<UserAddress> userAdresses = userService.findAddressUser(user.getId());
		modelMap.put("userAdresses", userAdresses);
		modelMap.put("user", user);

		modelMap.put("addAddressVariable", new UserAddress());
		modelMap.put("provinces", addressService.findAllProvinces());
		modelMap.addAttribute("activeTab", activeTab);

		List<OrderItems> orderItems = orderItemService.findAllOrderItemsByOrderIdAndVendorIdWithStatuses(orderId,
				vendorId);

		OrderItems representativeItem = null;
		if (!orderItems.isEmpty()) {
			representativeItem = orderItems.get(0);
		}

		List<OrderItemsOrderStatus> sortedStatuses = new ArrayList<>();
		OrderItemsOrderStatus currentStatus = null;

		Set<String> occurredStatusNames = new LinkedHashSet<>(); // Changed to LinkedHashSet to maintain insertion order

		if (representativeItem != null && representativeItem.getOrderItemsOrderStatuses() != null) {
			sortedStatuses = representativeItem.getOrderItemsOrderStatuses().stream()
					.sorted(Comparator.comparing(OrderItemsOrderStatus::getCreatedAt)).collect(Collectors.toList());

			currentStatus = sortedStatuses.isEmpty() ? null : sortedStatuses.get(sortedStatuses.size() - 1);

			// Populate occurredStatusNames from sortedStatuses
			sortedStatuses.forEach(s -> occurredStatusNames.add(s.getOrderStatus().getName()));
		}

		String currentStatusName = currentStatus != null ? currentStatus.getOrderStatus().getName() : null;

		modelMap.put("occurredStatusNames", occurredStatusNames);
		modelMap.put("currentStatusName", currentStatusName);

		Map<String, Date> statusToDateMap = new LinkedHashMap<>();
		if (sortedStatuses != null) {
			for (OrderItemsOrderStatus s : sortedStatuses) {
				statusToDateMap.put(s.getOrderStatus().getName(), s.getCreatedAt());
			}
		}

		// Fetch existing vendor review for the current user and vendor
		VendorReviews existingVendorReview = vendorReviewService.findVendorReviewByUserAndVendorId(vendorId,
				user.getId());
		modelMap.put("existingVendorReview", existingVendorReview);

		// Fetch existing product reviews for the current user and order items
		Map<Integer, ProductReviews> existingProductReviews = new HashMap<>();
		boolean allProductsReviewed = true; // Assume all products are reviewed initially
		for (OrderItems item : orderItems) {
			ProductReviews pr = productService.findProductReviewByUserAndProductId(user.getId(),
					item.getProducts().getId());
			if (pr != null) {
				existingProductReviews.put(item.getProducts().getId(), pr);
				if (pr.getRating() == 0) { // If any product has a 0 rating, they are not all reviewed
					allProductsReviewed = false;
				}
			} else {
				allProductsReviewed = false; // If a product has no review, they are not all reviewed
			}
		}
		modelMap.put("existingProductReviews", existingProductReviews);
		modelMap.put("allProductsReviewed", allProductsReviewed); // New model attribute

		modelMap.put("statusToDateMap", statusToDateMap);

		modelMap.put("orderItemsForTracking", orderItems);
		modelMap.put("orderStatusHistory", sortedStatuses);
		modelMap.put("currentOrderStatus", currentStatus);

		modelMap.put("trackingOrderId", orderId);
		modelMap.put("trackingVendorId", vendorId);
		modelMap.put("vendor", vendorService.findById(vendorId));

		return "customer/pages/account/orderTracking";
	}

	// rating
	@PostMapping("/rating")
	public String handleSubmitRating(@RequestParam("entityType") String entityType,
			@RequestParam("entityId") int entityId, @RequestParam("rating") Integer rating,
			@RequestParam(value = "review", required = false) String review, RedirectAttributes redirectAttributes,
			@RequestParam("orderId") int orderId, @RequestParam("vendorId") int vendorId, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails) {

		Users user = new Users();

		if (userDetails != null) {
			user = userService.findByEmail(userDetails.getUsername());
		} else if (authentication != null) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			user = userService.findByEmail(accountOAuth2User.getEmail());
		}

		if ("vendor".equals(entityType)) {
			VendorReviews vendorReviews = vendorReviewService.findVendorReviewByUserAndVendorId(vendorId, user.getId());

			// Only allow rating if no existing valid rating is found
			if (vendorReviews == null || vendorReviews.getRating() == 0) { // Check for null or an unrated placeholder
				if (vendorReviews == null) {
					vendorReviews = new VendorReviews(user, vendorService.findById(vendorId), rating, false, new Date(),
							new Date());
				} else {
					// If a placeholder exists with rating 0, update it
					vendorReviews.setRating(rating);
					vendorReviews.setUpdatedAt(new Date()); // Update timestamp
				}
				vendorReviewService.save(vendorReviews);
				redirectAttributes.addFlashAttribute("successMessage", "Vendor rated successfully!");
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "You have already rated this vendor.");
			}
		} else if ("product".equals(entityType)) {
			ProductReviews productReviews = productService.findProductReviewByUserAndProductId(user.getId(), entityId);

			// Only allow rating if no existing valid rating is found for the product
			if (productReviews == null || productReviews.getRating() == 0) {
				if (productReviews == null) {
					productReviews = new ProductReviews();
					productReviews.setUsers(user);
					productReviews.setProducts(productService.findById(entityId));
					productReviews.setRating(rating);
					productReviews.setComment(review);
					productReviews.setCreatedAt(new Date());
					productReviews.setUpdatedAt(new Date());
				} else {
					// If a placeholder exists with rating 0, update it
					productReviews.setRating(rating);
					productReviews.setComment(review);
					productReviews.setUpdatedAt(new Date());
				}
				productService.saveProductReview(productReviews);
				redirectAttributes.addFlashAttribute("successMessage", "Product rated successfully!");
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "You have already rated this product.");
			}
		}

		System.out.println("entityId: " + entityId);
		System.out.println("entityType: " + entityType);
		System.out.println("rating: " + rating);
		System.out.println("review: " + review);
		System.out.println("orderId: " + orderId);
		System.out.println("vendorId: " + vendorId);

		return "redirect:/customer/order-tracking/" + orderId + "/" + vendorId;
	}
}
