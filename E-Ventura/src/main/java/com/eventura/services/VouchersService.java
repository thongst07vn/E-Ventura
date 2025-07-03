package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.eventura.entities.Vouchers;


import com.eventura.entities.Coupons;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;

public interface VouchersService{
	
	public Page<Vouchers> findAll(Pageable pageable);
	
	public List<Vouchers> findAllVoucherByVendorId(int id, double value);
	public List<Vouchers> findAllVoucherByEventura(double value);
	
	public Page<Vouchers> findAllByDeletedAtISNUL(Pageable pageable);
	public Page<Vouchers> findAllByDeletedAtISNOTNUL(Pageable pageable);
	public Page<Vouchers> findAllVoucherExpired(Pageable pageable);
	public Page<Vouchers> findAllVoucherInValid(Pageable pageable);
	public Page<Vouchers> findAllVoucherValid(Pageable pageable);
	public Page<Vouchers> findByVendorId(int id,Pageable pageable);
	public Page<Vouchers> findByVendorIdISNULL(Pageable pageable);
	public Vouchers findById(int id);
	public boolean save(Vouchers vouchers);
	
	public Page<Vouchers> findAllVoucherExpiredByVendorId(int vendorId, Pageable pageable);
	public Page<Vouchers> findAllVoucherInValidByVendorId(int vendorId, Pageable pageable);
	public Page<Vouchers> findAllVoucherValidByVendorId(int vendorId, Pageable pageable);
	
	public Page<Vouchers> findAllVoucherEnableByVendorId(int vendorId, Pageable pageable);
	public Page<Vouchers> findAllVoucherDisableByVendorId(int vendorId, Pageable pageable);

}
