package com.eventura.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eventura.entities.Roles;
import com.eventura.repositories.RoleRepository;


@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Roles findById(int id) {
		Optional<Roles> role= roleRepository.findById(id);
		if(role != null) {			
			return role.get();
		}
		return null;
	}
	

	
}
