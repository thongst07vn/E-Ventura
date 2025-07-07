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
	public Page<OrderItems> findOrderItemsByOrderIdPage(int orderId, int vendorId, Pageable pageable) {
		// TODO Auto-generated method stub
		   // Lấy tất cả OrderItems theo orderId và phân trang
	    Page<OrderItems> orderItemsPage = orderItemRepository.findOrderItemsByOrderIdPage(orderId, pageable);
	    
	    // Lọc các OrderItems theo vendorId
	    List<OrderItems> filteredOrderItems = orderItemsPage.stream()
	        .filter(oi -> oi.getProducts().getVendors().getId().equals(vendorId))
	        .collect(Collectors.toList());

	    // Chuyển lại List thành Page
	    return new PageImpl<>(filteredOrderItems, pageable, orderItemsPage.getTotalElements());
	}

	@Override
	public double findTotalAmountByOrderIdAndVendorId(int orderId, int vendorId) {
		// TODO Auto-generated method stub
		return orderItemRepository.findTotalAmountByOrderIdAndVendorId(orderId, vendorId);
	}

	@Override
	public Page<OrderItems> findAlls(Pageable pageable) {
		// TODO Auto-generated method stub
		return orderItemRepository.findAll(pageable);
	}

	@Override
	public Page<OrderItems> findAllOrderItemsByOrderIdPage(int orderId, Pageable pageable) {
		// TODO Auto-generated method stub
		return orderItemRepository.findOrderItemsByOrderIdPage(orderId,pageable);
	}

	@Override
	public boolean saveOrderItems(OrderItems orderItems) {
		try {
			orderItemRepository.save(orderItems);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public OrderItems findById(int id) {
		// TODO Auto-generated method stub
		return orderItemRepository.findById(id).get();
	}

	@Override
	public List<OrderItems> findAllOrderItemsByVendorId(int vendorId) {
		// TODO Auto-generated method stub
		return orderItemRepository.findAllOrderItemsByVendorId(vendorId);
	}

	@Override
	public List<OrderItems> findAllOrderItemsByOrderId(int orderId) {
		// TODO Auto-generated method stub
		return orderItemRepository.findOrderItemsByOrderId(orderId);
	}

	@Override
	public List<OrderItems> findAllOrderItemsByOrderIdAndVendorId(int orderIds, int vendorId) {
		// TODO Auto-generated method stub
		return orderItemRepository.findOrderItemsByOrderIdAndVendorId(orderIds, vendorId);
	}
}
