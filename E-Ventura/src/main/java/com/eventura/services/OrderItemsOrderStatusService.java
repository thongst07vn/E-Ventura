package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderItemsOrderStatus;
import com.eventura.entities.OrderStatus;
import com.eventura.entities.Orders;

public interface OrderItemsOrderStatusService {
	
	public boolean saveOrderItemsOrderStatus(OrderItemsOrderStatus orderItemsOrderStatus);
}
