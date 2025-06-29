package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.entities.Users;
import com.eventura.entities.VendorEarnings;
import com.eventura.entities.VendorReviews;
import com.eventura.entities.Vendors;

public interface VendorService {
	
	public List<Vendors> findAll();
	
	public Vendors findById(int id);
	
	public int countByVendorId(int id); 
	
	public double sumByVendorId(int id); 
	
	public Page<Vendors> findAlls(Pageable pageable);
	
	public boolean disableVendorUser(Users users);
	
	public List<VendorReviews> findVendorReview(int id);
	
	public int countVendorReview(int id);

	public double avgVendorReview(int id);
	
	public Page<Vendors> findByKeywordPage(String keyword, Pageable pageable);
	
	public boolean save(Vendors vendor);
	
	public Page<Vendors> findAllVendorsByDeletedAtISNUL(Pageable pageable);
	public Page<Vendors> findAllVendorsByDeletedAtISNOTNUL(Pageable pageable);
	public Page<Vendors> findVendorsByDeletedAtNOTNULLByKeyword(String keyword, Pageable pageable);
	public Page<Vendors> findVendorsByDeletedAtISNULLByKeyword(String keyword, Pageable pageable);
}
