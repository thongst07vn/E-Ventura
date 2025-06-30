package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.entities.ProductVariants;
import com.eventura.entities.Products;

public interface ProductVariantService {
	public List<ProductVariants> findByProductId(int productId);
	
	public ProductVariants findById(int id);
	
	public boolean saveProductVariants(ProductVariants productVariants);

}
