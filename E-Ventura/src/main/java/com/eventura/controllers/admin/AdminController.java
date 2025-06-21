package com.eventura.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.eventura.configurations.AccountOAuth2UserServices;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.eventura.services.VendorService;

@Controller
@RequestMapping("admin")
public class AdminController {

	private final AccountOAuth2UserServices accountOAuth2UserServices;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;

	AdminController(AccountOAuth2UserServices accountOAuth2UserServices) {
		this.accountOAuth2UserServices = accountOAuth2UserServices;
	}

	// ======= Login ========
	@GetMapping({ "login" })
	public String login(@RequestParam(value = "error", required = false) String error, ModelMap modelMap,
			@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
		if (error != null) {
			modelMap.put("msg", "Login failed");
		}
		System.out.println(authentication.getName());
		return "admin/page/login/login";
	}

	// ======= Dashboard ========
	@GetMapping({ "dashboard" })
	public String home(Model model) {
		model.addAttribute("currentPage", "dashboard");
		return "admin/page/dashboard/index";
	}

	// ======= CATEGORY ========
	@GetMapping("category/list")
	public String categoryList(Model model, ModelMap map, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
//		map.put("categories", categoryService.findAll());
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<ProductCategories> catePage = categoryService.findAlls(pageable);
		map.put("categories", catePage);
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", catePage.getTotalPages());
		model.addAttribute("lastPageIndex", catePage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);
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

	// ======= PRODUCT ========
	@GetMapping("product/list")
	public String productList(Model model, ModelMap map, @RequestParam(defaultValue = "0") int page) {
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Products> productPage = productService.findAlls(pageable);
		map.put("products", productPage.getContent());
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("lastPageIndex", productPage.getTotalPages() - 1);
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

	// ======= Search PRODUCT ========
	@GetMapping("/product/search-by-category")
	public String searchByCategory(@RequestParam("categoryId") int categoryId, ModelMap map,
			@RequestParam(defaultValue = "0") int page) {
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Products> productPage;

		if (categoryId == 0) {
			// Lấy tất cả sản phẩm, phân trang
			productPage = productService.findAlls(pageable);
			map.put("products", productPage.getContent());
		} else {
			// Lấy sản phẩm theo categoryId, phân trang
			productPage = productService.findByCategoryIdPage(categoryId, pageable);
			map.put("products", productPage.getContent());
		}

		// Thông tin phân trang
		map.put("currentPages", page);
		map.put("totalPages", productPage.getTotalPages());
		map.put("lastPageIndex", productPage.getTotalPages() - 1);

		// Các dữ liệu khác
		map.put("categories", categoryService.findAll());
		map.put("vendors", vendorService.findAll());
		map.put("currentPage", "product");
		map.put("selectedCategoryId", categoryId);

		return "admin/page/product/list";
	}

	@GetMapping("/product/search-by-vendor")
	public String searchByVendor(@RequestParam("vendorId") int vendorId, ModelMap map,
			@RequestParam(defaultValue = "0") int page) {
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Products> productPage;

		if (vendorId == 0) {
			// Lấy tất cả sản phẩm, phân trang
			productPage = productService.findAlls(pageable);
			map.put("products", productPage.getContent());
		} else {
			// Lấy sản phẩm theo categoryId, phân trang
			productPage = productService.findByVendorIdPage(vendorId, pageable);
			map.put("products", productPage.getContent());
		}

		// Thông tin phân trang
		map.put("currentPages", page);
		map.put("totalPages", productPage.getTotalPages());
		map.put("lastPageIndex", productPage.getTotalPages() - 1);

		// Các dữ liệu khác
		map.put("categories", categoryService.findAll());
		map.put("vendors", vendorService.findAll());
		map.put("currentPage", "product");
		map.put("selectedVendorId", vendorId);
		map.put("categories", categoryService.findAll());
		map.put("vendors", vendorService.findAll());
		map.put("currentPage", "product");
		return "admin/page/product/list";
	}

	// ======= Order ========
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

	// ======= USER_CUSTOMER ========
	@GetMapping("customer/list")
	public String customerList(Model model, ModelMap modelMap, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Users> userPage = userService.findAlls(pageable);
		modelMap.put("users", userPage);
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", userPage.getTotalPages());
		model.addAttribute("lastPageIndex", userPage.getTotalPages() - 1);
		model.addAttribute("currentPage", "user");
		model.addAttribute("pageSize", pageSize);
		return "admin/page/user/customer/list";
	}

	@GetMapping("customer/detail")
	public String customerDetail(Model model) {

		model.addAttribute("currentPage", "user");
		return "admin/page/user/customer/detail";
	}

	// ======= USER_VENDOR ========
	@GetMapping("vendor/list")
	public String vendorList(Model model, ModelMap modelMap, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "12") int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Vendors> userPage = vendorService.findAlls(pageable);
		modelMap.put("vendors", userPage);
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", userPage.getTotalPages());
		model.addAttribute("lastPageIndex", userPage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);
//		modelMap.put("vendors", vendorService.findAll());
		model.addAttribute("currentPage", "user");
		return "admin/page/user/vendor/list";
	}

	@GetMapping("vendor/detail/{id}")
	public String vendorDetail(Model model, @PathVariable("id") int id, ModelMap modelMap) {
		modelMap.put("vendor", vendorService.findById(id));
		modelMap.put("income", vendorService.sumByVendorId(id));
		modelMap.put("totalSell", vendorService.countByVendorId(id));
		modelMap.put("vendorAddresses", userService.findAddressUser(id));
		modelMap.put("products", productService.findByVendorId(id));
		model.addAttribute("currentPage", "user");
		return "admin/page/user/vendor/detail";
	}

	// ======= CATEGORY ========
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
