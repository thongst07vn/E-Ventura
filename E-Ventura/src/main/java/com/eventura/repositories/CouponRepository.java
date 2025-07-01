package com.eventura.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Coupons;
import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.Wards;

@Repository
public interface CouponRepository extends JpaRepository<Coupons, Integer> {
	@Query("from Coupons where redeemAllowed = true and deletedAt is null and startTime <= current_timestamp() and endTime >= current_timestamp()")
	public List<Coupons> findAllCoupons();
	@Query("from Coupons where redeemAllowed = true and deletedAt is null and startTime <= current_timestamp() and endTime >= current_timestamp() and vendors.id = :id")
	public List<Coupons> findAllCouponsByVendorId(@Param("id") int id);
}