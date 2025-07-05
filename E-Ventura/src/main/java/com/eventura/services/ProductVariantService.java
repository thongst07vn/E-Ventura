package com.eventura.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.entities.ProductVariants;
import com.eventura.entities.Products;

public interface ProductVariantService {
	public List<ProductVariants> findByProductId(int productId);
	
	public ProductVariants findById(int id);
	
	public ProductVariants findByProductIdAndProductAttributeAndValue(int productId, int productAttributeId, String value);
	
	public ProductVariants findByProductIdAndValue(int productId, String value);

	
	public boolean saveProductVariants(ProductVariants productVariants);
	
	public boolean delete(int id);
	
	/* API */
	 public Optional<ProductVariants> findById(Integer id);
	 
	 public long getTotalVariantQuantityByProductId(int productId);
	
	


}
