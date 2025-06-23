package com.eventura.controllers.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventura.entities.Products;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.ProductVariantService;
import com.eventura.services.VendorProductCategoryService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorProductController")
@RequestMapping("vendor/product")
public class ProductController  {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductVariantService productVariantService;
	@Autowired
	private VendorProductCategoryService vendorProductCategoryService;
	
	/*===================== PRODUCT =====================*/
	@GetMapping("list")
	public String productList(ModelMap modelMap, HttpSession session, @RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "product");
		Integer vendorId = (Integer) session.getAttribute("vendorId");

		int pageSize = 3;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage = productService.findByVendorIdPage(vendorId,pageable);

		modelMap.put("products", productPage.getContent());
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", productPage.getTotalPages());
		modelMap.put("lastPageIndex", productPage.getTotalPages() - 1);
		
		modelMap.put("vendorCategories", vendorProductCategoryService.findByVendorId(vendorId));
		return "vendor/pages/product/list";
	}
	
	@GetMapping("searchByVendorCategory")
	public String searchByVendorCategory(ModelMap modelMap, HttpSession session,
										@RequestParam("categoryId") int categoryId, 
										@RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "product");
		Integer vendorId = (Integer) session.getAttribute("vendorId");

		int pageSize = 3;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage;
		
		if(categoryId == 0) {
			productPage = productService.findByVendorIdPage(vendorId, pageable);
			modelMap.put("products", productPage.getContent());
		}else {
			productPage = productService.findByVendorCategoryPage(vendorId, categoryId, pageable);
			modelMap.put("products", productPage.getContent());
		}
		
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", productPage.getTotalPages());
		modelMap.put("lastPageIndex", productPage.getTotalPages() - 1);
		
		modelMap.put("selectedCategoryId", categoryId);
		
		modelMap.put("vendorCategories", vendorProductCategoryService.findByVendorId(vendorId));
		return "vendor/pages/product/list";
	}
	
	@GetMapping("details/{id}")
	public String productDetail(@PathVariable("id") int id, ModelMap modelMap) {
		modelMap.put("currentPage", "product");

		return "vendor/pages/product/details";
	}
	
	@GetMapping("add")
	public String productAdd(ModelMap modelMap) {
		modelMap.put("currentPage", "product");
		modelMap.put("categories", categoryService.findAll());

		return "vendor/pages/product/add";
	}
	
	@GetMapping("edit/{id}")
	public String productEdit(@PathVariable("id") int id, ModelMap modelMap) {
		modelMap.put("currentPage", "product");

		modelMap.put("productVariants", productVariantService.findByProductId(id));
		modelMap.put("product", productService.findById(id));
		modelMap.put("categories", categoryService.findAll());

		return "vendor/pages/product/edit";
	}
	
	@GetMapping("delete/{id}")
	public String productDelete(@PathVariable("id") int id, ModelMap modelMap) {
		modelMap.put("currentPage", "product");

		return "vendor/pages/product/delete";
	}
	
	@GetMapping("review")
	public String productReview(ModelMap modelMap) {
		modelMap.put("currentPage", "review");

		return "vendor/pages/product/review";
	}
	
	@GetMapping("reviewDetail")
	public String productReviewDetail(ModelMap modelMap) {
		modelMap.put("currentPage", "review");

		return "vendor/pages/product/reviewDetail";
	}
	
}
