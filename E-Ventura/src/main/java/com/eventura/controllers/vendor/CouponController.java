package com.eventura.controllers.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.eventura.services.VendorProductCategoryService;
import com.eventura.services.VendorService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorCouponController")
@RequestMapping("vendor/coupon")
public class CouponController  {
	
	/*===================== Coupon =====================*/
	@GetMapping("list")
	public String couponList(ModelMap modelMap) {
		modelMap.put("currentPage", "promotion");

		return "vendor/pages/coupon/list";
	}
	
	@GetMapping("add")
	public String couponAdd(ModelMap modelMap) {
		modelMap.put("currentPage", "promotion");

		return "vendor/pages/coupon/add";
	}
	
	@GetMapping("edit")
	public String couponEdit(ModelMap modelMap) {
		modelMap.put("currentPage", "promotion");

		return "vendor/pages/coupon/edit";
	}
}
