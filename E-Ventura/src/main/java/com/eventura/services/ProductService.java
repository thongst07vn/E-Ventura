package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.entities.Products;

public interface ProductService {
	public List<Products> findAll();

	public List<Products> findByCategoryId(int id);

	public List<Products> findByVendorId(int id);

	public Products findById(int id);

	public List<Products> findTopNewProduct();

	public List<Products> findByKeyword(String keyword);

	public Page<Products> findAlls(Pageable pageable);

	public Page<Products> findByCategoryIdPage(int id,Pageable pageable);

	public Page<Products> findByVendorIdPage(int id,Pageable pageable);
}
