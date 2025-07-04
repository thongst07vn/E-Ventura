package com.eventura.services;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eventura.entities.Coupons;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.repositories.CouponRepository;
import com.eventura.repositories.UserAddressRepository;
import com.eventura.repositories.UserRepository;

@Service
public class CouponsServiceImpl implements CouponsService {
	
	@Autowired
	private CouponRepository couponRepository;
	
	@Override
	public List<Coupons> findAllCoupons() {

		return couponRepository.findAllCoupons();
	}

	@Override
	public List<Coupons> findAllCouponsByVendorId(int id) {
		
		return couponRepository.findAllCouponsByVendorId(id);
	}
  @Override
	public Page<Coupons> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findAll(pageable);
	}

	@Override
	public Page<Coupons> findAllByDeletedAtISNUL(Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findAllByDeletedAtISNUL(pageable);
	}

	@Override
	public Page<Coupons> findAllByDeletedAtISNOTNUL(Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findAllByDeletedAtISNOTNUL(pageable);
	}

	@Override
	public Page<Coupons> findAllCouponExpired(Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findAllCouponExpired(pageable);
	}

	@Override
	public Page<Coupons> findAllCouponInValid(Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findAllCouponInvalid(pageable);
	}

	@Override
	public Page<Coupons> findAllCouponValid(Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findAllCouponValid(pageable);
	}

	@Override
	public Page<Coupons> findByVendorId(int id, Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findByVendorId(id, pageable);
	}

	@Override
	public Page<Coupons> findByKeyword(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findByKeyword(keyword, pageable);
	}

	@Override
	public Coupons findById(int id) {
		// TODO Auto-generated method stub
		return couponRepository.findById(id).get();
	}

	@Override
	public boolean save(Coupons coupons) {
		// TODO Auto-generated method stub
		try {
			couponRepository.save(coupons);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Page<Coupons> findAllCouponExpiredByVendorId(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findAllCouponExpiredByVendorId(vendorId, pageable);
	}

	@Override
	public Page<Coupons> findAllCouponInValidByVendorId(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findAllCouponInvalidByVendorId(vendorId, pageable);
	}

	@Override
	public Page<Coupons> findAllCouponValidByVendorId(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findAllCouponValidByVendorId(vendorId, pageable);
	}

	@Override
	public Page<Coupons> findAllCouponsEnableByVendorId(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findAllCouponsEnableByVendorId(vendorId, pageable);
	}

	@Override
	public Page<Coupons> findAllCouponsDisableByVendorId(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return couponRepository.findAllCouponsDisableByVendorId(vendorId, pageable);
	}


}
