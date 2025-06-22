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
import com.eventura.entities.Vendors;
import com.eventura.repositories.UserRepository;
import com.eventura.repositories.VendorEarningRepository;
import com.eventura.repositories.VendorRepository;

@Service
public class VendorServiceImpl implements VendorService {

    private final UserRepository userRepository;

	@Autowired
	private VendorRepository vendorRepository;
	@Autowired
	private VendorEarningRepository vendorEarningRepository;

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
		return vendorEarningRepository.countByVendorId(id);
	}

	@Override
	public int sumByVendorId(int id) {
		// TODO Auto-generated method stub
		return vendorEarningRepository.sumByVendorId(id);
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

}
