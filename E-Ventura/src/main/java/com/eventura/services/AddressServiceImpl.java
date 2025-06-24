package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Wards;
import com.eventura.repositories.DistrictRepository;
import com.eventura.repositories.ProvinceRepository;
import com.eventura.repositories.UserAddressRepository;
import com.eventura.repositories.WardRepository;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	private ProvinceRepository provinceRepository;
	@Autowired
	private DistrictRepository districtRepository;
	@Autowired
	private WardRepository wardRepository;
	@Autowired
	private UserAddressRepository userAddressRepository;

	@Override
	public List<Provinces> findAllProvinces() {
		// TODO Auto-generated method stub
		return provinceRepository.findAll();
	}

	@Override
	public List<Districts> findDistrictsByProvinceCode(String provinceCode) {
		// TODO Auto-generated method stub
		return districtRepository.findDistrictsByProvinceCode(provinceCode);
	}

	@Override
	public List<Wards> findWardsByDistrictCode(String districtCode) {
		// TODO Auto-generated method stub
		return wardRepository.findWardsByDistrictCode(districtCode);
	}

	@Override
	public boolean save(UserAddress userAddress) {
		try {
			userAddressRepository.save(userAddress);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
