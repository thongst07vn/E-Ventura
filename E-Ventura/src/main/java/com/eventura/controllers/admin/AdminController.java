package com.eventura.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.VendorService;

@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private ProductService productService;
	//======= Login ========	
	@GetMapping({"login"})
	public String login(@RequestParam(value = "error", required =false)String error,ModelMap modelMap) {
		if(error!=null) {
			modelMap.put("msg", "Login failed");
		}
		return "admin/page/login/login";
	}
	//======= Dashboard ========
	@GetMapping({"dashboard"})
	public String home(Model model) {
        model.addAttribute("currentPage", "dashboard");
		return "admin/page/dashboard/index";
	}
	
	//======= CATEGORY ========	
	@GetMapping("category/list")
	public String categoryList(Model model, ModelMap map) {
		map.put("categories", categoryService.findAll());
		model.addAttribute("currentPage", "category");
		return "admin/page/category/list";
	}
	
	@GetMapping("category/add")
	public String addCategory(Model model) {
		model.addAttribute("currentPage", "category");
		return "admin/page/category/add";
	}
	@GetMapping("category/edit")
	public String editCategory(Model model) {
		model.addAttribute("currentPage", "category");
		return "admin/page/category/edit";
	}
	
	//======= PRODUCT ========
	@GetMapping("product/list")
	public String productList(Model model, ModelMap map) {
//		map.put("products", productService.findAll());
		map.put("products", productService.findByCategoryId(1));
		map.put("categories", categoryService.findAll());
		map.put("vendors", vendorService.findAll());
		model.addAttribute("currentPage", "product");
		return "admin/page/product/list";
	}
	
	@GetMapping("product/add")
	public String productAdd(Model model) {
		model.addAttribute("currentPage", "product");
		return "admin/page/product/add";
	}
	
	@GetMapping("product/edit")
	public String productEdit(Model model) {
		model.addAttribute("currentPage", "product");
		return "admin/page/product/edit";
	}
	//======= Search PRODUCT ========
	 @GetMapping("/product/search-by-category")
    public String searchByCategory(@RequestParam("categoryId") int categoryId,ModelMap map) {
        map.put("categories", categoryService.findAll());
		map.put("vendors", vendorService.findAll());
		map.put("currentPage", "product");
        if (categoryId == 0) {
        	map.put("products", productService.findAll());
        }else {
        	map.put("products", productService.findByCategoryId(categoryId));
        }         
        map.put("selectedCategoryId", categoryId);
        return "admin/page/product/list";
    }
	 @GetMapping("/product/search-by-vendor")
	 public String searchByVendor(@RequestParam("vendorId") int vendorId,ModelMap map) {
		 map.put("categories", categoryService.findAll());
		 map.put("vendors", vendorService.findAll());
		 map.put("currentPage", "product");
		 if (vendorId == 0) {
			 map.put("products", productService.findAll());
		 }else {
			 map.put("products", productService.findByVendorId(vendorId));
		 }         
		 map.put("selectedVendorId", vendorId);
		 return "admin/page/product/list";
	 }
	
	//======= Order ========
	@GetMapping("order/list")
	public String orderList(Model model) {
		model.addAttribute("currentPage", "order");
		return "admin/page/order/list";
	}
	@GetMapping("order/detail")
	public String orderDetail(Model model) {
		model.addAttribute("currentPage", "order");
		return "admin/page/order/detail";
	}
	
	//======= USER_CUSTOMER ========
	@GetMapping("customer/list")
	public String customerList(Model model) {
		model.addAttribute("currentPage", "user");
		return "admin/page/user/customer/list";
	}
	@GetMapping("customer/detail")
	public String customerDetail(Model model) {
		model.addAttribute("currentPage", "user");
		return "admin/page/user/customer/detail";
	}
	
	//======= USER_VENDOR ========
	@GetMapping("vendor/list")
	public String vendorList(Model model) {
		model.addAttribute("currentPage", "user");
		return "admin/page/user/vendor/list";
	}
	@GetMapping("vendor/detail")
	public String vendorDetail(Model model) {
		model.addAttribute("currentPage", "user");
		return "admin/page/user/vendor/detail";
	}
	
	//======= CATEGORY ========	
	@GetMapping("coupon/list")
	public String couponList(Model model) {
		model.addAttribute("currentPage", "coupon");
		return "admin/page/coupon/list";
	}
	
	@GetMapping("coupon/add")
	public String addCoupon(Model model) {
		model.addAttribute("currentPage", "coupon");
		return "admin/page/coupon/add";
	}
	@GetMapping("coupon/edit")
	public String editCoupon(Model model) {
		model.addAttribute("currentPage", "coupon");
		return "admin/page/coupon/edit";
	}
}
