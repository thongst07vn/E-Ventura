package com.eventura.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {

	@Query("""
		    SELECT new com.eventura.dtos.OrderVendorDTO(
		        o.id,
		        o.name,
		        o.createdAt,
		        SUM(oi.price * oi.quantity),
		        os.orderStatus 
		    )
		    FROM Orders o
		    JOIN o.orderItemses oi
		    JOIN oi.products p
		    JOIN p.vendors v
		    LEFT JOIN oi.orderItemsOrderStatuses os  
		    WHERE v.id = :vendorId
		    GROUP BY o.id, o.name, o.createdAt, os.orderStatus
		""")
		Page<OrderVendorDTO> findOrdersByVendorPage(@Param("vendorId") Integer vendorId, Pageable pageable);
	
	@Query("From Orders where id = :orderId")
	Orders findOrderByOrderId(@Param("orderId") int orderId);
	


}

