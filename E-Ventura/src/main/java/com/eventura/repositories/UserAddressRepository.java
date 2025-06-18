package com.eventura.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Products;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {

	@Query("from UserAddress where users.id = :user_id")
	public UserAddress findAddressUser(@Param("user_id") int user_id);
}
