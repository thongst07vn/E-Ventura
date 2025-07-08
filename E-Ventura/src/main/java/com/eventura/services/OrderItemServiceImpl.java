package com.eventura.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderItemsOrderStatus;
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

	
	@Override
	public Map<Integer, List<OrderItems>> groupOrderItemsByVendor(List<OrderItems> items) {
		items.sort(Comparator.comparing((OrderItems item) -> {
			return item.getOrderItemsOrderStatuses().stream()
				.map(status -> status.getCreatedAt())
				.max(Date::compareTo)
				.orElse(new Date(0));
		}).reversed());

		Map<Integer, List<OrderItems>> grouped = new LinkedHashMap<>();
		for (OrderItems item : items) {
			Integer vendorId = item.getProducts().getVendors().getId();
			grouped.computeIfAbsent(vendorId, k -> new ArrayList<>()).add(item);
		}
		return grouped;
	}
	
	 @Override // THÊM @Override VÀ TRIỂN KHAI PHƯƠNG THỨC NÀY
    public OrderItemsOrderStatus getLatestStatusForOrderItem(OrderItems orderItem) {
        return orderItem.getOrderItemsOrderStatuses().stream()
                .max(Comparator.comparing(OrderItemsOrderStatus::getCreatedAt))
                .orElse(null);
    }

	 @Override
	    public List<OrderItems> findAllOrderItemsByOrderIdAndStatus(int orderId) {
	        // SỬ DỤNG PHƯƠNG THỨC JOIN FETCH Ở ĐÂY
	        return orderItemRepository.findOrderItemsByOrderIdWithStatuses(orderId);
	    }

	@Override
    public List<OrderItems> findAllOrderItemsByOrderIdAndVendorIdWithStatuses(int orderId, int vendorId) {
        return orderItemRepository.findOrderItemsByOrderIdAndVendorId(orderId, vendorId);
    }
	





}
