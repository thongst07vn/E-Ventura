package com.eventura.controllers.vendor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.VendorProductCategory;
import com.eventura.helpers.FileHelper;
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
	public String categoryList(HttpSession session, ModelMap modelMap, 
							  @RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "category");
		int vendorId = (Integer) session.getAttribute("vendorId");
		
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<VendorProductCategory> vendorCategoriesPage = vendorProductCategoryService.findByVendorIdPage(vendorId, pageable);
		
		modelMap.put("vendorProductCategories", vendorCategoriesPage);
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", vendorCategoriesPage.getTotalPages());
		modelMap.put("lastPageIndex", vendorCategoriesPage.getTotalPages() - 1);
		
		/* ADD */
		modelMap.put("vendorProductCategory", new VendorProductCategory());


		return "vendor/pages/category/list";
	}
	
	@GetMapping("searchByKeyword")
	public String searchByKeyword(HttpSession session, ModelMap modelMap, 
								@RequestParam("keyword") String keyword,
								@RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "category");
		int vendorId = (Integer) session.getAttribute("vendorId");
		
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<VendorProductCategory> vendorCategoriesPage = vendorProductCategoryService.findByKeyword(vendorId, keyword, pageable);
		
		modelMap.put("vendorProductCategories", vendorCategoriesPage);
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", vendorCategoriesPage.getTotalPages());
		modelMap.put("lastPageIndex", vendorCategoriesPage.getTotalPages() - 1);
		modelMap.put("selectedKeyword", keyword);

		return "vendor/pages/category/list";
	}
	
	
	@GetMapping("edit/{id}")
	public String categoryEdit(@PathVariable("id") int id, ModelMap modelMap) {
		modelMap.put("currentPage", "category");
		modelMap.put("category", categoryService.findById(id));

		return "vendor/pages/category/edit";
	}

	
}
