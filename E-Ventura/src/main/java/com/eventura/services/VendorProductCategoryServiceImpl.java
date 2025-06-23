package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Override
	public Page<VendorProductCategory> findByVendorIdPage(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return vendorProductCategoryRepository.findByVendorIdPage(vendorId, pageable);
	}

	@Override
	public Page<VendorProductCategory> findByKeyword(int vendorId, String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return vendorProductCategoryRepository.findByKeyword(vendorId, keyword, pageable);
	}

}
