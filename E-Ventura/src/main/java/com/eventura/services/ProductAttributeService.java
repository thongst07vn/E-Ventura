package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.entities.ProductAttributes;
import com.eventura.entities.ProductReviews;
import com.eventura.entities.Products;
import com.eventura.entities.Users;

public interface ProductAttributeService {

	public boolean save(ProductAttributes productAttributes);

	public boolean delete(int id);
	
	public ProductAttributes findByName(String keyword);

}
