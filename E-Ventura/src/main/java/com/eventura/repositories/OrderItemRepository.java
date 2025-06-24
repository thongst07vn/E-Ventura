package com.eventura.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
import com.eventura.entities.Orders;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems, Integer> {

	@Query("""
			  SELECT oi
			  FROM OrderItems oi
			  WHERE oi.orders.id = :orderId
		""")
	Page<OrderItems> findOrderItemsByOrderId(@Param("orderId") Integer orderId, Pageable pageable);


}

