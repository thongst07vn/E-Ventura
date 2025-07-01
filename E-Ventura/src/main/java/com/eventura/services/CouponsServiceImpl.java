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
	
	
}
