package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public List<ProductCategories> findAll() {
		return categoryRepository.findAll();
	}


}
