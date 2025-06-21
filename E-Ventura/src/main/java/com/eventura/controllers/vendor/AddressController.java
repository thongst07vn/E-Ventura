package com.eventura.controllers.vendor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventura.entities.Districts;
import com.eventura.entities.DistrictsDTO;
import com.eventura.entities.Provinces;
import com.eventura.entities.Users;
import com.eventura.entities.Wards;
import com.eventura.entities.WardsDTO;
import com.eventura.services.AddressService;
import com.eventura.services.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;
    
    @GetMapping("/provinces")
    public List<Provinces> getProvinces() {
        return addressService.findAllProvinces();
    }


    @GetMapping("/districts")
    public List<DistrictsDTO> getDistricts(@RequestParam("provinceCode") String provinceCode) {
        return addressService.findDistrictsByProvinceCode(provinceCode)
                             .stream()
                             .map(DistrictsDTO::new)
                             .collect(Collectors.toList());
    }


    @GetMapping("/wards")
    public List<WardsDTO> getWards(@RequestParam("districtCode") String districtCode) {
        return addressService.findWardsByDistrictCode(districtCode)
                             .stream()
                             .map(WardsDTO::new)
                             .toList();
    }

}

