package com.eventura.controllers.vendor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("vendorCategoryController")
@RequestMapping("vendor/category")
public class CategoryController  {
	

	/*===================== CATEGORY =====================*/
	@GetMapping("list")
	public String categoryList() {
		return "vendor/pages/category/list";
	}
	
	@GetMapping("add")
	public String categoryAdd() {
		return "vendor/pages/category/add";
	}
	
	@GetMapping("edit")
	public String categoryEdit() {
		return "vendor/pages/category/edit";
	}

	
}
