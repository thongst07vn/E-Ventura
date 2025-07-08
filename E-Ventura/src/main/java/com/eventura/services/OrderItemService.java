package com.eventura.services;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderItemsOrderStatus;

public interface OrderItemService {
	public Page<OrderItems> findOrderItemsByOrderIdPage(int orderId, int vendorId, Pageable pageable);
	
	public double findTotalAmountByOrderIdAndVendorId(int orderId, int vendorId);
	
	public Page<OrderItems> findAlls(Pageable pageable);
	
	public Page<OrderItems> findAllOrderItemsByOrderIdPage(int orderId, Pageable pageable);
	
	public boolean saveOrderItems(OrderItems orderItems);
	
	public OrderItems findById(int id);
	
	public List<OrderItems> findAllOrderItemsByVendorId(int vendorId);
	
	public List<OrderItems> findAllOrderItemsByOrderId(int orderIds);
	
	public List<OrderItems> findAllOrderItemsByOrderIdAndVendorId(int orderId, int vendorId);

	public Map<Integer, List<OrderItems>> groupOrderItemsByVendor(List<OrderItems> items);

	public List<OrderItems> findAllOrderItemsByOrderIdAndStatus(int orderIds);


    public OrderItemsOrderStatus getLatestStatusForOrderItem(OrderItems orderItem);
    
    List<OrderItems> findAllOrderItemsByOrderIdAndVendorIdWithStatuses(int orderId, int vendorId);


}
