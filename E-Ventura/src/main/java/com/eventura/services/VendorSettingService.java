package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.entities.Users;
import com.eventura.entities.VendorEarnings;
import com.eventura.entities.VendorReviews;
import com.eventura.entities.VendorSettings;
import com.eventura.entities.Vendors;
import com.eventura.repositories.VendorSettingRepository;

public interface VendorSettingService {
	
	public List<VendorSettings> findAll();

	public List<VendorSettings> findAlls();
	
	public VendorSettings findById(int id);
	
	public boolean save(VendorSettings vendorSettings);
}
