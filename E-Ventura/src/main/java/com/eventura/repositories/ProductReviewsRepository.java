package com.eventura.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.eventura.entities.ProductReviews;
import com.eventura.entities.Products;
import com.eventura.entities.VendorReviews;

@Repository
public interface ProductReviewsRepository extends JpaRepository<ProductReviews, Integer> {
	@Query("FROM ProductReviews WHERE products.id = :id")
	public List<ProductReviews>  findProductReview(@Param("id") int id);
	
	@Query("FROM ProductReviews WHERE products.id = :productId AND users.id = :userId")
	public ProductReviews  findProductReviewByUserAndProductId(@Param("productId") int productId,@Param("userId") int userId);
	
	@Query("select count(pr) from ProductReviews pr where pr.products.id = :id")
	public int countProductReview(@Param("id") int id);
	
	@Query("select avg(pr.rating) from ProductReviews pr where pr.products.id = :id")
	public double avgProductReview(@Param("id") int id);
	
	@Query("FROM ProductReviews WHERE products.id = :id")
	public Page<ProductReviews>  findProductReviewPage(@Param("id") int id, Pageable pageable);
	
	@Query("FROM ProductReviews WHERE users.username like %:keyword%")
	public Page<ProductReviews>  findByKeywordPage(@Param("id") int id, @Param("keyword") String keyword, Pageable pageable);	
	
	
	
}
