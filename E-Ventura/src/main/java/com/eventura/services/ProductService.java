package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.entities.ProductAttributes;
import com.eventura.entities.ProductReviews;
import com.eventura.entities.Products;
import com.eventura.entities.Users;

public interface ProductService {
	public List<Products> findAll();

	public List<Products> findByCategoryId(int id);

	public List<Products> findByVendorId(int id);

	public Products findById(int id);

	public List<Products> findTopNewProduct();

	public List<Products> findByKeyword(String keyword);

	public Page<Products> findByKeywordPage(String keyword,Pageable pageable);

	public Page<Products> findAlls(Pageable pageable);

	public Page<Products> findByCategoryIdPage(int id,Pageable pageable);

	public Page<Products> findByVendorIdPage(int id,Pageable pageable);


	public List<ProductReviews> findProductReview(int id);
	
	public int countProductReview(int id);

	public double avgProductReview(int id);

	public Page<Products> findByVendorCategoryPage(int vendorId, int categoryId, Pageable pageable);

	public boolean save(Products products);

	
	public List<ProductAttributes> findProductAttributeByProductId(int id);
	
	public Page<Products> findByKeywordAndVendorIdPage(String keyword, int vendorId,Pageable pageable);
	
	public Page<ProductReviews> findProductReviewPage(int id, Pageable pageable);
	
	public Page<ProductReviews> findProductReviewsByKeyword(int id, String keyword, Pageable pageable);
	
	public Page<Products> findProductByVendorAndDeletePage(int id, Pageable pageable);
	
	public Page<Products> findProductByVendorAndDeleteAndCategoryPage(int vendorId, int categoryId ,Pageable pageable);

	public Page<Products> findProductByVendorAndDeleteAndKeywordPage(int vendorId, String keyword ,Pageable pageable);

 


}
