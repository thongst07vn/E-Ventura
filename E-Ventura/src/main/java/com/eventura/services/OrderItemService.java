package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;

public interface OrderItemService {
	public Page<OrderItems> findOrderItemsByOrderId(int orderId, int vendorId, Pageable pageable);
}
