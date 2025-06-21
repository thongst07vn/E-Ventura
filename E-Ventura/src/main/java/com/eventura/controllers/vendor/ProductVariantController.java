package com.eventura.controllers.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.ProductVariantService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorProductVariantController")
@RequestMapping("vendor/productVariant")
public class ProductVariantController  {
	
	@Autowired
	private ProductVariantService productVariantService;
	
	/*===================== PRODUCT VARIANT =====================*/
	@GetMapping("edit/{id}")
	public String edit(@PathVariable("id") int id, ModelMap modelMap) {
		modelMap.put("currentPage", "product");

		modelMap.put("productVariant", productVariantService.findById(id));
		
		
		return "vendor/pages/productVariant/edit";
	}
	
}
