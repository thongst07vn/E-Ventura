package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderStatus;
import com.eventura.entities.Orders;
import com.eventura.repositories.OrderStatusRepository;

@Service
public class OrderStatusServiceImpl implements OrderStatusService{

	@Autowired
	private OrderStatusRepository orderStatusRepository;
	
	@Override
	public List<OrderStatus> findAll() {
		// TODO Auto-generated method stub
		return orderStatusRepository.findAll();
	}

}
