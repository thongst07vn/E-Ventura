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
import com.eventura.entities.Vouchers;
import com.eventura.repositories.CouponRepository;
import com.eventura.repositories.UserAddressRepository;
import com.eventura.repositories.UserRepository;
import com.eventura.repositories.VoucherRepository;

@Service
public class VouchersServiceImpl implements VouchersService {
	
	@Autowired
	private VoucherRepository voucherRepository;
	

	@Override
	public Page<Vouchers> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findAll(pageable);
	}


	@Override
	public List<Vouchers> findAllVoucherByVendorId(int id, double value) {
		
		return voucherRepository.findAllVouchersByVendorId(id, value);
	}


	@Override
	public List<Vouchers> findAllVoucherByEventura(double value) {
		// TODO Auto-generated method stub
		return voucherRepository.findAllVouchers(value);
	}
	
	
}
