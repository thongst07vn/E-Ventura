package com.eventura.controllers.admin; // Adjust package as needed

import com.eventura.dtos.OrderItemBulkStatusUpdateDTO;
import com.eventura.dtos.OrderItemStatusUpdateDTO;
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

import java.lang.classfile.instruction.NewObjectInstruction;
import java.util.ArrayList;
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
	public ResponseEntity<Map<String, Object>> updateOrderItemStatus(@RequestBody OrderItemStatusUpdateDTO dto) {
		Map<String, Object> response = new HashMap<>();
		try {
			// 1. Find the OrderItem
			OrderItems optionalOrderItem = orderItemService.findById(dto.getOrderItemId());
			if (optionalOrderItem == null) {
				response.put("success", false);
				response.put("message", "Order Item not found with ID: " + dto.getOrderItemId());
				return ResponseEntity.badRequest().body(response);
			}

			// 2. Find the new OrderStatus by name
			OrderStatus optionalNewStatus = orderStatusService.findByName(dto.getNewStatusName());
			if (optionalNewStatus != null) {
				response.put("success", false);
				response.put("message", "Status not found: " + dto.getNewStatusName());
				return ResponseEntity.badRequest().body(response);
			}

			// 3. Create a new OrderItemsOrderStatus entry
			OrderItemsOrderStatusId orderItemsOrderStatusId = new OrderItemsOrderStatusId(optionalOrderItem.getId(),
					optionalNewStatus.getId());
			OrderItemsOrderStatus newOrderItemStatus = new OrderItemsOrderStatus();
			newOrderItemStatus.setId(orderItemsOrderStatusId);
			newOrderItemStatus.setOrderItems(optionalOrderItem);
			newOrderItemStatus.setOrderStatus(optionalNewStatus);
			newOrderItemStatus.setCreatedAt(new Date()); // Set current timestamp

			orderStatusService.saveOrderItemsOrderStatus(newOrderItemStatus); // Save the new status entry

			response.put("success", true);
			response.put("message", "Order item status updated successfully.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "An error occurred: " + e.getMessage());
			return ResponseEntity.status(500).body(response);
		}
	}

	@PostMapping("update-bulk-status")
	public ResponseEntity<Map<String, Object>> updateOrderItemBulkStatus(
			@RequestBody OrderItemBulkStatusUpdateDTO dto) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (dto.getOrderItemIds() == null || dto.getOrderItemIds().isEmpty()) {
				response.put("success", false);
				response.put("message", "No order item IDs provided for update.");
				return ResponseEntity.badRequest().body(response);
			}

			// Find the new OrderStatus by name
			OrderStatus newStatus = orderStatusService.findByName(dto.getNewStatusName());
			if (newStatus == null) {
				response.put("success", false);
				response.put("message", "Status not found: " + dto.getNewStatusName());
				return ResponseEntity.badRequest().body(response);
			}

			int updatedCount = 0;
			List<String> failedUpdates = new ArrayList<>();

			for (Integer orderItemId : dto.getOrderItemIds()) {
				OrderItems orderItem = orderItemService.findById(orderItemId);
				if (orderItem != null) {
					try {
						OrderItemsOrderStatusId orderItemsOrderStatusId = new OrderItemsOrderStatusId(orderItem.getId(),
								newStatus.getId());
						OrderItemsOrderStatus newOrderItemStatus = new OrderItemsOrderStatus();
						newOrderItemStatus.setId(orderItemsOrderStatusId);
						newOrderItemStatus.setOrderItems(orderItem);
						newOrderItemStatus.setOrderStatus(newStatus);
						newOrderItemStatus.setCreatedAt(new Date());

						orderStatusService.saveOrderItemsOrderStatus(newOrderItemStatus);
						updatedCount++;
					} catch (Exception e) {
						// Log the error for this specific item but continue with others
						System.err.println(
								"Failed to update status for OrderItem ID " + orderItemId + ": " + e.getMessage());
						failedUpdates.add("ID " + orderItemId + ": " + e.getMessage());
					}
				} else {
					failedUpdates.add("ID " + orderItemId + ": Not found");
				}
			}

			if (updatedCount > 0) {
				response.put("success", true);
				response.put("message", "Successfully updated status for " + updatedCount + " order items.");
				if (!failedUpdates.isEmpty()) {
					response.put("partialSuccess", true);
					response.put("failedUpdates", failedUpdates);
					response.put("message", response.get("message") + " Some items failed to update: "
							+ String.join(", ", failedUpdates));
				}
				return ResponseEntity.ok(response);
			} else {
				response.put("success", false);
				response.put("message",
						"No order items were updated. Possible reasons: items not found or all updates failed. Details: "
								+ String.join(", ", failedUpdates));
				return ResponseEntity.status(500).body(response);
			}

		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "An unexpected error occurred: " + e.getMessage());
			return ResponseEntity.status(500).body(response);
		}
	}
}