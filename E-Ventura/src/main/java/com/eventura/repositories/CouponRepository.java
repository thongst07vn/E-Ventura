package com.eventura.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Coupons;
import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.Users;
import com.eventura.entities.Wards;

@Repository
public interface CouponRepository extends JpaRepository<Coupons, Integer> {
	@Query("from Coupons where redeemAllowed = true and deletedAt is null and startTime <= current_timestamp() and endTime >= current_timestamp()")
	public List<Coupons> findAllCoupons();

	@Query("from Coupons where redeemAllowed = true and deletedAt is null and startTime <= current_timestamp() and endTime >= current_timestamp() and vendors.id = :id")
	public List<Coupons> findAllCouponsByVendorId(@Param("id") int id);

	@Query("FROM Coupons WHERE deletedAt IS NULL ORDER BY createdAt DESC")
	Page<Coupons> findAllByDeletedAtISNUL(Pageable pageable);

	@Query("FROM Coupons WHERE deletedAt IS  NULL ORDER BY createdAt DESC")
	Page<Coupons> findAllByDeletedAtISNOTNUL(Pageable pageable);
	
	@Query("FROM Coupons WHERE deletedAt IS NULL and endTime <= current_timestamp()")
	public Page<Coupons> findAllCouponExpired(Pageable pageable);
	
	@Query("FROM Coupons WHERE deletedAt IS NULL and startTime >= current_timestamp()")
	public Page<Coupons> findAllCouponInvalid(Pageable pageable);
	
	@Query("FROM Coupons WHERE deletedAt IS NULL and startTime <= current_timestamp() and endTime >= current_timestamp()")
	public Page<Coupons> findAllCouponValid(Pageable pageable);

	@Query("FROM Coupons WHERE deletedAt IS NULL and vendors.id = :id")
	public Page<Coupons> findByVendorId(@Param("id") int  id,Pageable pageable);
	
	@Query("FROM Coupons WHERE deletedAt IS NULL and products.name like %:keyword%")
	Page<Coupons> findByKeyword(@Param("keyword") String keyword,Pageable pageable);
	
	@Query("FROM Coupons WHERE deletedAt IS NULL and endTime <= current_timestamp() and vendors.id = :vendor_id")
	public Page<Coupons> findAllCouponExpiredByVendorId(@Param("vendor_id") int  vendor_id, Pageable pageable);
	
	@Query("FROM Coupons WHERE deletedAt IS NULL and startTime >= current_timestamp() and vendors.id = :vendor_id")
	public Page<Coupons> findAllCouponInvalidByVendorId(@Param("vendor_id") int  vendor_id, Pageable pageable);
	
	@Query("FROM Coupons WHERE deletedAt IS NULL and startTime <= current_timestamp() and endTime >= current_timestamp() and vendors.id = :vendor_id")
	public Page<Coupons> findAllCouponValidByVendorId(@Param("vendor_id") int  vendor_id, Pageable pageable);
	
	@Query("FROM Coupons WHERE deletedAt IS NULL and redeemAllowed = true and vendors.id = :vendor_id")
	public Page<Coupons> findAllCouponsEnableByVendorId(@Param("vendor_id") int  vendor_id, Pageable pageable);
	
	@Query("FROM Coupons WHERE deletedAt IS NULL and redeemAllowed = false and vendors.id = :vendor_id")
	public Page<Coupons> findAllCouponsDisableByVendorId(@Param("vendor_id") int  vendor_id, Pageable pageable);

}