package com.eventura.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Vouchers;
import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.Vouchers;
import com.eventura.entities.Wards;


@Repository
public interface VoucherRepository extends JpaRepository<Vouchers, Integer> {
	@Query("from Vouchers where redeemAllowed = true and deletedAt is null and quantity > 0 and startTime <= current_timestamp() and endTime >= current_timestamp() and vendors.id = :id and minOrderValue <= :value")
	public List<Vouchers> findAllVouchersByVendorId(@Param("id") int id,@Param("value") double value);
	@Query("from Vouchers where redeemAllowed = true and deletedAt is null and quantity > 0 and startTime <= current_timestamp() and endTime >= current_timestamp() and vendors.id IS NULL and minOrderValue <= :value")
	public List<Vouchers> findAllVouchers(@Param("value") double value);
	
	@Query("FROM Vouchers WHERE deletedAt IS NULL ORDER BY createdAt DESC")
	Page<Vouchers> findAllByDeletedAtISNUL(Pageable pageable);

	@Query("FROM Vouchers WHERE deletedAt IS  NULL ORDER BY createdAt DESC")
	Page<Vouchers> findAllByDeletedAtISNOTNUL(Pageable pageable);
	
	@Query("FROM Vouchers WHERE deletedAt IS NULL and endTime <= current_timestamp()")
	public Page<Vouchers> findAllVoucherExpired(Pageable pageable);
	
	@Query("FROM Vouchers WHERE deletedAt IS NULL and startTime >= current_timestamp()")
	public Page<Vouchers> findAllVoucherInvalid(Pageable pageable);
	
	@Query("FROM Vouchers WHERE deletedAt IS NULL and startTime <= current_timestamp() and endTime >= current_timestamp()")
	public Page<Vouchers> findAllVoucherValid(Pageable pageable);

	@Query("FROM Vouchers WHERE deletedAt IS NULL and vendors.id = :id")
	public Page<Vouchers> findByVendorId(@Param("id") int  id,Pageable pageable);
	
	@Query("FROM Vouchers WHERE deletedAt IS NULL and vendors IS NULL")
	public Page<Vouchers> findByVendorIdISNULL(Pageable pageable);
	
	@Query("FROM Vouchers WHERE deletedAt IS NULL and endTime <= current_timestamp() and vendors.id = :vendor_id")
	public Page<Vouchers> findAllVoucherExpiredByVendorId(@Param("vendor_id") int  vendor_id, Pageable pageable);
	
	@Query("FROM Vouchers WHERE deletedAt IS NULL and startTime >= current_timestamp() and vendors.id = :vendor_id")
	public Page<Vouchers> findAllVoucherInvalidByVendorId(@Param("vendor_id") int  vendor_id, Pageable pageable);
	
	@Query("FROM Vouchers WHERE deletedAt IS NULL and startTime <= current_timestamp() and endTime >= current_timestamp() and vendors.id = :vendor_id")
	public Page<Vouchers> findAllVoucherValidByVendorId(@Param("vendor_id") int  vendor_id, Pageable pageable);
	
	@Query("FROM Vouchers WHERE deletedAt IS NULL and redeemAllowed = true and vendors.id = :vendor_id")
	public Page<Vouchers> findAllVoucherEnableByVendorId(@Param("vendor_id") int  vendor_id, Pageable pageable);
	
	@Query("FROM Vouchers WHERE deletedAt IS NULL and redeemAllowed = false and vendors.id = :vendor_id")
	public Page<Vouchers> findAllVoucherDisableByVendorId(@Param("vendor_id") int  vendor_id, Pageable pageable);
	
}