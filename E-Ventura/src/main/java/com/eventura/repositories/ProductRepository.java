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
	
		@Query("SELECT p FROM Products p LEFT JOIN p.productReviewses pr JOIN p.vendors v WHERE v.users.deletedAt IS NULL AND p.deletedAt IS NULL GROUP BY p ORDER BY AVG(pr.rating) DESC, COUNT(pr.comment) DESC")
	public List<Products> findTopViewProduct(Pageable pageable);
	
	@Query("SELECT p FROM Products p ORDER BY p.price DESC")
    public List<Products> findTopByPriceDesc(Pageable pageable);
	
	@Query("from Products p JOIN Vendors v ON p.vendors.id = v.id WHERE v.users.deletedAt IS NULL And p.deletedAt IS NULL AND p.name like %:keyword% ")
	public List<Products>  findByKeyword(@Param("keyword") String keyword,Sort sort);
	
	@Query("from Products p JOIN Vendors v ON p.vendors.id = v.id WHERE v.users.deletedAt IS NULL And p.deletedAt IS NULL AND p.name like %:keyword% ")
	public Page<Products>  findByKeywordPage(@Param("keyword") String keyword,Pageable pageable);
	
	@Query("from Products where deletedAt IS NULL AND price >= :min AND price <= :max AND name like %:keyword%")
	public Page<Products>  findByPriceRangePage(@Param("min") double min, @Param("max") double max,@Param("keyword") String keyword,Pageable pageable);
	
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
	
	@Query("from Products p where p.vendors.id = :vendor_id and p.deletedAt is null and p.deleted = false")
	public Page<Products> findProductByVendorAndDeletePage(@Param("vendor_id") int vendor_id, Pageable pageable);
	
	@Query("from Products p where p.vendors.id = :vendor_id and p.deletedAt is null and p.deleted = false and p.productCategories.id = :category_id")
	public Page<Products> findProductByVendorAndDeleteAndCategoryPage(@Param("vendor_id") int vendor_id, @Param("category_id") int category_id, Pageable pageable);
	
	@Query("from Products p where p.vendors.id = :vendor_id and p.deletedAt is null and p.deleted = false and p.name like %:keyword%")
	public Page<Products> findProductByVendorAndDeleteAndKeywordPage(@Param("vendor_id") int vendor_id, @Param("keyword") String keyword, Pageable pageable);
	

	@Query("Select p from Products p RIGHT JOIN p.productReviewses pr ON p.id = pr.products.id where p.vendors.id = :vendor_id and p.deleted is false ORDER BY pr.createdAt DESC")
	public Page<Products> findProductReviewed(@Param("vendor_id") int vendor_id, Pageable pageable);
	

	@Query("from Products p where p.vendors.id = :vendor_id and p.deleted = false")
	public Page<Products> findProductByVendorAndDeletePage1(@Param("vendor_id") int vendor_id, Pageable pageable);
	
	@Query("from Products p where p.vendors.id = :vendor_id and p.deleted = false and p.name like %:keyword%")
	public Page<Products> findProductByVendorAndDeleteAndKeywordPage1(@Param("vendor_id") int vendor_id, @Param("keyword") String keyword, Pageable pageable);
	
	@Query("from Products p where p.vendors.id = :vendor_id and p.deleted = false and p.productCategories.id = :category_id")
	public Page<Products> findProductByVendorAndDeleteAndCategoryPage1(@Param("vendor_id") int vendor_id, @Param("category_id") int category_id, Pageable pageable);
	
}
