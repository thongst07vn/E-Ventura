package com.eventura.controllers.admin; // Adjust package as needed

import com.eventura.dtos.OrderStatusUpdateRequest;
import com.eventura.dtos.StatusUpdateResponse;
import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderItemsOrderStatus;
import com.eventura.entities.OrderItemsOrderStatusId;
import com.eventura.entities.Vouchers;
import com.eventura.services.OrderItemService;
import com.eventura.services.OrderService;
import com.eventura.services.OrderStatusService;
import com.eventura.services.VouchersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController // Use @RestController for REST APIs returning JSON
@RequestMapping("/admin/order-items")
public class OrderAPI {
	@Autowired
	private OrderStatusService orderStatusService;
	@Autowired
	private OrderItemService orderItemService;
	@PostMapping("/update-status")
	public ResponseEntity<?> updateOrderItemStatus(@RequestBody OrderStatusUpdateRequest request) {
		try {
			System.out.println(request.getOrderItemId());
			OrderItems orderItems = orderItemService.findById(request.getOrderItemId());
			System.out.println(orderItems);
			OrderItemsOrderStatusId orderItemsOrderStatusId = new OrderItemsOrderStatusId();
			if("Delivering".equals(request.getNewStatus())) {				
				orderItemsOrderStatusId = new OrderItemsOrderStatusId(orderItems.getId(),orderStatusService.findById(3).getId());
			}else if ("Received".equals(request.getNewStatus())) {
				orderItemsOrderStatusId = new OrderItemsOrderStatusId(orderItems.getId(),orderStatusService.findById(4).getId());
			} else if ("Canceled".equals(request.getNewStatus())) {
				orderItemsOrderStatusId = new OrderItemsOrderStatusId(orderItems.getId(),orderStatusService.findById(5).getId());
			} 
			OrderItemsOrderStatus orderItemsOrderStatus = new OrderItemsOrderStatus(orderItemsOrderStatusId, orderItems, orderStatusService.findById(1), new Date());
			if(!orderStatusService.saveOrderItemsOrderStatus(orderItemsOrderStatus)) {							
				System.out.println("save order item order status wrong " + orderItems.getId());						
			}
			return ResponseEntity.ok().body(new StatusUpdateResponse(true, "Status updated successfully"));
		} catch (Exception e) {
			// Handle exceptions (e.g., order item not found, invalid status)
			e.printStackTrace();
			return ResponseEntity.status(500)
					.body(new StatusUpdateResponse(false, "Error updating status: " + e.getMessage()));
		}
	}
}