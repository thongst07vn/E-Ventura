package com.eventura.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
import com.eventura.entities.Orders;

public interface OrderService {
	public Page<OrderVendorDTO> findOrdersByVendorPage(int vendorId, Pageable pageable);
	
	public Page<OrderVendorDTO> findOrdersByStatusPage(int vendorId, int statusId, Pageable pageable);
	
	public Page<OrderVendorDTO> findByKeyword(int vendorId, String keyword, Pageable pageable);

	
	public Orders findOrderByOrderId(int orderId);
	
	public Page<Orders> findAlls(Pageable pageable);
	
	public boolean saveOrder(Orders order);
	
	public Page<Orders> findOrdersByUserId(int userId, Pageable pageable);
	

}
