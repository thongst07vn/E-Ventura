package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.eventura.entities.Users;
import com.eventura.entities.VendorEarnings;
import com.eventura.entities.VendorReviews;
import com.eventura.entities.Vendors;

public interface VendorReviewService {
	public Page<VendorReviews>  findVendorReviewByVendorId(int vendorId, Pageable pageable);
	
	public int countVendorReview(int vendorId);
	
	public double avgVendorReview(int vendorId);


}
