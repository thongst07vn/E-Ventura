package com.eventura.services;

import java.util.List;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;

public interface CategoryService {
	public List<ProductCategories> findAll();
	
	public ProductCategories findById(int id);

}
