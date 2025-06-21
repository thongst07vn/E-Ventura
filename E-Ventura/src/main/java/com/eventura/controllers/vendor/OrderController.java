package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("vendorOrderController")
@RequestMapping("vendor/order")
public class OrderController  {
	
	
	/*===================== ORDER =====================*/
	@GetMapping("list")
	public String orderList(ModelMap modelMap) {
		modelMap.put("currentPage", "order");

		return "vendor/pages/order/list";
	}
	
	@GetMapping("detail")
	public String orderDetail(ModelMap modelMap) {
		modelMap.put("currentPage", "order");

		return "vendor/pages/order/detail";
	}
	
	@GetMapping("tracking")
	public String orderTracking(ModelMap modelMap) {
		modelMap.put("currentPage", "order");

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
