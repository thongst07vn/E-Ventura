package com.eventura.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {
	@Query("from Products where deletedAt IS NULL AND productCategories.id = :category_id")
	public List<Products> findProductByCategory(@Param("category_id") int category_id, Sort sort);
	
	@Query("from Products where deletedAt IS NULL AND vendors.id = :vendor_id")
	public List<Products>  findProductByVendor(@Param("vendor_id") int vendor_id);
	
	@Query("Select p from Products p WHERE deletedAt IS NULL order by p.createdAt")
	public List<Products>  findTopNewProduct(Pageable pageable);
	
	@Query("from Products where deletedAt IS NULL AND name like %:keyword%")
	public List<Products>  findByKeyword(@Param("keyword") String keyword,Sort sort);
	
	@Query("from Products where deletedAt IS NULL AND name like %:keyword%")
	public Page<Products>  findByKeywordPage(@Param("keyword") String keyword,Pageable pageable);
	
	@Query("FROM Products p JOIN Vendors v ON p.vendors.id = v.id WHERE v.users.deletedAt IS NULL AND p.deletedAt IS NULL AND p.productCategories.id = :category_id")
	public Page<Products> findProductByCategoryPage(@Param("category_id") int category_id,Pageable pageable);
	
	@Query("from Products where vendors.id = :vendor_id")
	public Page<Products>  findProductByVendorPage(@Param("vendor_id") int vendor_id,Pageable pageable);

	@Query("FROM Products p JOIN Vendors v ON p.vendors.id = v.id WHERE v.users.deletedAt IS NULL")
	public Page<Products>  findAllProduct(Pageable pageable);
	
	@Query("FROM Products p WHERE p.vendors.id = :vendor_id AND p.productCategories.id = :category_id")
	public Page<Products> findProductsByVendorCategoryPage(@Param("vendor_id") int vendor_id, @Param("category_id") int category_id, Pageable pageable);

	@Query("from Products where name like %:keyword% and vendors.id = :vendorId")
	public Page<Products>  findByKeywordAndVendorIdPage(@Param("keyword") String keyword, @Param("vendorId") int vendorId, Pageable pageable);
}
