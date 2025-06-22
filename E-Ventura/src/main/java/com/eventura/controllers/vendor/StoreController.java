package com.eventura.controllers.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.eventura.services.VendorService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorStoreController")
@RequestMapping("vendor/store")
public class StoreController  {
	
	@Autowired
	private UserService userService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private ProductService productService;
	
	/*===================== STORE =====================*/
	@GetMapping("edit")
	public String storeEdit(HttpSession session,ModelMap modelMap) {
		modelMap.put("currentPage", "store");
		
		int id = (Integer) session.getAttribute("vendorId");
		modelMap.put("user", userService.findById(id));
		modelMap.put("vendor", vendorService.findById(id));

		return "vendor/pages/store/edit";
	}
	
	@GetMapping("view")
	public String storeView(HttpSession session, ModelMap modelMap) {
		modelMap.put("currentPage", "store");
		
		int vendorId = (Integer) session.getAttribute("vendorId");
		modelMap.put("user", userService.findById(vendorId));
		modelMap.put("vendor", vendorService.findById(vendorId));
		modelMap.put("products", productService.findByVendorId(vendorId));
		modelMap.put("revenue", vendorService.sumByVendorId(vendorId));
		

		return "vendor/pages/store/view";
	}
	
	@GetMapping("review")
	public String storeReview(ModelMap modelMap) {
		modelMap.put("currentPage", "review");

		return "vendor/pages/store/review";
	}
	
	
	
}
