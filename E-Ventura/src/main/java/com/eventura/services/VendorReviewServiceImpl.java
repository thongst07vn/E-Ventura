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

import com.eventura.entities.Users;
import com.eventura.entities.VendorEarnings;
import com.eventura.entities.VendorReviews;
import com.eventura.entities.Vendors;
import com.eventura.repositories.UserRepository;
import com.eventura.repositories.VendorEarningRepository;
import com.eventura.repositories.VendorRepository;
import com.eventura.repositories.VendorReviewsRepository;

@Service
public class VendorReviewServiceImpl implements VendorReviewService {
	
	@Autowired
	private VendorReviewsRepository vendorReviewsRepository;

	@Override
	public Page<VendorReviews> findVendorReviewByVendorId(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return vendorReviewsRepository.findVendorReviewPage(vendorId, pageable);
	}

	@Override
	public int countVendorReview(int vendorId) {
		// TODO Auto-generated method stub
		return vendorReviewsRepository.countVendorReview(vendorId);
	}

	@Override
	public double avgVendorReview(int vendorId) {
		// TODO Auto-generated method stub
		return vendorReviewsRepository.avgVendorReview(vendorId);
	}

	@Override
	public VendorReviews findVendorReviewByUserAndVendorId(int vendorId, int userId) {
		// TODO Auto-generated method stub
		return vendorReviewsRepository.findVendorReviewByUserAndVendorId(vendorId, userId);
	}
	

}
