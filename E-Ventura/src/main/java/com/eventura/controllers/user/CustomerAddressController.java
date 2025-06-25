package com.eventura.controllers.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eventura.dtos.DistrictsDTO;
import com.eventura.dtos.UserAddressDTO;
import com.eventura.dtos.WardsDTO;
import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.Wards;
import com.eventura.services.AddressService;
import com.eventura.services.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/customer/api/address")
public class CustomerAddressController {

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
    
	@GetMapping({"geteditaddress"})
	public UserAddressDTO GetEditAddress(@RequestParam("editAddressId") int editAddressId) {
		UserAddress userAddress = addressService.findById(editAddressId);
		UserAddressDTO userAddressDTO = new UserAddressDTO(userAddress);
		System.out.println(userAddressDTO.getAddress());
		System.out.println(userAddressDTO.getDistricts());
		System.out.println(userAddressDTO.getDistrictsCode());
		System.out.println(userAddressDTO.getDistrictsUnit());
		System.out.println(userAddressDTO.getProvinces());
		System.out.println(userAddressDTO.getProvincesCode());
		System.out.println(userAddressDTO.getProvincesUnit());
		System.out.println(userAddressDTO.getWards());
		System.out.println(userAddressDTO.getWardsCode());
		System.out.println(userAddressDTO.getWardsUnit());
		return userAddressDTO; 		    	
	}

}

