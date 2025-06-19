package com.eventura.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.eventura.entities.Roles;
import com.eventura.entities.Users;

public interface RoleService {
	
	public List<Roles> findAll();
}
