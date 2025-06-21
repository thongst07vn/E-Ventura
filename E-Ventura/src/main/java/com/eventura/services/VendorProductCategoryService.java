package com.eventura.services;

import java.util.List;

import com.eventura.entities.VendorProductCategory;

public interface VendorProductCategoryService {
	public List<VendorProductCategory> findByVendorId(int vendorId);
	
}
