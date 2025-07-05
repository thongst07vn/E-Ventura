package com.eventura.controllers.vendor;

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
import com.eventura.services.AddressService;
import com.eventura.services.CategoryService;
import com.eventura.services.OrderService;
import com.eventura.services.OrderStatusService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.eventura.services.VendorReviewService;
import com.eventura.services.VendorService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorDashboardController")
@RequestMapping("vendor/dashboard")
public class DashboardController  {
	
	@Autowired
	private VendorService vendorService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private VendorReviewService vendorReviewService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderStatusService orderStatusService;
	@Autowired
	private UserService userService;
	@Autowired
	private AddressService addressService;
	
	
	/*===================== DASHBOARD =====================*/
	@GetMapping("home")
	public String home(ModelMap modelMap, HttpSession session,
					  @RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "dashboard");
		int vendorId = (Integer) session.getAttribute("vendorId");
		
		/* Top 5 Follower */
		modelMap.put("followers", vendorReviewService.getTop5LatestReviews(vendorId));

		/* DASHBOARD */
		modelMap.put("totalFollowers", vendorReviewService.countFollowerByVendorId(vendorId));
		modelMap.put("totalRevenues", vendorService.sumByVendorId(vendorId));
		modelMap.put("totalOrders", vendorService.countByVendorId(vendorId));
		modelMap.put("totalProducts", productService.findByVendorId(vendorId).size());
		modelMap.put("totalCategories", categoryService.findAll().size());
	
		/* Phân trang Order */
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OrderVendorDTO> orderVendorPages = orderService.findOrdersByVendorPage(vendorId, pageable);
		modelMap.put("orders", orderVendorPages.getContent());
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", orderVendorPages.getTotalPages());
		modelMap.put("lastPageIndex", orderVendorPages.getTotalPages() - 1);
		
		modelMap.put("orderStatuses", orderStatusService.findAll());

		
		
		return "vendor/pages/dashboard/index";
	}
	
	@GetMapping("findByStatus")
	public String findByStatus(@RequestParam("orderStatusId") int orderStatusId, ModelMap modelMap, HttpSession session,
							   @RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "order");
		int vendorId = (Integer) session.getAttribute("vendorId");
		
		/* DASHBOARD */
		modelMap.put("totalFollowers", vendorReviewService.countFollowerByVendorId(vendorId));
		modelMap.put("totalRevenues", vendorService.sumByVendorId(vendorId));
		modelMap.put("totalOrders", vendorService.countByVendorId(vendorId));
		modelMap.put("totalProducts", productService.findByVendorId(vendorId).size());
		modelMap.put("totalCategories", categoryService.findAll().size());
		
		int pageSize = 10;
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


		return "vendor/pages/dashboard/index";
	}
	
	@GetMapping("searchByKeyword")
	public String findByKeyword(@RequestParam("keyword") String keyword, ModelMap modelMap, HttpSession session,
							   @RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "order");
		int vendorId = (Integer) session.getAttribute("vendorId");
		
		/* DASHBOARD */
		modelMap.put("totalFollowers", vendorReviewService.countFollowerByVendorId(vendorId));
		modelMap.put("totalRevenues", vendorService.sumByVendorId(vendorId));
		modelMap.put("totalOrders", vendorService.countByVendorId(vendorId));
		modelMap.put("totalProducts", productService.findByVendorId(vendorId).size());
		modelMap.put("totalCategories", categoryService.findAll().size());
		
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OrderVendorDTO> orderPages = orderService.findByKeyword(vendorId, keyword, pageable);
		
		modelMap.put("orders", orderPages.getContent());
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", orderPages.getTotalPages());
		modelMap.put("lastPageIndex", orderPages.getTotalPages() - 1);
		
		modelMap.put("selectedKeyword", keyword);
		modelMap.put("orderStatuses", orderStatusService.findAll());

		return "vendor/pages/dashboard/index";
	}
	
	@GetMapping("userDetails/{userId}")
	public String userDetails(@PathVariable("userId") int userId, ModelMap modelMap) {
		
		modelMap.put("user", userService.findById(userId));
		modelMap.put("userAdresses", userService.findAddressUser(userId));

		return "vendor/pages/user/detail";
	}
	
	
}
