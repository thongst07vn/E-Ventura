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

import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.repositories.UserAddressRepository;
import com.eventura.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserAddressRepository addressRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Email khong ton tai");
		} else {
			List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
			roles.add(new SimpleGrantedAuthority("ROLE_"+user.getRoles().getName()));		
			return new User(user.getEmail(), user.getPassword(), roles);
		}
	}

	@Override
	public boolean save(Users account) {
		try {
			userRepository.save(account);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Users findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

	@Override
	public Users findById(int id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id).get();
	}

	@Override
	public List<UserAddress> findAddressUser(int id) {
		// TODO Auto-generated method stub
		return addressRepository.findAddressUser(id);
	}

	@Override
	public List<Users> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public Page<Users> findAlls(Pageable pageable) {
		// TODO Auto-generated method stub
		return userRepository.findUsersWithRoleId3(pageable);
	}

	@Override
	public Page<Users> findAllByDeletedAtISNUL(Pageable pageable) {
		// TODO Auto-generated method stub
		return userRepository.findUsersWithRoleId3ByDeletedAtISNULL(pageable);
	}

	@Override
	public Page<Users> findAllByDeletedAtISNOTNUL(Pageable pageable) {
		// TODO Auto-generated method stub
		return userRepository.findUsersWithRoleId3ByDeletedAtNOTNULL(pageable);
	}



	
}
