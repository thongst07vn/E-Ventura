package com.eventura.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {
	@Query("from Products where productCategories.id = :category_id")
	public List<Products> findProductByCategory(@Param("category_id") int category_id);
	@Query("from Products where vendors.id = :vendor_id")
	public List<Products>  findProductByVendor(@Param("vendor_id") int vendor_id);

}
