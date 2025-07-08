package com.eventura.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.Products;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {

	@Query("from UserAddress where users.id = :user_id Order By createdAt desc")
	public List<UserAddress> findAddressUser(@Param("user_id") int user_id);
	
	@Query("""
		    SELECT ua
		    FROM UserAddress ua
		    JOIN ua.users u
		    JOIN u.orderses o
		    WHERE o.id = :orderId
		""")
	public List<UserAddress> findUserAddressByOrderId(@Param("orderId") Integer orderId);
}
