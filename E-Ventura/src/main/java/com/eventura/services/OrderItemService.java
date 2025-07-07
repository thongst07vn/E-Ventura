package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;

public interface OrderItemService {
	public Page<OrderItems> findOrderItemsByOrderIdPage(int orderId, int vendorId, Pageable pageable);
	
	public double findTotalAmountByOrderIdAndVendorId(int orderId, int vendorId);
	
	public Page<OrderItems> findAlls(Pageable pageable);
	
	public Page<OrderItems> findAllOrderItemsByOrderIdPage(int orderId, Pageable pageable);
	
	public boolean saveOrderItems(OrderItems orderItems);
	
	public OrderItems findById(int id);
	
	public List<OrderItems> findAllOrderItemsByVendorId(int vendorId);
	
	public List<OrderItems> findAllOrderItemsByOrderId(int orderId);

}
