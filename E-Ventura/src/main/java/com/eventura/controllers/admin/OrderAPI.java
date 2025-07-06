package com.eventura.controllers.admin; // Adjust package as needed

import com.eventura.dtos.OrderStatusUpdateRequest;
import com.eventura.dtos.StatusUpdateResponse;
import com.eventura.dtos.UpdateVendorStatusRequest;
import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderItemsOrderStatus;
import com.eventura.entities.OrderItemsOrderStatusId;
import com.eventura.entities.OrderStatus;
import com.eventura.entities.Vouchers;
import com.eventura.repositories.OrderStatusRepository;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController // Use @RestController for REST APIs returning JSON
@RequestMapping("/admin/order-items")
public class OrderAPI {
	@Autowired
	private OrderStatusService orderStatusService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private OrderStatusRepository orderStatusRepository;
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
	@PostMapping("update-status-by-vendor")
    public ResponseEntity<StatusUpdateResponse> updateStatusByVendor(@RequestBody UpdateVendorStatusRequest request) {
        try {
            // 1. Get all order items for the given vendor
            List<OrderItems> orderItems = orderItemService.findAllOrderItemsByVendorId(request.getVendorId());

            if (orderItems.isEmpty()) {
                return ResponseEntity.badRequest().body(new StatusUpdateResponse(false, "No order items found for this vendor."));
            }

            // Determine the target status ID based on the newStatus string
            int targetStatusId;
            if ("Delivering".equals(request.getNewStatus())) {
                targetStatusId = 3; // Corresponds to your orderStatusService.findById(3)
            } else if ("Received".equals(request.getNewStatus())) {
                targetStatusId = 4; // Corresponds to your orderStatusService.findById(4)
            } else if ("Canceled".equals(request.getNewStatus())) {
                targetStatusId = 5; // Corresponds to your orderStatusService.findById(5)
            } else {
                return ResponseEntity.badRequest().body(new StatusUpdateResponse(false, "Invalid new status: " + request.getNewStatus()));
            }

            // Fetch the OrderStatus entity once for efficiency
            OrderStatus targetStatus = orderStatusService.findById(targetStatusId);
            if (targetStatus == null) {
                return ResponseEntity.status(500).body(new StatusUpdateResponse(false, "Target status with ID " + targetStatusId + " not found."));
            }

            // 3. Update the status for each order item
            for (OrderItems item : orderItems) {
                OrderItemsOrderStatusId orderItemsOrderStatusId = new OrderItemsOrderStatusId(item.getId(), targetStatus.getId());
                OrderItemsOrderStatus orderItemsOrderStatus = new OrderItemsOrderStatus(orderItemsOrderStatusId, item, targetStatus, new Date());

                // Assuming saveOrderItemsOrderStatus handles creating a new entry or updating correctly
                if (!orderStatusService.saveOrderItemsOrderStatus(orderItemsOrderStatus)) {
                    System.out.println("Failed to save order item order status for item ID: " + item.getId());
                    // You might choose to return an error here, or continue and log individual failures
                }
            }

            return ResponseEntity.ok().body(new StatusUpdateResponse(true, "Vendor order status updated successfully."));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new StatusUpdateResponse(false, "An error occurred: " + e.getMessage()));
        }
    }
}