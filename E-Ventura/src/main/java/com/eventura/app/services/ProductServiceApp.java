package com.eventura.app.services;

import java.util.List;

import com.eventura.app.dto.ProductDto;

public interface ProductServiceApp {
	public List<ProductDto> findAll();
	public List<ProductDto> findByKeyword(String keyword);
	
	public ProductDto findById(int id);
}
