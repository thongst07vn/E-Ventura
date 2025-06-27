package com.eventura.controllers.vendor;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventura.dtos.OrderVendorDTO;
import com.eventura.entities.OrderItems;
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
public class OrderController  {

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
	
	/*===================== ORDER =====================*/
	@GetMapping("list")
	public String orderList(ModelMap modelMap, HttpSession session,
							@RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "order");
		int vendorId = (Integer) session.getAttribute("vendorId");
		
		int pageSize = 6;
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
		
		int pageSize = 6;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OrderVendorDTO> orderStatusPages = orderService.findOrdersByStatusPage(vendorId, orderStatusId, pageable);
		Page<OrderVendorDTO> orderVendorPages = orderService.findOrdersByVendorPage(vendorId, pageable);

		
		if(orderStatusId == 0) {
			modelMap.put("orders", orderVendorPages.getContent());
			modelMap.put("currentPages", page);
			modelMap.put("totalPage", orderVendorPages.getTotalPages());
			modelMap.put("lastPageIndex", orderVendorPages.getTotalPages() - 1);
		}else {
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
		
		int pageSize = 6;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OrderVendorDTO> orderPages = orderService.findByKeyword(vendorId, keyword, pageable);
		
		modelMap.put("orders", orderPages.getContent());
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", orderPages.getTotalPages());
		modelMap.put("lastPageIndex", orderPages.getTotalPages() - 1);
		
		modelMap.put("selectedKeyword", keyword);
	
		return "vendor/pages/order/list";
	}
	
	@GetMapping("detail/{orderId}")
	public String orderDetail(@PathVariable("orderId") int orderId, ModelMap modelMap, HttpSession session,
							  @RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "order");
		int vendorId = (Integer) session.getAttribute("vendorId");
		
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OrderItems> orderItemPages = orderItemService.findOrderItemsByOrderIdPage(orderId, vendorId, pageable);
		
		modelMap.put("orderItems", orderItemPages.getContent());
		
		/* Các thông tin khác */
		modelMap.put("order", orderService.findOrderByOrderId(orderId));
		modelMap.put("userAddresses", userAddressService.findUserAddressesByOrderId(orderId));
		modelMap.put("totalAmount", orderItemService.findTotalAmountByOrderIdAndVendorId(orderId, vendorId));
		
		/* Truyền lại orderId */
		modelMap.put("orderId", orderId);
		System.out.println(userAddressService.findUserAddressesByOrderId(orderId));

		return "vendor/pages/order/detail";
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
