package com.eventura.controllers.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
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
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
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
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
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
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
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
			@RequestParam(defaultValue = "5") int pageSize,@RequestParam(defaultValue = "show_all") String filter) {
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Users> userPage;

	    switch (filter) {
	        case "enabled":
	        	userPage = userService.findAllByDeletedAtISNUL(pageable);
	            break;
	        case "disabled":
	        	userPage = userService.findAllByDeletedAtISNOTNUL(pageable);
	            break;
	        case "show_all":
	        default:
	        	userPage = userService.findAlls(pageable);
	            break;
	    }
		modelMap.put("users", userPage);
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", userPage.getTotalPages());
		model.addAttribute("lastPageIndex", userPage.getTotalPages() - 1);
		model.addAttribute("currentPage", "user");
		model.addAttribute("filter", filter);
		model.addAttribute("pageSize", pageSize);
		return "admin/page/user/customer/list";
	}

	@GetMapping("customer/detail/{id}")
	public String customerDetail(Model model, @PathVariable("id") int id, ModelMap modelMap) {
		modelMap.put("user", userService.findById(id));
		modelMap.put("id",id);
		if(userService.findById(id).getDeletedAt() != null) {			
			modelMap.put("actionType","enable");
		}else {
			
			modelMap.put("actionType","disable");
		}
		model.addAttribute("currentPage", "user");
		return "admin/page/user/customer/detail";
	}
	@PostMapping("customer/edit")
	public String customerEdit(@RequestParam("id") int id, @RequestParam("actionType") String actionType) {
	    Users users = userService.findById(id);

	    if ("enable".equals(actionType)) {
	        users.setDeletedAt(null); // Set to current date for disabling
	    } else if ("disable".equals(actionType)) {
	        users.setDeletedAt(new Date()); // Set to null for enabling
	    } else {
	        // Handle unexpected actionType, maybe log an error or return an error page
	        System.err.println("Invalid actionType received: " + actionType);
	        return "redirect:/admin/customer/detail/" + id + "?error=invalidAction";
	    }

	    if (userService.save(users)) { // Assuming you have a save method in your userService to persist changes
	        System.out.println("Operation successful");
	        return "redirect:/admin/customer/detail/" + id;
	    } else {
	        System.out.println("Operation failed");
	        return "redirect:/admin/customer/detail/" + id + "?error=saveFailed";
	    }
	}
	// ======= USER_VENDOR ========
	@GetMapping("vendor/list")
	public String vendorList(Model model, ModelMap modelMap, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "12") int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Vendors> userPage = vendorService.findAlls(pageable);
		modelMap.put("vendors", userPage);
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", userPage.getTotalPages());
		model.addAttribute("lastPageIndex", userPage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);
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
		modelMap.put("id",id);
		if(vendorService.findById(id).getUsers().getDeletedAt() != null) {			
			modelMap.put("actionType","disable");
		}else {
			
			modelMap.put("actionType","enable");
		}
		model.addAttribute("currentPage", "user");
		return "admin/page/user/vendor/detail";
	}
	
	@PostMapping("vendor/edit")
	public String vendorEdit(@RequestParam("id") int id, @RequestParam("actionType") String actionType) {
	    Users users = userService.findById(id);

	    if ("enable".equals(actionType)) {
	        users.setDeletedAt(new Date()); // Set to current date for disabling
	    } else if ("disable".equals(actionType)) {
	        users.setDeletedAt(null); // Set to null for enabling
	    } else {
	        // Handle unexpected actionType, maybe log an error or return an error page
	        System.err.println("Invalid actionType received: " + actionType);
	        return "redirect:/admin/vendor/detail/" + id + "?error=invalidAction";
	    }

	    if (userService.save(users)) { // Assuming you have a save method in your userService to persist changes
	        System.out.println("Operation successful");
	        return "redirect:/admin/vendor/detail/" + id;
	    } else {
	        System.out.println("Operation failed");
	        return "redirect:/admin/vendor/detail/" + id + "?error=saveFailed";
	    }
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
