package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("vendorStoreController")
@RequestMapping("vendor/store")
public class StoreController  {
	
	/*===================== STORE =====================*/
	@GetMapping("edit")
	public String storeEdit() {
		return "vendor/pages/store/edit";
	}
	
	@GetMapping("view")
	public String storeView() {
		return "vendor/pages/store/view";
	}
	
	@GetMapping("review")
	public String storeReview() {
		return "vendor/pages/store/review";
	}
	
	
	
}
