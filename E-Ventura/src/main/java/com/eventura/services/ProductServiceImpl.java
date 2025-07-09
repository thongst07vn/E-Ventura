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
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductReviewsRepository productReviewsRepository;

	@Autowired
	private ProductAttributeRepository productAttributeRepository;

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
		return productRepository.findProductByCategoryPage(id, pageable);
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

	@Override
	public boolean save(Products products) {
		// TODO Auto-generated method stub
		try {
			productRepository.save(products);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<ProductAttributes> findProductAttributeByProductId(int id) {

		return productAttributeRepository.findDistinctByProductId(id);
	}

	@Override
	public Page<Products> findByKeywordAndVendorIdPage(String keyword, int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findByKeywordAndVendorIdPage(keyword, vendorId, pageable);
	}

	@Override
	public Page<ProductReviews> findProductReviewPage(int id, Pageable pageable) {
		// TODO Auto-generated method stub
		return productReviewsRepository.findProductReviewPage(id, pageable);
	}

	@Override
	public Page<ProductReviews> findProductReviewsByKeyword(int id, String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return productReviewsRepository.findByKeywordPage(id, keyword, pageable);
	}

	@Override
	public Page<Products> findProductByVendorAndDeletePage(int id, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findProductByVendorAndDeletePage(id, pageable);
	}

	@Override
	public Page<Products> findProductByVendorAndDeleteAndCategoryPage(int vendorId, int categoryId, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findProductByVendorAndDeleteAndCategoryPage(vendorId, categoryId, pageable);
	}

	@Override
	public Page<Products> findProductByVendorAndDeleteAndKeywordPage(int vendorId, String keyword, Pageable pageable) {
		// TODO productRepository-generated method stub
		return productRepository.findProductByVendorAndDeleteAndKeywordPage(vendorId, keyword, pageable);
	}

	@Override
	public Page<Products> findProductReviewed(int vendorId, Pageable pageable) {
		// TODO productRepository-generated method stub
		return productRepository.findProductReviewed(vendorId, pageable);
	}

	@Override
	public Page<Products> find5ProductReviewed(int vendorId) {
		// TODO Auto-generated method stub
		PageRequest limit = PageRequest.of(0, 4); // trang 0, lấy 5 dòng
		Page<Products> top5 = productRepository.findProductReviewed(vendorId, limit);

		return top5;
	}

	@Override
	public Page<Products> findByPriceRange(double min, double max, String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findByPriceRangePage(min, max, keyword, pageable);
	}

	@Override
	public List<Products> findTopViewProduct() {
		Pageable pageable = PageRequest.of(0, 10);
		return productRepository.findTopViewProduct(pageable);
	}

	@Override
	public Products findTopByPrice() {
		Pageable pageable = PageRequest.of(0, 1);
		return productRepository.findTopByPriceDesc(pageable).get(0);
	}

	@Override
	public ProductReviews findProductReviewByUserAndProductId(int productId, int userId) {
		// TODO Auto-generated method stub
		return productReviewsRepository.findProductReviewByUserAndProductId(productId, userId);
	}

	@Override
	public boolean saveProductReview(ProductReviews productReviews) {
		// TODO Auto-generated method stub
		try {
			productReviewsRepository.save(productReviews);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
