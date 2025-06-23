package com.eventura.controllers.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	
	@GetMapping({"productdetails/{id}"})
	public String prouctDetails(@PathVariable("id") int id,ModelMap modelMap) {
		Products product = productService.findById(id);
		modelMap.put("product", product);
		return "customer/pages/product/productdetails";
	}
	@GetMapping({"findbycategory/{id}"})
	public String findByCategory(@PathVariable("id") int id,ModelMap modelMap,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		
		Page<Products> products = productService.findByCategoryIdPage(id,pageable);
		List<ProductCategories> categories = categoryService.findAll();
		modelMap.put("products", products);
		modelMap.put("categories", categories);
		modelMap.put("categoryId", id);
		
		modelMap.addAttribute("currentPages", page);
		modelMap.addAttribute("totalPages", products.getTotalPages());
		modelMap.addAttribute("lastPageIndex", products.getTotalPages() - 1);
		modelMap.addAttribute("pageSize", pageSize);
		modelMap.addAttribute("currentPage", "category");
		return "customer/pages/home/shopgrid";
	}

	@GetMapping({"search"})
	public String searchByKeyword(@RequestParam("keyword") String keyword,ModelMap modelMap) {
		List<Products> products = productService.findByKeyword(keyword);
		List<ProductCategories> categories = categoryService.findAll();
		modelMap.put("products", products);
		modelMap.put("categories", categories);
		modelMap.put("keyword", keyword);
		return "customer/pages/home/shopgrid";
	}
	

}
