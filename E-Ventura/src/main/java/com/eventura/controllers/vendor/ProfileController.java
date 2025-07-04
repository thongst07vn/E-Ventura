package com.eventura.controllers.vendor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.Roles;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.Wards;
import com.eventura.services.AddressService;
import com.eventura.services.UserService;
import com.eventura.helpers.FileHelper;

import jakarta.servlet.http.HttpSession;
@Controller("vendorProfileController")
@RequestMapping("vendor/profile")
public class ProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;

    @GetMapping("edit")
    public String editProfile(HttpSession session, ModelMap modelMap) {
        modelMap.put("currentPage", "profile");

        Integer vendorId = (Integer) session.getAttribute("vendorId");
        Users user = userService.findById(vendorId);
		List<UserAddress> userAdresses = userService.findAddressUser(user.getId());
		
        modelMap.put("user", user);
		modelMap.put("userAdresses", userAdresses.get(0));
        modelMap.put("provinces", addressService.findAllProvinces());

        if (!userAdresses.isEmpty()) {
            String provinceCode = userAdresses.get(0).getProvinces().getCode();
            String districtCode = userAdresses.get(0).getDistricts().getCode();

            modelMap.put("districts", addressService.findDistrictsByProvinceCode(provinceCode));
            modelMap.put("wards", addressService.findWardsByDistrictCode(districtCode));
        } else {
            // Nếu không có địa chỉ, truyền vào model thông tin mặc định
            modelMap.put("districts", List.of());
            modelMap.put("wards", List.of());
        }

        return "vendor/pages/profile/edit";
    }


    @PostMapping("edit")
    public String editProfile(@ModelAttribute("user") Users user, @ModelAttribute("userAddress") UserAddress userAddress,
                               ModelMap modelMap,@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, HttpSession session) {
        modelMap.put("currentPage", "profile");
        
        
     // Check if first name and last name are empty
     		if (user.getFirstName() == null || user.getFirstName().trim().isEmpty() || user.getLastName() == null
     				|| user.getLastName().trim().isEmpty()) {
     			redirectAttributes.addFlashAttribute("msgErrorName", "* Last name or First name cannot be empty.");
     			return "redirect:/vendor/profile/edit";
     		}
     		
     		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
     			redirectAttributes.addFlashAttribute("msgErrorEmail", "* Email cannot be empty");
     			return "redirect:/vendor/profile/edit";
     		} 

     		// Check if phone number is 10 digits
     		if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("\\d{10}")) {
     			redirectAttributes.addFlashAttribute("msgErrorPhone", "* Phone number must be 10 digits.");
     			return "redirect:/vendor/profile/edit";
     		}
     		
    		// Check if address is empty
    		if (userAddress.getAddress() == null || userAddress.getAddress().trim().isEmpty()) {
    			redirectAttributes.addFlashAttribute("msgErrorAddress", "* Address cannot be empty.");
    			return "redirect:/vendor/profile/edit";
    		}
     		
     		
     	// Check if Provine / District / Ward is empty
    		if (userAddress.getProvinces() == null || userAddress.getDistricts() == null || userAddress.getWards() == null) {
    			redirectAttributes.addFlashAttribute("msgErrorPDW", "* Province-Districts-Ward cannot be empty");
     			return "redirect:/vendor/profile/edit";
    		}
     		

     		if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
     			redirectAttributes.addFlashAttribute("msgErrorUsername", "* Username cannot be empty");
     			return "redirect:/vendor/profile/edit";
     		} 
        
        Integer vendorId = (Integer) session.getAttribute("vendorId");
        Users currentUser = userService.findById(vendorId);
      

        // Cập nhật thông tin người dùng
        Roles role = new Roles();
        role.setId(2); // Vai trò của Vendor
        user.setRoles(role);
        user.setCreatedAt(new Date());
        user.setAvatar("nomig.jpg");
        
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
        
        // Lưu người dùng vào cơ sở dữ liệu
        userService.save(user);
        
        userAddress.setProvinces(addressService.findProvinceById(userAddress.getProvinces().getCode()));
        userAddress.setDistricts(addressService.findDistrictById(userAddress.getDistricts().getCode()));
        userAddress.setWards(addressService.findWardById(userAddress.getWards().getCode()));
        userAddress.setUsers(user);
        userAddress.setCreatedAt(new Date());
        userAddress.setDeletedAt(null);
        userAddress.setName("Bao");
        System.out.println(userAddress.getId());
        System.out.println(userAddress.getAddress());
        System.out.println(userAddress.getProvinces().getName());



        if(addressService.save(userAddress)) {
            redirectAttributes.addFlashAttribute("sweetAlert", "success");
            redirectAttributes.addFlashAttribute("message", "Edit Profile Successfully");
        }else {
            redirectAttributes.addFlashAttribute("sweetAlert", "error");
            redirectAttributes.addFlashAttribute("message", "Edit Profile Failed");
        }

       

        return "redirect:/vendor/profile/edit";
    }

}

