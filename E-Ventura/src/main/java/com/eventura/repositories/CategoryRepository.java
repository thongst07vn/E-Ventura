package com.eventura.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.Vendors;

@Repository
public interface CategoryRepository extends JpaRepository<ProductCategories, Integer> {
	@Query("from ProductCategories where deletedAt IS NULL AND name like %:keyword%")
	public Page<ProductCategories>  findByKeywordPage(@Param("keyword") String keyword,Pageable pageable);
	
	@Query("from ProductCategories where deletedAt IS NULL")
	public List<ProductCategories>  findAllCategory();
	
	
}