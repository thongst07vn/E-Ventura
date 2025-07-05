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
public class VendorServiceImpl implements VendorService {

    private final UserRepository userRepository;

	@Autowired
	private VendorRepository vendorRepository;
	@Autowired
	private VendorEarningRepository vendorEarningRepository;
	@Autowired
	private VendorReviewsRepository vendorReviewsRepository;

    VendorServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	@Override
	public List<Vendors> findAll() {
		// TODO Auto-generated method stub
		return vendorRepository.findAll();
	}

	@Override
	public Vendors findById(int id) {
		// TODO Auto-generated method stub
		return vendorRepository.findById(id).get();
	}

	@Override
	public int countByVendorId(int id) {
		// TODO Auto-generated method stub
		if(vendorRepository.findById(id).isPresent()) {			
			return vendorEarningRepository.countByVendorId(id);
			
		}else {			
			return 0;			
		}
	}

	@Override
	public double sumByVendorId(int id) {
		if(vendorRepository.findById(id).isPresent()) {
			return vendorEarningRepository.sumByVendorId(id);
		}else {
			return 0;			
		}
	}

	@Override
	public Page<Vendors> findAlls(Pageable pageable) {
		// TODO Auto-generated method stub
		return vendorRepository.findAll(pageable);
	}

	@Override
	public boolean disableVendorUser(Users users) {
		// TODO Auto-generated method stub
		try {
			userRepository.save(users);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<VendorReviews> findVendorReview(int id) {
		// TODO Auto-generated method stub
		return vendorReviewsRepository.findVendorReview(id);
	}

	@Override
	public int countVendorReview(int id) {
		// TODO Auto-generated method stub
		return vendorReviewsRepository.countVendorReview(id);
	}

	@Override
	public double avgVendorReview(int id) {
		// TODO Auto-generated method stub
		return vendorReviewsRepository.avgVendorReview(id);
	}

	@Override
	public Page<Vendors> findByKeywordPage(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return vendorRepository.findByKeywordPage(keyword, pageable);
	}

	@Override
	public boolean save(Vendors vendor) {
		// TODO Auto-generated method stub
		try {
			vendorRepository.save(vendor);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Page<Vendors> findAllVendorsByDeletedAtISNUL(Pageable pageable) {
		// TODO Auto-generated method stub
		return vendorRepository.findVendorByDeletedAtISNULL(pageable);
	}

	@Override
	public Page<Vendors> findAllVendorsByDeletedAtISNOTNUL(Pageable pageable) {
		// TODO Auto-generated method stub
		return vendorRepository.findVendorsByDeletedAtNOTNULL(pageable);
	}

	@Override
	public Page<Vendors> findVendorsByDeletedAtNOTNULLByKeyword(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return vendorRepository.findVendorsByDeletedAtISNULLByKeyword(keyword, pageable);
	}

	@Override
	public Page<Vendors> findVendorsByDeletedAtISNULLByKeyword(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return vendorRepository.findVendorsByDeletedAtNOTNULLByKeyword(keyword, pageable);
	}

	@Override
	public boolean saveVendorEarning(VendorEarnings vendorEarnings) {
		try {
			vendorEarningRepository.save(vendorEarnings);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
