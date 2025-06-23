package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eventura.entities.ProductReviews;
import com.eventura.entities.Products;
import com.eventura.repositories.ProductRepository;
import com.eventura.repositories.ProductReviewsRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductReviewsRepository productReviewsRepository;

	@Override
	public List<Products> findAll() {
		// TODO Auto-generated method stub
		return productRepository.findAll(Sort.by("createdAt").descending());
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

	@Override
	public Page<Products> findAlls(Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findAllProduct(pageable);
	}

	@Override
	public Page<Products> findByCategoryIdPage(int id, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findProductByCategoryPage(id,pageable);
	}

	@Override
	public Page<Products> findByVendorIdPage(int id, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findProductByVendorPage(id, pageable);
	}

	@Override
	public List<ProductReviews> findProductReview(int id) {
		// TODO Auto-generated method stub
		return productReviewsRepository.findProductReview(id);
	}

	@Override
	public int countProductReview(int id) {
		// TODO Auto-generated method stub
		return productReviewsRepository.countProductReview(id);
	}

	@Override
	public double avgProductReview(int id) {
		// TODO Auto-generated method stub
		return productReviewsRepository.avgProductReview(id);
	}
	@Override
	public Page<Products> findByVendorCategoryPage(int vendorId, int categoryId, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findProductsByVendorCategoryPage(vendorId, categoryId, pageable);

	}

	@Override
	public Page<Products> findByKeywordPage(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findByKeywordPage(keyword, pageable);
	}

}
