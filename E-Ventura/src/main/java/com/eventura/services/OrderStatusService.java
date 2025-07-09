package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderItemsOrderStatus;
import com.eventura.entities.OrderStatus;
import com.eventura.entities.Orders;

public interface OrderStatusService {
	public List<OrderStatus> findAll();
	
	public List<OrderItemsOrderStatus> findStatusByOrderItemId(int order_id);
	public List<OrderItemsOrderStatus> findItemStatusAll();
	
	public OrderStatus findById(int id);
	public OrderStatus findByName(String name);
	
	public boolean saveOrderItemsOrderStatus(OrderItemsOrderStatus orderItemsOrderStatus);
}
