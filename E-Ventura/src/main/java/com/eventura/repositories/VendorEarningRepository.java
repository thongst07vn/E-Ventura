package com.eventura.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Products;
import com.eventura.entities.Users;
import com.eventura.entities.VendorEarnings;
import com.eventura.entities.Vendors;

@Repository
public interface VendorEarningRepository extends JpaRepository<VendorEarnings, Integer> {
	@Query("select count(v) from VendorEarnings v where v.vendors.id = :id")
	public int countByVendorId(@Param("id") int id);
	@Query("select sum(v.amount) from VendorEarnings v where v.vendors.id = :id")
	public int sumByVendorId(@Param("id") int id);
}
