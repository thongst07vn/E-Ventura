package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Override
	public Page<ProductCategories> findAlls(Pageable pageable) {
		// TODO Auto-generated method stub
		return categoryRepository.findAll(pageable);
	}
	@Override
	public ProductCategories findById(int id) {
		// TODO Auto-generated method stub
		return categoryRepository.findById(id).get();
	}

	@Override
	public Page<ProductCategories> findByKeywordPage(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return categoryRepository.findByKeywordPage(keyword, pageable);
	}

	@Override
	public boolean save(ProductCategories productCategories) {
		// TODO Auto-generated method stub
		try {
			categoryRepository.save(productCategories);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


}
