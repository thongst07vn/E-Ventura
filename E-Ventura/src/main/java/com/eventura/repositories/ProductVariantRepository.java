package com.eventura.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.ProductVariants;
import com.eventura.entities.Products;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariants, Integer> {
	@Query("from ProductVariants where products.id = :productId")
	public List<ProductVariants>  findByProductId(@Param("productId") int productId);
}
