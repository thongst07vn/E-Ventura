package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderItemsOrderStatus;
import com.eventura.entities.OrderStatus;
import com.eventura.entities.Orders;
import com.eventura.repositories.OrderItemOrderStatusRepository;
import com.eventura.repositories.OrderStatusRepository;

@Service
public class OrderStatusServiceImpl implements OrderStatusService{

	@Autowired
	private OrderStatusRepository orderStatusRepository;
	@Autowired
	private OrderItemOrderStatusRepository orderStatusReposItemOrderStatusRepository;
	
	@Override
	public List<OrderStatus> findAll() {
		// TODO Auto-generated method stub
		return orderStatusRepository.findAll();
	}

	@Override
	public List<OrderItemsOrderStatus> findStatusByOrderItemId(int order_id) {
		// TODO Auto-generated method stub
		return orderStatusReposItemOrderStatusRepository.findStatusByOrderItemId(order_id, Sort.by("createdAt").descending());
	}

	@Override
	public List<OrderItemsOrderStatus> findItemStatusAll() {
		// TODO Auto-generated method stub
		return orderStatusReposItemOrderStatusRepository.findAll(Sort.by("createdAt").descending());
	}

	@Override
	public OrderStatus findById(int id) {
		// TODO Auto-generated method stub
		return orderStatusRepository.findById(id).get();
	}

	@Override
	public boolean saveOrderItemsOrderStatus(OrderItemsOrderStatus orderItemsOrderStatus) {
		// TODO Auto-generated method stub
		try {
			orderStatusReposItemOrderStatusRepository.save(orderItemsOrderStatus);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public OrderStatus findByName(String name) {
		// TODO Auto-generated method stub
		return orderStatusRepository.findByName(name);
	}

}
