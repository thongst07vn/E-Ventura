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
	List<OrderItems> findOrderItemsByOrderId(@Param("orderId") Integer orderId, Pageable pageable);

	@Query("""
			  SELECT oi
			  FROM OrderItems oi
			  WHERE oi.orders.id = :orderId
		""")
	Page<OrderItems> findOrderItemsByOrderIdPage(@Param("orderId") Integer orderId, Pageable pageable);
	
	@Query("""
		    SELECT SUM(oi.price * oi.quantity)
		    FROM OrderItems oi
		    JOIN oi.orders o
		    JOIN oi.products p
		    JOIN p.vendors v
		    WHERE o.id = :orderId AND v.id = :vendorId
		""")
		Double findTotalAmountByOrderIdAndVendorId(@Param("orderId") Integer orderId, @Param("vendorId") Integer vendorId);
	
	@Query("""
			  SELECT oi
			  FROM OrderItems oi
			  WHERE oi.products.vendors.id = :vendorId
		""")
	List<OrderItems> findAllOrderItemsByVendorId(@Param("vendorId") Integer vendorId);
	
	@Query("""
			  SELECT oi
			  FROM OrderItems oi
			  WHERE oi.orders.id = :orderId
		""")
	List<OrderItems> findOrderItemsByOrderId(@Param("orderId") Integer orderId);

	
	@Query("""
			  SELECT oi
			  FROM OrderItems oi
			  JOIN oi.orders o
			  JOIN oi.products p
		      JOIN p.vendors v
			  WHERE oi.orders.id = :orderId and v.id = :vendorId
		""")
	List<OrderItems> findOrderItemsByOrderIdAndVendorId(@Param("orderId") Integer orderId, @Param("vendorId") Integer vendorId);
	
	 // Thêm phương thức này để tải OrderItems cùng với các trạng thái của nó
    @Query("SELECT oi FROM OrderItems oi JOIN oi.orderItemsOrderStatuses WHERE oi.orders.id = :orderId")
    List<OrderItems> findOrderItemsByOrderIdWithStatuses(@Param("orderId") int orderId);
    
    @Query("FROM OrderItems oi JOIN oi.products p WHERE oi.orders.id = :orderId and p.vendors.id = :vendorId")
    List<OrderItems> findOrderItemsByOrderIdAndVendorId(int orderId, int vendorId); // Đảm bảo phương thức này có sẵn nếu cần.



}

