package com.eventura.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;

public interface UserService extends UserDetailsService {
	
	public boolean save(Users account);
	public boolean delete(int id);
	
	public Users findByEmail(String email);
	public Users findById(int id);	
	
	public UserAddress findAddressUser(int id);
	public List<Users> findAll();

	
}
