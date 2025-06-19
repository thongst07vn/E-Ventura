package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eventura.entities.Products;
import com.eventura.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

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
		return productRepository.findProductByCategory(id, Sort.by("createdAt").descending());
	}

	@Override
	public List<Products> findByVendorId(int id) {
		// TODO Auto-generated method stub
		return productRepository.findProductByVendor(id);
	}

	@Override
	public Products findById(int id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id).get();
  }
  
  @Override
	public List<Products> findTopNewProduct() {
		Pageable topten = PageRequest.of(0, 10, Sort.by("createdAt"));
		return productRepository.findTopNewProduct(topten);
	}

	@Override
	public List<Products> findByKeyword(String keyword) {
		return productRepository.findByKeyword(keyword, Sort.by("createdAt").descending());
	}

}
