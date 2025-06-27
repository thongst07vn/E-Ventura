package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.Vendors;

public interface CategoryService {
	public List<ProductCategories> findAll();
	
	public Page<ProductCategories> findAlls(Pageable pageable);

	public ProductCategories findById(int id);
	
	public Page<ProductCategories> findByKeywordPage(String keyword, Pageable pageable);

	public boolean save(ProductCategories productCategories);
}
