package com.eventura.services;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eventura.entities.Users;
import com.eventura.entities.Vendors;
import com.eventura.repositories.VendorRepository;

@Service
public class VendorServiceImpl implements VendorService {

	@Autowired
	private VendorRepository vendorRepository;
	
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

}
