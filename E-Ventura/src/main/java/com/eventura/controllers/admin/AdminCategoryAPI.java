package com.eventura.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventura.dtos.CategoryDTO;
import com.eventura.dtos.UserAddressDTO;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.UserAddress;
import com.eventura.services.CategoryService;

@RestController
@RequestMapping("admin/api")
public class AdminCategoryAPI {
	@Autowired
	private CategoryService categoryService; 
	
	@GetMapping({"category"})
	public CategoryDTO GetEditCategory(@RequestParam("editCategoryId") int editCategoryId) {
		ProductCategories category = categoryService.findById(editCategoryId);
		CategoryDTO categoryDTO = new CategoryDTO(category);
		return categoryDTO; 		    	
	}
}
