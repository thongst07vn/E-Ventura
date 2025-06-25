package com.eventura.services;

import java.util.List;

import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Wards;

public interface AddressService {
	
	public List<Provinces> findAllProvinces();
	public List<Districts> findDistrictsByProvinceCode(String provinceCode);
	public List<Wards> findWardsByDistrictCode(String districtCode);
	
	public boolean save(UserAddress userAddress);
	public UserAddress findById(int id);
	
	public Provinces findProvinceById(String id);
	public Districts findDistrictById(String id);
	public Wards findWardById(String id);
	
}
