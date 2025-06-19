package com.eventura.controllers.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;

@Controller
@RequestMapping({"product"})

public class ProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping({"productdetails"})
	public String prouctDetails() {
		return "customer/pages/product/productdetails";
	}
	@GetMapping({"findbycategory/{id}"})
	public String findByCategory(@PathVariable("id") int id,ModelMap modelMap) {
		List<Products> products = productService.findByCategoryId(id);
		List<ProductCategories> categories = categoryService.findAll();
		modelMap.put("products", products);
		modelMap.put("categories", categories);
		return "customer/pages/home/shopgrid";
	}

	@GetMapping({"search"})
	public String searchByKeyword(@RequestParam("keyword") String keyword,ModelMap modelMap) {
		List<Products> products = productService.findByKeyword(keyword);
		List<ProductCategories> categories = categoryService.findAll();
		modelMap.put("products", products);
		modelMap.put("categories", categories);
		return "customer/pages/home/shopgrid";
	}
	
	@GetMapping("/searchbycategory")
    public String searchByVendor(@RequestParam("categoryId") int categoryId,ModelMap map) {
        map.put("categories", categoryService.findAll());
        if (categoryId == 0) {
            map.put("products", productService.findAll());
        }else {
            map.put("products", productService.findByCategoryId(categoryId));
        }
        map.put("selectedCategoryId", categoryId);
        return "customer/pages/home/shopgrid";
    }

}
