package com.eventura.controllers.vendor;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderItemsOrderStatus;
import com.eventura.entities.OrderItemsOrderStatusId;
import com.eventura.entities.OrderStatus;
import com.eventura.entities.Orders;
import com.eventura.entities.VendorProductCategory;
import com.eventura.repositories.VendorEarningRepository;
import com.eventura.services.OrderItemService;
import com.eventura.services.OrderService;
import com.eventura.services.OrderStatusService;
import com.eventura.services.UserAddressService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorOrderController")
@RequestMapping("vendor/order")
public class OrderController {

	private final VendorEarningRepository vendorEarningRepository;

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderStatusService orderStatusService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private UserAddressService userAddressService;

	OrderController(VendorEarningRepository vendorEarningRepository) {
		this.vendorEarningRepository = vendorEarningRepository;
	}

	/* ===================== ORDER ===================== */
	@GetMapping("list")
	public String orderList(ModelMap modelMap, HttpSession session, @RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "order");
		int vendorId = (Integer) session.getAttribute("vendorId");

		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OrderVendorDTO> orderVendorPages = orderService.findOrdersByVendorPage(vendorId, pageable);

		modelMap.put("orders", orderVendorPages.getContent());
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", orderVendorPages.getTotalPages());
		modelMap.put("lastPageIndex", orderVendorPages.getTotalPages() - 1);

