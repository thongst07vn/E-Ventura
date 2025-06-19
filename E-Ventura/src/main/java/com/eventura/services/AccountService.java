package com.eventura.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.eventura.entities.Users;

public interface AccountService extends UserDetailsService {
	
	public Users findByEmail(String email);
	public Users findById(int id);
	
	public boolean save(Users account);
	public boolean delete(int id);

}
