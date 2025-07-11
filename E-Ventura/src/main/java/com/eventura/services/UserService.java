package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.eventura.entities.ActivityLog;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;

public interface UserService extends UserDetailsService {
	
	public boolean save(Users account);
	public boolean delete(int id);
	
	public Users findByEmail(String email);
	public Users findById(int id);	
	public Users findByUsername(String username);
	
	public List<UserAddress> findAddressUser(int id);
	
	public List<Users> findAll();
	public Page<Users> findAlls(Pageable pageable);
	public Page<Users> findAllByDeletedAtISNUL(Pageable pageable);
	public Page<Users> findAllByDeletedAtISNOTNUL(Pageable pageable);
	public Page<Users> findUsersWithRoleId3ByDeletedAtNOTNULLByKeyword(String keyword, Pageable pageable);
	public Page<Users> findUsersWithRoleId3ByDeletedAtISNULLByKeyword(String keyword, Pageable pageable);
	public Page<Users> findUsersWithRoleId3ByKeyword(String keyword, Pageable pageable);
	
	public boolean saveActivityLog(ActivityLog activityLog);
	public int countActivityLogPC();
	public int countActivityLogPhone();
	
	public List<Users> findNewUser(); 
}