		modelMap.put("orderStatuses", orderStatusService.findAll());
		return "vendor/pages/order/list";
	}

	@GetMapping("findByStatus")
	public String findByStatus(@RequestParam("orderStatusId") int orderStatusId, ModelMap modelMap, HttpSession session,
			@RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "order");
		int vendorId = (Integer) session.getAttribute("vendorId");

		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OrderVendorDTO> orderStatusPages = orderService.findOrdersByStatusPage(vendorId, orderStatusId, pageable);
		Page<OrderVendorDTO> orderVendorPages = orderService.findOrdersByVendorPage(vendorId, pageable);

		if (orderStatusId == 0) {
			modelMap.put("orders", orderVendorPages.getContent());
			modelMap.put("currentPages", page);
			modelMap.put("totalPage", orderVendorPages.getTotalPages());
			modelMap.put("lastPageIndex", orderVendorPages.getTotalPages() - 1);
		} else {
			modelMap.put("orders", orderStatusPages.getContent());
			modelMap.put("currentPages", page);
			modelMap.put("totalPage", orderStatusPages.getTotalPages());
			modelMap.put("lastPageIndex", orderStatusPages.getTotalPages() - 1);
		}

		modelMap.put("selectedOrderStatusId", orderStatusId);
		modelMap.put("orderStatuses", orderStatusService.findAll());

		return "vendor/pages/order/list";
	}

	@GetMapping("searchByKeyword")
	public String findByKeyword(@RequestParam("keyword") String keyword, ModelMap modelMap, HttpSession session,
			@RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "order");
		int vendorId = (Integer) session.getAttribute("vendorId");

		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OrderVendorDTO> orderPages = orderService.findByKeyword(vendorId, keyword, pageable);

		modelMap.put("orders", orderPages.getContent());
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", orderPages.getTotalPages());
		modelMap.put("lastPageIndex", orderPages.getTotalPages() - 1);

		modelMap.put("selectedKeyword", keyword);
		modelMap.put("orderStatuses", orderStatusService.findAll());

		return "vendor/pages/order/list";
	}

	@GetMapping("detail/{orderId}")
	public String orderDetail(@PathVariable("orderId") int orderId, ModelMap modelMap, HttpSession session,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1") int statusId) {
		modelMap.put("currentPage", "order");
		int vendorId = (Integer) session.getAttribute("vendorId");

		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OrderItems> orderItemPages = orderItemService.findOrderItemsByOrderIdPage(orderId, vendorId, pageable);
		Orders order = orderService.findOrderByOrderId(orderId);

		List<Map<String, Object>> orderItemStatuses = orderItemPages.getContent().stream().map(item -> {
			// Assuming findStatusByOrderItemId returns a list of statuses,
			// and you want the one with the latest timestamp (e.g., 'updatedAt' or
			// 'createdAt')
			List<OrderItemsOrderStatus> statuses = orderStatusService.findStatusByOrderItemId(item.getId());

			// Find the latest status for the current order item
			OrderItemsOrderStatus latestStatus = statuses.stream()
					.max(Comparator.comparing(OrderItemsOrderStatus::getCreatedAt)) // Or getCreatedAt() if that's your
																					// timestamp
					.orElse(null); // Handle case where no status is found

			Map<String, Object> statusMap = new HashMap<>();
			if (latestStatus != null) {
				statusMap.put("statusName", latestStatus.getOrderStatus().getName());
				statusMap.put("orderItemId", latestStatus.getOrderItems().getId());
			}
			return statusMap;
		}).filter(map -> map.containsKey("statusName")) // Filter out items that didn't have a status
				.collect(Collectors.toList());

		modelMap.put("orderItemStatuses", orderItemStatuses);
		modelMap.put("orderItems", orderItemPages.getContent());
		modelMap.put("order", order);

		modelMap.put("userAddresses", userAddressService.findUserAddressesByOrderId(orderId));
		modelMap.put("totalAmount", orderItemService.findTotalAmountByOrderIdAndVendorId(orderId, vendorId));
		modelMap.put("shippingAmount", order.getTotalAmount() - orderItemService.findTotalAmountByOrderIdAndVendorId(orderId, vendorId));


		/* Truyền lại orderId */
		modelMap.put("orderId", orderId);
		System.out.println(userAddressService.findUserAddressesByOrderId(orderId));
		
		/* Hiển lại orderStatus */
		List<OrderItems> orderItems = orderItemService.findAllOrderItemsByOrderId(orderId);
		List<OrderItemsOrderStatus> orderItemsOrderStatus = orderStatusService.findStatusByOrderItemId(orderItems.get(0).getId());
		modelMap.put("selectedStatusId", orderItemsOrderStatus.get(0).getOrderStatus().getId());
		
		return "vendor/pages/order/detail";
	}

	@PostMapping("changeStatus")
	public String changeStatus(ModelMap modelMap, @RequestParam("orderId") int orderId,
			@RequestParam("statusId") int statusId, RedirectAttributes redirectAttributes, HttpSession session) {
		modelMap.put("currentPage", "order");
		int vendorId = (Integer) session.getAttribute("vendorId");


		// **Quan trọng: Thêm kiểm tra quyền và trạng thái hợp lệ cho Vendor ở đây**
		// Ví dụ:
		if (statusId != 1 && statusId != 2 && statusId != 5) { // Giả sử 1 là Ordered, 2 là Confirm
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "You don't have authority to put this status.");
			return "redirect:/vendor/order/detail/" + orderId;
		}

		List<OrderItems> orderItems = orderItemService.findAllOrderItemsByOrderIdAndVendorId(orderId, vendorId);

		if (orderItems.isEmpty()) {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Your order has no items.");
			return "redirect:/vendor/order/list";
		}

		OrderStatus newOrderStatus = orderStatusService.findById(statusId);
		if (newOrderStatus == null) {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Your status is invalid.");
			return "redirect:/vendor/order/detail/" + orderId;
		}

		boolean allSuccessful = true;

		for (OrderItems item : orderItems) {
			// 1. TẠO MỘT ID PHỨC HỢP MỚI cho dòng lịch sử này
			OrderItemsOrderStatusId compositeId = new OrderItemsOrderStatusId(item.getId(), statusId);

			OrderItemsOrderStatus orderItemsOrderStatus = new OrderItemsOrderStatus();

			// 3. THIẾT LẬP ID PHỨC HỢP CHO ĐỐI TƯỢNG MỚI
			orderItemsOrderStatus.setId(compositeId);

			// 4. Thiết lập các mối quan hệ và thời gian tạo
			orderItemsOrderStatus.setOrderItems(item);
			orderItemsOrderStatus.setOrderStatus(newOrderStatus);
			orderItemsOrderStatus.setCreatedAt(new Date()); // Ghi lại thời điểm thay đổi

			if (!orderStatusService.saveOrderItemsOrderStatus(orderItemsOrderStatus)) {
				allSuccessful = false;
			}	// trạng thái
		}
		if (allSuccessful) {
			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Change your order status successfuly!");
		} else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Some items cannot update status.");
		}
		return "redirect:/vendor/order/detail/" + orderId + "?selectedStatusId=" + statusId;

	}

	@GetMapping("tracking/{orderId}")
	public String orderTracking(@PathVariable("orderId") int orderId, ModelMap modelMap) {
		modelMap.put("currentPage", "order");

		modelMap.put("userAddresses", userAddressService.findUserAddressesByOrderId(orderId));
		modelMap.put("order", orderService.findOrderByOrderId(orderId));

		return "vendor/pages/order/tracking";
	}

	@GetMapping("returnList")
	public String orderReturnList(ModelMap modelMap) {
		modelMap.put("currentPage", "order");

		return "vendor/pages/order/return_dispute_list";
	}

	@GetMapping("returnDetail")
	public String orderReturnDetail(ModelMap modelMap) {
		modelMap.put("currentPage", "order");

		return "vendor/pages/order/return_dispute_detail";
	}

}
