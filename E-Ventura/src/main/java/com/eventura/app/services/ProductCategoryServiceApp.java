package com.eventura.app.services;

import java.util.List;

import com.eventura.app.dto.ProductCategoryDto;
import com.eventura.app.dto.ProductDto;

public interface ProductCategoryServiceApp {
	public List<ProductCategoryDto> findAll();
	
	public ProductCategoryDto findById(int id);
}
