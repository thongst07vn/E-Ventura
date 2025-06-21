package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventura.entities.VendorProductCategory;
import com.eventura.repositories.VendorProductCategoryRepository;

@Service
public class VendorProductCategoryServiceImpl implements VendorProductCategoryService {
	
	@Autowired
	private VendorProductCategoryRepository vendorProductCategoryRepository;

	@Override
	public List<VendorProductCategory> findByVendorId(int vendorId) {
		// TODO Auto-generated method stub
		return vendorProductCategoryRepository.findByVendorId(vendorId);
	}

}
