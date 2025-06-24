package com.eventura.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
import com.eventura.repositories.OrderItemRepository;
import com.eventura.repositories.OrderRepository;

@Service
public class OrderItemServiceImpl implements OrderItemService{
	
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Override
	public Page<OrderItems> findOrderItemsByOrderId(int orderId, int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		   // Lấy tất cả OrderItems theo orderId và phân trang
	    Page<OrderItems> orderItemsPage = orderItemRepository.findOrderItemsByOrderId(orderId, pageable);
	    
	    // Lọc các OrderItems theo vendorId
	    List<OrderItems> filteredOrderItems = orderItemsPage.stream()
	        .filter(oi -> oi.getProducts().getVendors().getId().equals(vendorId))
	        .collect(Collectors.toList());

	    // Chuyển lại List thành Page
	    return new PageImpl<>(filteredOrderItems, pageable, orderItemsPage.getTotalElements());
	}
}
