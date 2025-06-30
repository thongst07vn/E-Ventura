package com.eventura.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderItemsOrderStatus;
import com.eventura.entities.OrderItemsOrderStatusId;
import com.eventura.entities.OrderStatus;
import com.eventura.entities.Orders;
import com.eventura.entities.Products;

@Repository
public interface OrderItemOrderStatusRepository extends JpaRepository<OrderItemsOrderStatus, OrderItemsOrderStatusId> {
	@Query("from OrderItemsOrderStatus where orderItems.id = :orderItem_id")
	public List<OrderItemsOrderStatus> findStatusByOrderItemId(@Param("orderItem_id") int orderItem_id, Sort sort);
}

