package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.eventura.entities.Coupons;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;
import com.eventura.entities.Vouchers;

public interface VouchersService{
	
	public Page<Vouchers> findAll(Pageable pageable);
	
	public List<Vouchers> findAllVoucherByVendorId(int id, double value);
	public List<Vouchers> findAllVoucherByEventura(double value);
}
