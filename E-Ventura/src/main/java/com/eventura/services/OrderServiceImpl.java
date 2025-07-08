package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
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

	@Override
	public Page<OrderVendorDTO> findOrdersByStatusPage(int vendorId, int statusId, Pageable pageable) {
		// TODO Auto-generated method stub
		return orderRepository.findOrdersByStatusPage(vendorId, statusId, pageable);
	}

	@Override
	public Page<OrderVendorDTO> findByKeyword(int vendorId, String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return orderRepository.findByKeyword(vendorId, keyword, pageable);
	}

	@Override
	public Page<Orders> findAlls(Pageable pageable) {
		// TODO Auto-generated method stub
		return orderRepository.findAll(pageable);
	}

	@Override
	public boolean saveOrder(Orders order) {
		try {
			orderRepository.save(order);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Page<Orders> findOrdersByUserId(int userId, Pageable pageable) {
		// TODO Auto-generated method stub
		return orderRepository.findOrdersByUserId(userId, pageable);
	}

	
	
	

}
