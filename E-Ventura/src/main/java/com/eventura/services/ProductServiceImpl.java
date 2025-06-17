package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventura.entities.Products;
import com.eventura.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;
	@Override
	public List<Products> findAll() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	@Override
	public List<Products> findByCategoryId(int id) {
		// TODO Auto-generated method stub
		return productRepository.findProductByCategory(id);
	}

	@Override
	public List<Products> findByVendorId(int id) {
		// TODO Auto-generated method stub
		return productRepository.findProductByVendor(id);
	}

}
