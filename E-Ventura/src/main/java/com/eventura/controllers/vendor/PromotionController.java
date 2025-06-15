package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("vendorPromotionController")
@RequestMapping("vendor/promotion")
public class PromotionController  {

	/*===================== PROMOTION =====================*/
	@GetMapping("list")
	public String promotionList() {
		return "vendor/pages/promotion/list";
	}
	
	@GetMapping("add")
	public String promotionAdd() {
		return "vendor/pages/promotion/add";
	}
	
	@GetMapping("edit")
	public String promotionEdit() {
		return "vendor/pages/promotion/edit";
	}
	
	
}
