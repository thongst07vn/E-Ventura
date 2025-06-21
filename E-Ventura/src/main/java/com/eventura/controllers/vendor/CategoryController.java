package com.eventura.controllers.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.VendorProductCategoryService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorCategoryController")
@RequestMapping("vendor/category")
public class CategoryController  {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private VendorProductCategoryService vendorProductCategoryService;
	@Autowired
	private CategoryService categoryService;
	
	
	/*===================== CATEGORY =====================*/
	@GetMapping("list")
	public String categoryList(HttpSession session, ModelMap modelMap ) {
		modelMap.put("currentPage", "category");
		
		int id = (Integer) session.getAttribute("vendorId");
		modelMap.put("vendorProductCategories", vendorProductCategoryService.findByVendorId(id));

		return "vendor/pages/category/list";
	}
	
	@GetMapping("add")
	public String categoryAdd(ModelMap modelMap) {
		modelMap.put("currentPage", "category");

		return "vendor/pages/category/add";
	}
	
	@GetMapping("edit/{id}")
	public String categoryEdit(@PathVariable("id") int id, ModelMap modelMap) {
		modelMap.put("currentPage", "category");
		modelMap.put("category", categoryService.findById(id));

		return "vendor/pages/category/edit";
	}

	
}
