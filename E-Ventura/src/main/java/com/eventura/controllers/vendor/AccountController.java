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
import com.eventura.entities.Wards;
import com.eventura.services.AddressService;
import com.eventura.services.MailService;
import com.eventura.services.UserService;

@Controller("vendorAccountController")
@RequestMapping("vendor/account")
public class AccountController {

	@Autowired
	private AddressService addressService;
	@Autowired
	private UserService userService;
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

		modelMap.put("user", user);
		modelMap.put("userAddress", userAddress);
		modelMap.put("provinces", addressService.findAllProvinces());

		return "vendor/pages/login/register";
	}

	@PostMapping("register")
	public String register(@ModelAttribute("user") Users user, @ModelAttribute("userAddress") UserAddress userAddress, @RequestParam("rePassword") String rePassword,
			@RequestParam("provinceCode") String provinceCode, @RequestParam("districtCode") String districtCode,
			@RequestParam("wardCode") String wardCode, RedirectAttributes redirectAttributes) {
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

		if (userService.save(user)) {
			if (addressService.save(userAddress)) {
				String baseUrl = environment.getProperty("base_url"); // Đọc cấu hình base_url từ application.properties
				String url = baseUrl + "vendor/account/verify?email=" + user.getEmail();

				String from = environment.getProperty("spring.mail.username");
				String to = user.getEmail();
				String subject = "Verify Account";
				String body = "Click <a href='" + url + "'>here</a> to activate your account";

				if (mailService.send(from, to, subject, body)) {
					redirectAttributes.addFlashAttribute("msg", "Vào email để kích hoạt tài khoản");
				} else {
					redirectAttributes.addFlashAttribute("msg", "Gửi mail kích hoạt tài khoản thất bại");
				}
				return "redirect:/vendor/account/register";

			} else {
				redirectAttributes.addFlashAttribute("msg", "Register Failed");
				return "redirect:/vendor/account/register";
			}
		}

		return "redirect:/vendor/account/register";
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

