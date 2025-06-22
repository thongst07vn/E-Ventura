package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.eventura.entities.Roles;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;

public interface RoleService {
	
	public Roles findById(int id);

	
}
