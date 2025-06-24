package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.Orders;
import com.eventura.repositories.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Page<OrderVendorDTO> findOrdersByVendorPage(int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		return orderRepository.findOrdersByVendorPage(vendorId, pageable);
	}

	@Override
	public Orders findOrderByOrderId(int orderId) {
		// TODO Auto-generated method stub
		return orderRepository.findOrderByOrderId(orderId);
	}

}
