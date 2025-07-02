package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eventura.entities.ProductAttributes;
import com.eventura.entities.ProductReviews;
import com.eventura.entities.Products;
import com.eventura.repositories.ProductAttributeRepository;
import com.eventura.repositories.ProductRepository;
import com.eventura.repositories.ProductReviewsRepository;

@Service
public class ProductAttributeServiceImpl implements ProductAttributeService {
	@Autowired
	private ProductAttributeRepository productAttributeRepository;

	@Override
	public boolean save(ProductAttributes productAttributes) {
		// TODO Auto-generated method stub
		try {
			productAttributeRepository.save(productAttributes);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		try {
			productAttributeRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ProductAttributes findByName(String keyword) {
		// TODO Auto-generated method stub
		return productAttributeRepository.findByName(keyword);
	}

	

}
