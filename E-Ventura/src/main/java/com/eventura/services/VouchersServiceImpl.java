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


	@Override
	public Page<Vouchers> findAllByDeletedAtISNUL(Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findAllByDeletedAtISNUL(pageable);
	}


	@Override
	public Page<Vouchers> findAllByDeletedAtISNOTNUL(Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findAllByDeletedAtISNOTNUL(pageable);
	}


	@Override
	public Page<Vouchers> findAllVoucherExpired(Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findAllVoucherExpired(pageable);
	}


	@Override
	public Page<Vouchers> findAllVoucherInValid(Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findAllVoucherInvalid(pageable);
	}


	@Override
	public Page<Vouchers> findAllVoucherValid(Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findAllVoucherValid(pageable);
	}


	@Override
	public Page<Vouchers> findByVendorId(int id, Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findByVendorId(id, pageable);
	}


	@Override
	public Page<Vouchers> findByVendorIdISNULL(Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findByVendorIdISNULL(pageable);
	}


	@Override
	public boolean save(Vouchers vouchers) {
		// TODO Auto-generated method stub
		try {
			voucherRepository.save(vouchers);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public Vouchers findById(int id) {
		// TODO Auto-generated method stub
		return voucherRepository.findById(id).get();
	}


	@Override
	public Page<Vouchers> findAllVoucherExpiredByVendorId(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findAllVoucherExpiredByVendorId(vendorId, pageable);
	}


	@Override
	public Page<Vouchers> findAllVoucherInValidByVendorId(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findAllVoucherInvalidByVendorId(vendorId, pageable);
	}


	@Override
	public Page<Vouchers> findAllVoucherValidByVendorId(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findAllVoucherValidByVendorId(vendorId, pageable);
	}


	@Override
	public Page<Vouchers> findAllVoucherEnableByVendorId(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findAllVoucherEnableByVendorId(vendorId, pageable);
	}


	@Override
	public Page<Vouchers> findAllVoucherDisableByVendorId(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findAllVoucherDisableByVendorId(vendorId, pageable);
	}


	
	
}
