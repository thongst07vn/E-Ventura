package com.eventura.controllers.vendor;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.Roles;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.VendorSettings;
import com.eventura.entities.Vendors;
import com.eventura.entities.Wards;
import com.eventura.services.AddressService;
import com.eventura.services.MailService;
import com.eventura.services.UserService;
import com.eventura.services.VendorService;
import com.eventura.services.VendorSettingService;

@Controller("vendorAccountController")
@RequestMapping("vendor/account")
public class AccountController {

	@Autowired
	private AddressService addressService;
	@Autowired
	private UserService userService;
	@Autowired
	private VendorSettingService vendorSettingService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private MailService mailService;
	@Autowired
	private Environment environment;

	/* ===================== LOGIN / REGISTER ===================== */
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error, ModelMap modelMap) {
		if (error != null) {
			modelMap.put("msg", error);
		}
		return "vendor/pages/login/login";
	}

	@GetMapping("register")
	public String register(ModelMap modelMap) {
		Users user = new Users();
		UserAddress userAddress = new UserAddress();
		Vendors vendor = new Vendors();

		
		modelMap.put("user", user);
		modelMap.put("userAddress", userAddress);
		modelMap.put("vendor", vendor);
		modelMap.put("provinces", addressService.findAllProvinces());
		modelMap.put("vendorSettings", vendorSettingService.findAll());

		return "vendor/pages/login/register";
	}

	@PostMapping("register")
	public String register(@ModelAttribute("user") Users user, 
	                       @ModelAttribute("userAddress") UserAddress userAddress, 
	                       @ModelAttribute("vendor") Vendors vendor,
	                       @RequestParam("vendorSettingId") int vendorSettingId,
	                       @RequestParam("rePassword") String rePassword,
	                       @RequestParam(value = "provinceCode", required = false) String provinceCode,
	                       @RequestParam(value = "districtCode", required = false) String districtCode,
	                       @RequestParam(value = "wardCode", required = false) String wardCode,
	                       RedirectAttributes redirectAttributes,
	                       ModelMap modelMap) {
		
		// Check if first name and last name are empty
	    if (user.getFirstName() == null || user.getFirstName().trim().isEmpty() || 
	        user.getLastName() == null || user.getLastName().trim().isEmpty()) {
	        redirectAttributes.addFlashAttribute("msgErrorName", "* Last name or First name cannot be empty.");
	        return "redirect:/vendor/account/register";
	    }

	    // Check if phone number is 10 digits
	    if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("\\d{10}")) {
	        redirectAttributes.addFlashAttribute("msgErrorPhone", "* Phone number must be 10 digits.");
	        return "redirect:/vendor/account/register";
	    }
	    
	    if(user.getEmail() == null || user.getEmail().trim().isEmpty()) {
	    	redirectAttributes.addFlashAttribute("msgErrorEmail", "* Email cannot be empty");
	        return "redirect:/vendor/account/register";
	    }else {
	    	// Check if email already exists
		    Users existingUser = userService.findByEmail(user.getEmail());
		    if (existingUser != null) {
		        redirectAttributes.addFlashAttribute("msgErrorEmail", "* Email is already in use.");
		        return "redirect:/vendor/account/register";
		    }
	    }
	    
	    if(user.getUsername() == null || user.getUsername().trim().isEmpty()) {
	    	redirectAttributes.addFlashAttribute("msgErrorUsername", "* Username cannot be empty");
	        return "redirect:/vendor/account/register";
	    }else {
	    	// Check if username already exists
		    Users existingUsername = userService.findByUsername(user.getUsername());
		    if (existingUsername != null) {
		        redirectAttributes.addFlashAttribute("msgErrorUsername", "* Username is already in use.");
		        return "redirect:/vendor/account/register";
		    }
	    }
	    	    
	    // Check if password is between 8 and 16 characters, contains at least 1 uppercase letter and 1 number
	    String password = user.getPassword();
	    String passwordPattern = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,16}$";  // Regex to validate password

	    if (!password.matches(passwordPattern)) {
	        redirectAttributes.addFlashAttribute("msgErrorPasswordStrength", "Password must be between 8 and 16 characters, and include at least 1 uppercase letter and 1 number.");
	        return "redirect:/vendor/account/register";
	    }
	    
	    // Check if password and confirm password match
	    if (!user.getPassword().equals(rePassword)) {
	        redirectAttributes.addFlashAttribute("msgErrorPassword", "* Password and confirm password do not match.");
	        return "redirect:/vendor/account/register";
	    }

	    // Check if Provine / District / Ward is empty
	    if (provinceCode == null || districtCode == null || wardCode == null) {
	        redirectAttributes.addFlashAttribute("msgErrorPDW", "* Province-Districts-Ward cannot be empty");
	        return "redirect:/vendor/account/register";
	    }

	    // Check if address is empty
	    if (userAddress.getAddress() == null || userAddress.getAddress().trim().isEmpty() ) {
	        redirectAttributes.addFlashAttribute("msgErrorAddress", "* Address cannot be empty.");
	        return "redirect:/vendor/account/register";
	    }
	    
	    if(vendor.getName() == null || vendor.getName().trim().isEmpty()) {
	    	redirectAttributes.addFlashAttribute("msgErrorVendorName", "* Vendor's Name cannot be empty");
	        return "redirect:/vendor/account/register";
	    }
	    
	    if(vendor.getContactName() == null || vendor.getContactName().trim().isEmpty()) {
	    	redirectAttributes.addFlashAttribute("msgErrorVendorContactName", "* Vendor Contact Name cannot be empty");
	        return "redirect:/vendor/account/register";
	    }
	    
	    if(vendor.getContactEmail() == null || vendor.getContactEmail().trim().isEmpty()) {
	    	redirectAttributes.addFlashAttribute("msgErrorVendorContactEmail", "* Vendor Contact Email cannot be empty");
	        return "redirect:/vendor/account/register";
	    }

	    
		/* ROLE */
		Roles role = new Roles();
		role.setId(2);

		/* USER */
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		user.setAvatar("noimg.jpg");
		user.setCreatedAt(new Date());
		user.setDeletedAt(new Date());
		user.setRoles(role);
				

		/* USER ADDRESS */
		Provinces provinces = addressService.findProvinceById(provinceCode);
		Districts districts = addressService.findDistrictById(districtCode);
		Wards wards = addressService.findWardById(wardCode);
	
		userAddress.setUsers(user);
		userAddress.setCreatedAt(new Date());
		userAddress.setProvinces(provinces);
		userAddress.setDistricts(districts);
		userAddress.setWards(wards);
		userAddress.setName("Bao");

		 try {
		        if (userService.save(user)) {
		            System.out.println("User saved successfully.");
		        	/* VENDOR */
		    		vendor.setId(user.getId());
		    		vendor.setDescription("Your Description will be here !");	
		    		vendor.setVendorSettings(vendorSettingService.findById(vendorSettingId));
		    		vendor.setCreatedAt(new Date());
		    		vendor.setUpdatedAt(new Date());
		    		System.out.println(vendor.getId() + ',' + vendor.getName() + ',' + vendor.getContactName() + ',' + vendor.getContactEmail() + ',' + vendor.getDescription() + ',' + vendor.getVendorSettings().getVendorType() + ',' + vendor.getCreatedAt() + ',' + vendor.getUpdatedAt());
		            if (addressService.save(userAddress)) {
		                System.out.println("Address saved successfully.");
		                if(vendorService.save(vendor)) {
		                    System.out.println("Vendor saved successfully.");
		                    String baseUrl = environment.getProperty("base_url");
		                    String url = baseUrl + "vendor/account/verify?email=" + user.getEmail();

		                    String from = environment.getProperty("spring.mail.username");
		                    String to = user.getEmail();
		                    String subject = "Verify Vendor";
		                    String body = "<div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4;'>" +
		                                  "<h2 style='color: #4CAF50;'>VENDOR INFORMATION:</h2>" +
		                                  "<div style='background-color: #fff; padding: 15px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>" +
		                                  "<p style='font-size: 14px; color: #333;'><strong>Fullname:</strong> " + user.getFirstName() + " " + user.getLastName() + "</p>" +
		                                  "<p style='font-size: 14px; color: #333;'><strong>Username:</strong> " + user.getUsername() + "</p>" +
		                                  "<p style='font-size: 14px; color: #333;'><strong>Phone Number:</strong> " + user.getPhoneNumber() + "</p>" +
		                                  "<p style='font-size: 14px; color: #333;'><strong>Email:</strong> " + user.getEmail() + "</p>" +
		                                  "<p style='font-size: 14px; color: #333;'><strong>Address:</strong> " + userAddress.getAddress() + ", " + userAddress.getWards().getName() + ", " + userAddress.getDistricts().getName() + ", " + userAddress.getProvinces().getName() + "</p>" +
		                                  "</div>" +
		                                  "<p style='font-size: 14px; color: #333;'>Click <a href='" + url + "' style='color: #4CAF50; text-decoration: none;'>here</a> to activate your Vendor Account.</p>" +
		                                  "</div>";

		                    if (mailService.send(from, to, subject, body)) {
		                        redirectAttributes.addFlashAttribute("msg", "Vào email để kích hoạt tài khoản");
		                    } else {
		                        redirectAttributes.addFlashAttribute("msgFailed", "Gửi mail kích hoạt tài khoản thất bại");
		                       

		                    }
		                    return "redirect:/vendor/account/register";
		                } else {
		                    redirectAttributes.addFlashAttribute("msgFailed", "Register Failed");
		                    setModelMap(modelMap, user, userAddress, vendor);
		                    return "redirect:/vendor/account/register";
		                }
		            } else {
		                redirectAttributes.addFlashAttribute("msgFailed", "Failed to save address");
		                return "redirect:/vendor/account/register";
		            }
		        } else {
		            redirectAttributes.addFlashAttribute("msgFailed", "Failed to save user");
		            return "redirect:/vendor/account/register";
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        redirectAttributes.addFlashAttribute("msgFailed", "Unexpected error: " + e.getMessage());
		        return "redirect:/vendor/account/register";
		    }
	}

	private void setModelMap(ModelMap modelMap, Users user, UserAddress userAddress, Vendors vendor) {
	    modelMap.put("selectedLastName", user.getLastName());
	    modelMap.put("selectedFirstName", user.getFirstName());
	    modelMap.put("selectedPhone", user.getPhoneNumber());
	    modelMap.put("selectedEmail", user.getEmail());
	    modelMap.put("selectedUsername", user.getUsername());
	    modelMap.put("selectedProvinceCode", userAddress.getProvinces().getCode());
	    modelMap.put("selectedDistrictCode", userAddress.getDistricts().getCode());
	    modelMap.put("selectedWardCode", userAddress.getWards().getCode());
	    modelMap.put("selectedAddress", userAddress.getAddress());
	    modelMap.put("selectedVendorName", vendor.getName());
	    modelMap.put("selectedVendorContactName", vendor.getContactName());
	    modelMap.put("selectedVendorContactEmail", vendor.getContactEmail());
	    modelMap.put("selectedVendorTypeCode", vendor.getVendorSettings().getId());
	}
	
	/* Verify */
	@GetMapping("test")
	public String test() {
	    Vendors vendor = new Vendors();
	    vendor.setId(20);
	    vendor.setName("Bao");
	    vendor.setContactName("sdsdsd");
	    vendor.setContactEmail("sdsd@gmail.com");
	    vendor.setDescription("Your Description will be here !");  
	    vendor.setVendorSettings(vendorSettingService.findById(1));
	    vendor.setCreatedAt(new Date());
	    vendor.setUpdatedAt(new Date());
	    
	    // Lưu đối tượng vendor vào cơ sở dữ liệu
	    vendorService.save(vendor);  // Giả sử bạn đã inject vendorService
	    
	    return "vendor/pages/login/login";  // Chuyển hướng về trang đăng ký
	}


	/* Verify */
	@GetMapping({ "verify" })
	public String verify(@RequestParam("email") String email,
				  	      RedirectAttributes redirectAttributes) {

		Users user = userService.findByEmail(email);
		if (user == null) {
			redirectAttributes.addFlashAttribute("msgActive", "Tài khoản không hợp lệ1");
			return "redirect:/vendor/account/login";
		} else {
			user.setDeletedAt(null);
			if (userService.save(user)) {
				redirectAttributes.addFlashAttribute("msgActive", "Kích hoạt tài khoản thành công");
				return "redirect:/vendor/account/login";
			} else {
				redirectAttributes.addFlashAttribute("msgActive", "Tài khoản không hợp lệ");
				return "redirect:/vendor/account/login";
			}
		}
	}
}

