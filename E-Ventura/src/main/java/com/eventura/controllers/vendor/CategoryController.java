package com.eventura.controllers.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorCategoryController")
@RequestMapping("vendor/category")
public class CategoryController  {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	
	/*===================== CATEGORY =====================*/
	@GetMapping("list")
	public String categoryList(HttpSession session, ModelMap modelMap ) {
		modelMap.put("currentPage", "category");

		return "vendor/pages/category/list";
	}
	
	@GetMapping("add")
	public String categoryAdd(ModelMap modelMap) {
		modelMap.put("currentPage", "category");

		return "vendor/pages/category/add";
	}
	
	@GetMapping("edit")
	public String categoryEdit(ModelMap modelMap) {
		modelMap.put("currentPage", "category");

		return "vendor/pages/category/edit";
	}

	
}
