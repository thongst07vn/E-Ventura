package com.eventura.controllers.vendor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.services.AddressService;
import com.eventura.services.UserService;

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
        modelMap.put("user", user);

        modelMap.put("provinces", addressService.findAllProvinces());

        List<UserAddress> userAddressList = new ArrayList<>(user.getUserAddresses());

        if (!userAddressList.isEmpty()) {
            String provinceCode = userAddressList.get(0).getProvinces().getCode();
            String districtCode = userAddressList.get(0).getDistricts().getCode();

            modelMap.put("districts", addressService.findDistrictsByProvinceCode(provinceCode));
            modelMap.put("wards", addressService.findWardsByDistrictCode(districtCode));
        } else {
            modelMap.put("districts", List.of());
            modelMap.put("wards", List.of());
        }


        return "vendor/pages/profile/edit";
    }
}

