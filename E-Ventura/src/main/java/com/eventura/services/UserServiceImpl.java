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
		
		if(user == null || user.getDeletedAt()!=null) {
			throw new UsernameNotFoundException("Email khong ton tai");
		} else {
			// If the user is soft-deleted, consider them disabled
	        boolean isAccountNonLocked = true; // Assuming no locking mechanism based on your code
	        boolean isCredentialsNonExpired = true; // Assuming no credential expiration based on your code
	        boolean isAccountNonExpired = true; // Assuming no account expiration based on your code
	        boolean isEnabled = (user.getDeletedAt() == null); // User is enabled only if deletedAt is null

	        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
	        roles.add(new SimpleGrantedAuthority("ROLE_" + user.getRoles().getName()));
	        
	        return new User(user.getEmail(), user.getPassword(), isEnabled, isAccountNonExpired, isCredentialsNonExpired, isAccountNonLocked, roles);
//	        // Use Spring Security's User constructor that takes enabled status
//	        return new User(user.getEmail(), user.getPassword(), isEnabled, isAccountNonExpired, isCredentialsNonExpired, isAccountNonLocked, roles);
//			List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
//			roles.add(new SimpleGrantedAuthority("ROLE_"+user.getRoles().getName()));		
//			return new User(user.getEmail(), user.getPassword(), roles);
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

	@Override
	public Page<Users> findUsersWithRoleId3ByDeletedAtNOTNULLByKeyword(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return userRepository.findUsersWithRoleId3ByDeletedAtNOTNULLByKeyword(keyword, pageable);
	}

	@Override
	public Page<Users> findUsersWithRoleId3ByDeletedAtISNULLByKeyword(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return userRepository.findUsersWithRoleId3ByDeletedAtISNULLByKeyword(keyword, pageable);
	}

	@Override
	public Page<Users> findUsersWithRoleId3ByKeyword(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return userRepository.findUsersWithRoleId3ByKeyword(keyword, pageable);
	}

	@Override
	public Users findByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}



	
}
