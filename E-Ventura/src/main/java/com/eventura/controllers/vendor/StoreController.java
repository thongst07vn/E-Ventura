package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("vendorStoreController")
@RequestMapping("vendor/store")
public class StoreController  {
	
	/*===================== STORE =====================*/
	@GetMapping("edit")
	public String storeEdit(ModelMap modelMap) {
		modelMap.put("currentPage", "store");

		return "vendor/pages/store/edit";
	}
	
	@GetMapping("view")
	public String storeView(ModelMap modelMap) {
		modelMap.put("currentPage", "store");

		return "vendor/pages/store/view";
	}
	
	@GetMapping("review")
	public String storeReview(ModelMap modelMap) {
		modelMap.put("currentPage", "review");

		return "vendor/pages/store/review";
	}
	
	
	
}
