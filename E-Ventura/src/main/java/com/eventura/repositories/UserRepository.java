package com.eventura.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Products;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

	@Query("from Users where email = :email")
	public Users findByEmail(@Param("email") String email);
	
	@Query("from Users where username = :username")
	public Users findByUsername(@Param("username") String username);
	
	@Query("SELECT u FROM Users u WHERE u.roles.id = 3 ORDER BY u.createdAt DESC")
	Page<Users> findUsersWithRoleId3(Pageable pageable);

	@Query("SELECT u FROM Users u WHERE u.roles.id = 3 and u.deletedAt IS NULL ORDER BY u.createdAt DESC")
	Page<Users> findUsersWithRoleId3ByDeletedAtISNULL(Pageable pageable);

	@Query("SELECT u FROM Users u WHERE u.roles.id = 3 and u.deletedAt IS NOT NULL ORDER BY u.createdAt DESC")
	Page<Users> findUsersWithRoleId3ByDeletedAtNOTNULL(Pageable pageable);
	
	@Query("SELECT u FROM Users u WHERE u.roles.id = 3 and u.deletedAt IS NOT NULL and username like %:keyword% ORDER BY u.createdAt DESC")
	public Page<Users> findUsersWithRoleId3ByDeletedAtNOTNULLByKeyword(@Param("keyword") String keyword,Pageable pageable);
	
	@Query("SELECT u FROM Users u WHERE u.roles.id = 3 and u.deletedAt IS NULL and username like %:keyword% ORDER BY u.createdAt DESC")
	Page<Users> findUsersWithRoleId3ByDeletedAtISNULLByKeyword(@Param("keyword") String keyword,Pageable pageable);
	
	@Query("SELECT u FROM Users u WHERE u.roles.id = 3 and username like %:keyword% ORDER BY u.createdAt DESC")
	Page<Users> findUsersWithRoleId3ByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
