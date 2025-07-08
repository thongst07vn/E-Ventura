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
import com.eventura.entities.Products;
import com.eventura.entities.VendorProductCategory;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {

	@Query("""
		    SELECT new com.eventura.dtos.OrderVendorDTO(
		        o.id,
		        o.name,
		        o.paymentMethod,
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
		    GROUP BY o.id, o.name, o.paymentMethod, o.createdAt, os.orderStatus
		""")
	Page<OrderVendorDTO> findOrdersByVendorPage(@Param("vendorId") Integer vendorId, Pageable pageable);
	
	@Query("From Orders where id = :orderId")
	Orders findOrderByOrderId(@Param("orderId") int orderId);
	
	@Query("""
			SELECT new com.eventura.dtos.OrderVendorDTO(
		        o.id,
		        o.name,
		        o.paymentMethod,
		        o.createdAt,
		        SUM(oi.price * oi.quantity),
		        oios.orderStatus
		    )
		    FROM Orders o
		    JOIN o.orderItemses oi
		    JOIN oi.products p
		    JOIN p.vendors v
		    LEFT JOIN oi.orderItemsOrderStatuses oios
		    JOIN oios.orderStatus os
		    WHERE v.id = :vendorId AND os.id = :statusId
		    GROUP BY o.id, o.name, o.createdAt, oios.orderStatus
		""")
	Page<OrderVendorDTO> findOrdersByStatusPage(@Param("vendorId") Integer vendorId, @Param("statusId") Integer statusId, Pageable pageable);
	
	@Query("""
			SELECT new com.eventura.dtos.OrderVendorDTO(
		        o.id,
		        o.name,
		        o.paymentMethod, 
		        o.createdAt,
		        SUM(oi.price * oi.quantity),
		        oios.orderStatus
		    )
		    FROM Orders o
		    JOIN o.orderItemses oi
		    JOIN oi.products p
		    JOIN p.vendors v
		    LEFT JOIN oi.orderItemsOrderStatuses oios
		    JOIN oios.orderStatus os
		    WHERE v.id = :vendorId AND o.name like %:keyword%
		    GROUP BY o.id, o.name, o.paymentMethod,  o.createdAt, oios.orderStatus
		""")
	Page<OrderVendorDTO> findByKeyword(@Param("vendorId") int vendorId, @Param("keyword") String keyword, Pageable pageable);
	
	@Query("from Orders where users.id = :userId")
	Page<Orders>  findOrdersByUserId(@Param("userId") int userId, Pageable pageable);

}

