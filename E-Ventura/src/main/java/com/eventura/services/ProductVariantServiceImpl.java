package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.eventura.entities.ProductVariants;
import com.eventura.entities.Products;
import com.eventura.repositories.ProductVariantRepository;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {
	
	@Autowired
	private ProductVariantRepository productVariantRepository;

	@Override
	public List<ProductVariants> findByProductId(int productId) {
		// TODO Auto-generated method stub
		return productVariantRepository.findByProductId(productId);
	}

	@Override
	public ProductVariants findById(int id) {
		// TODO Auto-generated method stub
		return productVariantRepository.findById(id).get();
	}
	
}
