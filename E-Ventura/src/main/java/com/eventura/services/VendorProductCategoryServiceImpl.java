package com.eventura.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventura.entities.Users;
import com.eventura.entities.VendorProductCategory;
import com.eventura.entities.Vendors;
import com.eventura.repositories.ProductRepository;
import com.eventura.repositories.VendorProductCategoryRepository;
import com.eventura.repositories.VendorRepository;

@Service
public class VendorProductCategoryServiceImpl implements VendorProductCategoryService {

	
	@Autowired
	private VendorProductCategoryRepository vendorProductCategoryRepository;
	@Autowired
	private VendorRepository vendorRepository;


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
	
	@Override
	public boolean save(int vendorId, VendorProductCategory vendorProductCategory) {
		try {
			Optional<Vendors> vendorOpt = vendorRepository.findById(vendorId);
			if (vendorOpt.isEmpty()) return false;
			
			String categoryName = vendorProductCategory.getProductCategories().getName().trim();

			// Check trùng theo tên
			if (vendorProductCategoryRepository.existsByVendorIdAndCategoryNameIgnoreCase(vendorId, categoryName)) {
				throw new RuntimeException("Danh mục này đã tồn tại cho Vendor.");
			}

			vendorProductCategory.setVendors(vendorOpt.get());
			vendorProductCategoryRepository.save(vendorProductCategory);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
