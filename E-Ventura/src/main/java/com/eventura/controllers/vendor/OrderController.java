package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("vendorOrderController")
@RequestMapping("vendor/order")
public class OrderController  {
	
	
	/*===================== ORDER =====================*/
	@GetMapping("list")
	public String orderList() {
		return "vendor/pages/order/list";
	}
	
	@GetMapping("detail")
	public String orderDetail() {
		return "vendor/pages/order/detail";
	}
	
	@GetMapping("tracking")
	public String orderTracking() {
		return "vendor/pages/order/tracking";
	}
	
	@GetMapping("returnList")
	public String orderReturnList() {
		return "vendor/pages/order/return_dispute_list";
	}
	
	@GetMapping("returnDetail")
	public String orderReturnDetail() {
		return "vendor/pages/order/return_dispute_detail";
	}

}
