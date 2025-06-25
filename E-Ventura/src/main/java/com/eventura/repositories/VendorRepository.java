package com.eventura.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Products;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;

@Repository
public interface VendorRepository extends JpaRepository<Vendors, Integer> {
	@Query("from Vendors where name like %:keyword%")
	public Page<Vendors>  findByKeywordPage(@Param("keyword") String keyword,Pageable pageable);
}
