package com.eventura.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.ProductAttributes;
import com.eventura.entities.ProductVariants;
import com.eventura.entities.Products;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttributes, Integer> {
	
	@Query("SELECT DISTINCT pa " +
	           "FROM Products p " +
	           "JOIN p.productVariantses pv " +
	           "JOIN pv.productAttributes pa " +
	           "WHERE p.id = :productId")
	    List<ProductAttributes> findDistinctByProductId(@Param("productId") Integer productId);
	
	@Query("FROM ProductAttributes WHERE LOWER(name) = LOWER(:keyword)")
	ProductAttributes findByName(@Param("keyword") String keyword);


}
