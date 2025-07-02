package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.eventura.entities.Coupons;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;

public interface CouponsService{
	
	public List<Coupons> findAllCoupons();
	
	public List<Coupons> findAllCouponsByVendorId(int id);

	public Page<Coupons> findAll(Pageable pageable);
	public Page<Coupons> findAllByDeletedAtISNUL(Pageable pageable);
	public Page<Coupons> findAllByDeletedAtISNOTNUL(Pageable pageable);
	public Page<Coupons> findAllCouponExpired(Pageable pageable);
	public Page<Coupons> findAllCouponInValid(Pageable pageable);
	public Page<Coupons> findAllCouponValid(Pageable pageable);
	public Page<Coupons> findByVendorId(int id,Pageable pageable);
	public Page<Coupons> findByKeyword(String keyword, Pageable pageable);
	public Coupons findById(int id);
	public boolean save(Coupons coupons);

}
