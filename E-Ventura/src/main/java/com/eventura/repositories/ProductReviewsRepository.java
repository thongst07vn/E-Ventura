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

@Repository
public interface ProductReviewsRepository extends JpaRepository<ProductReviews, Integer> {
	@Query("FROM ProductReviews WHERE products.id = :id")
	public List<ProductReviews>  findProductReview(@Param("id") int id);
	
	@Query("select count(pr) from ProductReviews pr where pr.products.id = :id")
	public int countProductReview(@Param("id") int id);
	
	@Query("select avg(pr.rating) from ProductReviews pr where pr.products.id = :id")
	public double avgProductReview(@Param("id") int id);
	
	@Query("FROM ProductReviews WHERE products.id = :id")
	public Page<ProductReviews>  findProductReviewPage(@Param("id") int id, Pageable pageable);
	
}
