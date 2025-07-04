package com.eventura.services;

import java.util.List;
import java.util.Optional;

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
	
	@Override
	public boolean saveProductVariants(ProductVariants productVariants) {
		try {
			System.out.println("service Id: " + productVariants.getId());

			productVariantRepository.save(productVariants);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(int id) {
		try {
			productVariantRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ProductVariants findByProductIdAndProductAttributeAndValue(int productId, int productAttributeId,
			String value) {
		// TODO Auto-generated method stub
		return productVariantRepository.findByProductIdAndProductAttributeAndValue(productId, productAttributeId, value);
	}

	@Override
	public Optional<ProductVariants> findById(Integer id) {
		// TODO Auto-generated method stub
		return productVariantRepository.findById(id);
	}

	@Override
	public ProductVariants findByProductIdAndValue(int productId, String value) {
		// TODO Auto-generated method stub
		return productVariantRepository.findByProductIdAndValue(productId, value);
	}
	
}
