package com.eventura.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.UserAddress;
import com.eventura.entities.VendorProductCategory;

@Repository
public interface VendorProductCategoryRepository extends JpaRepository<VendorProductCategory, Integer> {
	@Query("from VendorProductCategory where vendors.id = :vendor_id")
	public List<VendorProductCategory> findByVendorId(@Param("vendor_id") int vendor_id);
}