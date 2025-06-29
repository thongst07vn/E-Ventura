package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.entities.Users;
import com.eventura.entities.VendorProductCategory;

public interface VendorProductCategoryService {
	public List<VendorProductCategory> findByVendorId(int vendorId);
	
	public Page<VendorProductCategory> findByVendorIdPage(int vendorId, Pageable pageable);
	
	public Page<VendorProductCategory> findByKeyword(int vendorId, String keyword, Pageable pageable);
	
	public boolean save(int vendorId, VendorProductCategory vendorProductCategory);
	
}
