package com.eventura.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Products;
import com.eventura.entities.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

	@Query("from Users where email = :email")
	public Users findByEmail(@Param("email") String email);
	
	@Query("SELECT u FROM Users u WHERE u.roles.id = 3 ORDER BY u.createdAt DESC")
	Page<Users> findUsersWithRoleId3(Pageable pageable);

	@Query("SELECT u FROM Users u WHERE u.roles.id = 3 and u.deletedAt IS NULL ORDER BY u.createdAt DESC")
	Page<Users> findUsersWithRoleId3ByDeletedAtISNULL(Pageable pageable);

	@Query("SELECT u FROM Users u WHERE u.roles.id = 3 and u.deletedAt IS NOT NULL ORDER BY u.createdAt DESC")
	Page<Users> findUsersWithRoleId3ByDeletedAtNOTNULL(Pageable pageable);
}
