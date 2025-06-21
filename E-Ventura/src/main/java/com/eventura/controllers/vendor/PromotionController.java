package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("vendorPromotionController")
@RequestMapping("vendor/promotion")
public class PromotionController  {

	/*===================== PROMOTION =====================*/
	@GetMapping("list")
	public String promotionList(ModelMap modelMap) {
		modelMap.put("currentPage", "promotion");

		return "vendor/pages/promotion/list";
	}
	
	@GetMapping("add")
	public String promotionAdd(ModelMap modelMap) {
		modelMap.put("currentPage", "promotion");

		return "vendor/pages/promotion/add";
	}
	
	@GetMapping("edit")
	public String promotionEdit(ModelMap modelMap) {
		modelMap.put("currentPage", "promotion");

		return "vendor/pages/promotion/edit";
	}
	
	
}
