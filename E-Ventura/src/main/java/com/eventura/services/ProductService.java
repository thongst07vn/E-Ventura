package com.eventura.services;

import java.util.List;

import com.eventura.entities.Products;

public interface ProductService {
	public List<Products> findAll();
	
	public List<Products> findByCategoryId(int id);	
	
	public List<Products> findByVendorId(int id);
	
	
	public Products findById(int id);
}
