package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.eventura.entities.VendorSettings;
import com.eventura.repositories.VendorSettingRepository;

@Service
public class VendorSettingServiceImpl implements VendorSettingService {
	
	@Autowired
	private VendorSettingRepository vendorSettingRepository;

	@Override
	public List<VendorSettings> findAll() {
		// TODO Auto-generated method stub
		return vendorSettingRepository.findAllDeleteAt();
	}

	@Override
	public VendorSettings findById(int id) {
		// TODO Auto-generated method stub
		return vendorSettingRepository.findById(id).get();
	}

	@Override
	public boolean save(VendorSettings vendorSettings) {
		// TODO Auto-generated method stub
		try {
			vendorSettingRepository.save(vendorSettings);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


}
