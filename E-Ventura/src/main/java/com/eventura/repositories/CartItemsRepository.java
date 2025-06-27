package com.eventura.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.entities.CartItems;
import com.eventura.entities.Carts;
import com.eventura.entities.Districts;
import com.eventura.entities.Provinces;
import com.eventura.entities.Wards;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {
	@Query("from CartItems where carts.id = :cartId")
	public List<CartItems> findAllCartItemsByCartId(@Param("cartId") int cartId);
	
}