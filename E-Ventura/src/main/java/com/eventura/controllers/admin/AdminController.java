package com.eventura.controllers.admin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventura.configurations.AccountOAuth2UserServices;
import com.eventura.dtos.ProductDTO;
import com.eventura.entities.OrderItems;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;
import com.eventura.services.CategoryService;
import com.eventura.services.CommissionsService;
import com.eventura.services.OrderItemService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.eventura.services.VendorProductCategoryService;
import com.eventura.services.VendorService;
import com.example.demo.helpers.FileHelper;


@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;
	@Autowired
	private VendorProductCategoryService vendorProductCategoryService;
	@Autowired
	private CommissionsService commissionsService;
	@Autowired
	private OrderItemService orderItemService;
	AdminController(AccountOAuth2UserServices accountOAuth2UserServices) {
	}

	// ======= Login ========
	@GetMapping({ "login" })
	public String login(@RequestParam(value = "error", required = false) String error, ModelMap modelMap,
			@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
		if (error != null) {
			modelMap.put("msg", error);
		}
		System.out.println(authentication.getName());
		return "admin/page/login/login";
	}

	// ======= Dashboard ========
	@GetMapping({ "dashboard" })
	public String home(Model model, ModelMap modelMap) {
		modelMap.put("countActivityLogPC", userService.countActivityLogPC());
		modelMap.put("countActivityLogPhone", userService.countActivityLogPhone());
		modelMap.put("countCommission", commissionsService.countCommission());
		modelMap.put("sumCommission",commissionsService.sumCommission());
		
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
		map.put("categories", catePage.getContent().stream()
	            .filter(category -> category.getDeletedAt() == null && category.getId() != 1)
	            .collect(Collectors.toList()));
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", catePage.getTotalPages());
		model.addAttribute("lastPageIndex", catePage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);
		map.put("category",new ProductCategories());
		model.addAttribute("currentPage", "category");
		return "admin/page/category/list";
	}
	@GetMapping("category/search-by-keyword")
	public String categorySearchByKeyword(Model model, ModelMap map, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int pageSize, @RequestParam("keyword") String keyword) {
//		map.put("categories", categoryService.findAll());
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<ProductCategories> catePage = categoryService.findByKeywordPage(keyword,pageable);
		map.put("categories", catePage.getContent().stream()
	            .filter(category -> category.getDeletedAt() == null && category.getId() != 1)
	            .collect(Collectors.toList()));
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", catePage.getTotalPages());
		model.addAttribute("lastPageIndex", catePage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);
		map.put("category",new ProductCategories());
		map.put("keyword", keyword);
		model.addAttribute("currentPage", "category");
		return "admin/page/category/list";
	}

	@PostMapping("category/delete")
	public String categoryDelete(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
		ProductCategories productCategories = categoryService.findById(id);
		ProductCategories productCategories1 = categoryService.findById(1);
		productCategories.setDeletedAt(new Date());
	    for(Products product : productService.findByCategoryId(productCategories.getId())) {
	    	product.setProductCategories(productCategories1);
	    }
		if (categoryService.save(productCategories)) {
			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Category "+productCategories.getName()+" deleted successfully!");
			return "redirect:/admin/category/list";
		} else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Failed to delete "+productCategories.getName()+"!");
			return "redirect:/admin/category/list";
		}
	}
	@PostMapping("category/add")
	public String categoryAdd(@ModelAttribute("category") ProductCategories category, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		category.setCreatedAt(new Date());
		category.setUpdatedAt(new Date());
		 // 2. Handle file upload for avatar
	    if (file != null && !file.isEmpty()) {
	        try {
	            String fileName = FileHelper.generateFileName(file.getOriginalFilename());
	            // Using getFile() might fail if running from a JAR; consider using getResourceAsStream() and Files.copy()
	            // Or get a path outside of the JAR for persistent storage
	            File imagesFolder = new ClassPathResource("static/assets/imgs/others/").getFile();
	            Path path = Paths.get(imagesFolder.getAbsolutePath() + File.separator + fileName);
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            category.setPhoto(fileName);
	        } catch (Exception e) {
	            e.printStackTrace();
	            // If file upload fails, fall back to existing avatar if available
	            category.setPhoto("noimg.jpg");	
	            // Optionally, rethrow a custom exception or add a logging message
	        }
	    }else {
	    	 category.setPhoto("noimg.jpg");
		}
		if (categoryService.save(category)) {
			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Category "+category.getName()+" add successfully!");
			return "redirect:/admin/category/list";
		} else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Failed to add "+category.getName()+"!");
			return "redirect:/admin/category/list";
		}
	}
	@PostMapping("category/edit")
	public String categoryEdit(@ModelAttribute("category") ProductCategories category, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		category.setCreatedAt(new Date());
		category.setUpdatedAt(new Date());
		// 2. Handle file upload for avatar
		if (file != null && !file.isEmpty()) {
			try {
				String fileName = FileHelper.generateFileName(file.getOriginalFilename());
				if(category.getPhoto() != fileName) {					
					// Using getFile() might fail if running from a JAR; consider using getResourceAsStream() and Files.copy()
					// Or get a path outside of the JAR for persistent storage
					File imagesFolder = new ClassPathResource("static/assets/imgs/others/").getFile();
					Path path = Paths.get(imagesFolder.getAbsolutePath() + File.separator + fileName);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					category.setPhoto(fileName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (categoryService.save(category)) {
			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Category edit successfully!");
			return "redirect:/admin/category/list";
		} else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Failed to edit!");
			return "redirect:/admin/category/list";
		}
		
	}

	// ======= PRODUCT ========
	@GetMapping("product/list")
	public String productList(Model model, ModelMap map, @RequestParam(defaultValue = "0") int page) {
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage = productService.findAlls(pageable);
		List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

		// Lặp qua productPage và thêm các ProductDTO vào List
		for (Products product : productPage) {
			if (product.getDeletedAt() == null) {
				if (!productService.findProductReview(product.getId()).isEmpty()) {
					productDTOList.add(new ProductDTO(product, productService.avgProductReview(product.getId())));
				} else {
					productDTOList.add(new ProductDTO(product, 0));
				}
			}

		}
		Page<ProductDTO> productDTOPage = new PageImpl<ProductDTO>(productDTOList, productPage.getPageable(),
				productPage.getTotalElements()
		);
		map.put("products", productDTOPage.getContent());
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("lastPageIndex", productPage.getTotalPages() - 1);

		map.put("categories", categoryService.findAll());
		map.put("vendors", vendorService.findAll());
		map.put("minRating", 0);
		model.addAttribute("currentPage", "product");
		return "admin/page/product/list";
	}

	@PostMapping("product/delete")
	public String productEdit(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
		Products product = productService.findById(id);
		product.setDeletedAt(new Date());
		if (productService.save(product)) {
			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
			return "redirect:/admin/product/list";
		} else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Failed to delete product!");
			return "redirect:/admin/product/list";
		}
	}

	@GetMapping("product/detail/{id}")
	public String productdetail(ModelMap modelMap, @PathVariable("id") int id) {
		modelMap.put("product", productService.findById(id));
		modelMap.put("vendorAddresses", userService.findAddressUser(productService.findById(id).getVendors().getId()));
		modelMap.put("productReviews", productService.findProductReview(id));

		if (!productService.findProductReview(id).isEmpty()) {
			modelMap.put("countProductReview", productService.countProductReview(id));
			modelMap.put("avgProductReview", productService.avgProductReview(id));
			modelMap.put("widthProduct", Math.min(productService.avgProductReview(id), 5) * 20);
		} else {
			modelMap.put("countProductReview", 0);
			modelMap.put("avgProductReview", 0);
			modelMap.put("widthProduct", Math.min(0, 5) * 20);

		}
		if (!vendorService.findVendorReview(productService.findById(id).getVendors().getId()).isEmpty()) {
			modelMap.put("countVendorReview",
					vendorService.countVendorReview(productService.findById(id).getVendors().getId()));
			modelMap.put("avgVendorReview",
					vendorService.avgVendorReview(productService.findById(id).getVendors().getId()));
			modelMap.put("widthVendor",
					Math.min(vendorService.avgVendorReview(productService.findById(id).getVendors().getId()), 5) * 20);
		} else {
			modelMap.put("countVendorReview", 0);
			modelMap.put("avgVendorReview", 0);
			modelMap.put("widthVendor", Math.min(0, 5) * 20);
		}
		modelMap.put("currentPage", "product");
		return "admin/page/product/detail";
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
			List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

			// Lặp qua productPage và thêm các ProductDTO vào List
			for (Products product : productPage) {
				if (product.getDeletedAt() == null) {

					if (!productService.findProductReview(product.getId()).isEmpty()) {
						productDTOList.add(new ProductDTO(product, productService.avgProductReview(product.getId())));
					} else {
						productDTOList.add(new ProductDTO(product, 0));
					}
				}

			}
			// Tạo một PageImpl mới từ List các ProductDTO, Pageable và tổng số phần tử
			Page<ProductDTO> productDTOPage = new PageImpl<ProductDTO>(productDTOList, productPage.getPageable(),
					productPage.getTotalElements() // Lấy tổng số phần tử từ productPage
			);
			map.put("products", productDTOPage.getContent());
//			map.put("products", productPage.getContent());
		} else {
			// Lấy sản phẩm theo categoryId, phân trang
			productPage = productService.findByCategoryIdPage(categoryId, pageable);
			List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

			// Lặp qua productPage và thêm các ProductDTO vào List
			for (Products product : productPage) {
				if (product.getDeletedAt() == null) {
					if (!productService.findProductReview(product.getId()).isEmpty()) {
						productDTOList.add(new ProductDTO(product, productService.avgProductReview(product.getId())));
					} else {
						productDTOList.add(new ProductDTO(product, 0));
					}
				}

			}
			productDTOList.sort(Comparator.comparing(ProductDTO::getRating, Comparator.reverseOrder()));
			// Tạo một PageImpl mới từ List các ProductDTO, Pageable và tổng số phần tử
			Page<ProductDTO> productDTOPage = new PageImpl<ProductDTO>(productDTOList, productPage.getPageable(),
					productPage.getTotalElements() // Lấy tổng số phần tử từ productPage
			);
			map.put("products", productDTOPage.getContent());
//			map.put("products", productPage.getContent());
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
			List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

			// Lặp qua productPage và thêm các ProductDTO vào List
			for (Products product : productPage) {
				if (product.getDeletedAt() == null) {
					if (!productService.findProductReview(product.getId()).isEmpty()) {
						productDTOList.add(new ProductDTO(product, productService.avgProductReview(product.getId())));
					} else {
						productDTOList.add(new ProductDTO(product, 0));
					}
				}
			}
			// Tạo một PageImpl mới từ List các ProductDTO, Pageable và tổng số phần tử
			Page<ProductDTO> productDTOPage = new PageImpl<ProductDTO>(productDTOList, productPage.getPageable(),
					productPage.getTotalElements() // Lấy tổng số phần tử từ productPage
			);
			map.put("products", productDTOPage.getContent());
//			map.put("products", productPage.getContent());
		} else {
			// Lấy sản phẩm theo categoryId, phân trang
			productPage = productService.findByVendorIdPage(vendorId, pageable);
			List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

			// Lặp qua productPage và thêm các ProductDTO vào List
			for (Products product : productPage) {
				 if (product.getDeletedAt() == null) {					 
					 if (!productService.findProductReview(product.getId()).isEmpty()) {
						 productDTOList.add(new ProductDTO(product, productService.avgProductReview(product.getId())));
					 } else {
						 productDTOList.add(new ProductDTO(product, 0));
					 }
				 }
			}
			productDTOList.sort(Comparator.comparing(ProductDTO::getRating, Comparator.reverseOrder()));
			// Tạo một PageImpl mới từ List các ProductDTO, Pageable và tổng số phần tử
			Page<ProductDTO> productDTOPage = new PageImpl<ProductDTO>(productDTOList, productPage.getPageable(),
					productPage.getTotalElements() // Lấy tổng số phần tử từ productPage
			);
			map.put("products", productDTOPage.getContent());
//			map.put("products", productPage.getContent());
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
	@GetMapping("product/search-by-keyword")
	public String productBykeyword(Model model, ModelMap map, @RequestParam(defaultValue = "0") int page,
			@RequestParam("keyword") String keyword) {
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage = productService.findByKeywordPage(keyword,pageable);
		List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

		for (Products product : productPage) {
			if (product.getDeletedAt() == null) {
				if (!productService.findProductReview(product.getId()).isEmpty()) {
					productDTOList.add(new ProductDTO(product, productService.avgProductReview(product.getId())));
				} else {
					productDTOList.add(new ProductDTO(product, 0));
				}
			}

		}
		Page<ProductDTO> productDTOPage = new PageImpl<ProductDTO>(productDTOList, productPage.getPageable(),
				productPage.getTotalElements()
		);
		map.put("products", productDTOPage.getContent());
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("lastPageIndex", productPage.getTotalPages() - 1);

		map.put("categories", categoryService.findAll());
		map.put("vendors", vendorService.findAll());
		map.put("minRating", 0);
		map.put("keyword", keyword);
		model.addAttribute("currentPage", "product");
		return "admin/page/product/list";
	}
	
	// ======= Order ========
	@GetMapping("order/list")
	public String orderList(Model model, ModelMap modelMap, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "show_all") String filter) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OrderItems> orderPage = orderItemService.findAlls(pageable);

		modelMap.put("orders", orderPage);
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", orderPage.getTotalPages());
		model.addAttribute("lastPageIndex", orderPage.getTotalPages() - 1);
		model.addAttribute("filter", filter);
		model.addAttribute("pageSize", pageSize);
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
			@RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "show_all") String filter) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
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

	@GetMapping("user/search-by-keyword")
	public String userSearchByKeyword(Model model, ModelMap modelMap, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "show_all") String filter,
			@RequestParam("keyword") String keyword) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Users> userPage;

		switch (filter) {
		case "enabled":
			userPage = userService.findUsersWithRoleId3ByDeletedAtISNULLByKeyword(keyword, pageable);
			break;
		case "disabled":
			userPage = userService.findUsersWithRoleId3ByDeletedAtNOTNULLByKeyword(keyword, pageable);
			break;
		case "show_all":
		default:
			userPage = userService.findUsersWithRoleId3ByKeyword(keyword, pageable);
			break;
		}
		modelMap.put("users", userPage);
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", userPage.getTotalPages());
		model.addAttribute("lastPageIndex", userPage.getTotalPages() - 1);
		model.addAttribute("currentPage", "user");
		model.addAttribute("filter", filter);
		model.addAttribute("pageSize", pageSize);
		modelMap.put("keyword", keyword);
		return "admin/page/user/customer/list";
	}

	@GetMapping("customer/detail/{id}")
	public String customerDetail(Model model, @PathVariable("id") int id, ModelMap modelMap) {
		modelMap.put("user", userService.findById(id));
		modelMap.put("id", id);
		if (userService.findById(id).getDeletedAt() != null) {
			modelMap.put("actionType", "enable");
		} else {

			modelMap.put("actionType", "disable");
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
			@RequestParam(defaultValue = "12") int pageSize, @RequestParam(defaultValue = "show_all") String filter) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Vendors> userPage;
		switch (filter) {
		case "enabled":
			userPage = vendorService.findAllVendorsByDeletedAtISNUL(pageable);
			break;
		case "disabled":
			userPage = vendorService.findAllVendorsByDeletedAtISNOTNUL(pageable);
			break;
		case "show_all":
		default:
			userPage = vendorService.findAlls(pageable);
			break;
		}
		modelMap.put("vendors", userPage.getContent());
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", userPage.getTotalPages());
		model.addAttribute("lastPageIndex", userPage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("filter", filter);
		model.addAttribute("currentPage", "user");
		return "admin/page/user/vendor/list";
	}

	@GetMapping("vendor/search-by-keyword")
	public String vendorSearchByKeyword(Model model, ModelMap modelMap, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "12") int pageSize, @RequestParam("keyword") String keyword, @RequestParam(defaultValue = "show_all") String filter) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Vendors> userPage;
		
		switch (filter) {
		case "enabled":
			userPage = vendorService.findVendorsByDeletedAtISNULLByKeyword(keyword,pageable);
			break;
		case "disabled":
			userPage = vendorService.findVendorsByDeletedAtNOTNULLByKeyword(keyword,pageable);
			break;
		case "show_all":
		default:
			userPage = vendorService.findByKeywordPage(keyword, pageable);
			break;
		}
		modelMap.put("vendors", userPage.getContent());
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", userPage.getTotalPages());
		model.addAttribute("lastPageIndex", userPage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("filter", filter);
		modelMap.put("keyword", keyword);
		model.addAttribute("currentPage", "user");
		return "admin/page/user/vendor/list";
	}

	@GetMapping("vendor/detail/{id}")
	public String vendorDetail(Model model, @PathVariable("id") int id, ModelMap modelMap,
			@RequestParam(defaultValue = "0") int page) {
		int pageSize = 6;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage = productService.findByVendorIdPage(id, pageable);
		// Khởi tạo một List để chứa các ProductDTO
		List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

		// Lặp qua productPage và thêm các ProductDTO vào List
		for (Products product : productPage) {
			if (product.getDeletedAt() == null) {
				if (!productService.findProductReview(product.getId()).isEmpty()) {
					productDTOList.add(new ProductDTO(product, productService.avgProductReview(product.getId())));
				} else {
					productDTOList.add(new ProductDTO(product, 0));
				}
			}

		}

		// Tạo một PageImpl mới từ List các ProductDTO, Pageable và tổng số phần tử
		Page<ProductDTO> productDTOPage = new PageImpl<ProductDTO>(productDTOList, productPage.getPageable(),
				productPage.getTotalElements() // Lấy tổng số phần tử từ productPage
		);

		modelMap.put("products", productDTOPage.getContent());
		modelMap.put("vendorCategories", vendorProductCategoryService.findByVendorId(id));
		modelMap.put("currentPages", page);
		modelMap.put("totalPages", productPage.getTotalPages());
		modelMap.put("lastPageIndex", productPage.getTotalPages() - 1);
		modelMap.put("vendor", vendorService.findById(id));
		modelMap.put("income", vendorService.sumByVendorId(id));
		modelMap.put("totalSell", vendorService.countByVendorId(id));
		modelMap.put("vendorAddresses", userService.findAddressUser(id));
		modelMap.put("id", id);
		if (vendorService.findById(id).getUsers().getDeletedAt() != null) {
			modelMap.put("actionType", "disable");
		} else {

			modelMap.put("actionType", "enable");
		}

		model.addAttribute("currentPage", "user");
		return "admin/page/user/vendor/detail";
	}

	@PostMapping("vendor/edit")
	public String vendorEdit(@RequestParam("id") int id, @RequestParam("actionType") String actionType) {
		Users users = userService.findById(id);

		if ("enable".equals(actionType)) {
			users.setDeletedAt(new Date()); // Set to current date for disabling
			vendorService.findById(id).setUpdatedAt(new Date()); // Set to current date for disabling
		} else if ("disable".equals(actionType)) {
			users.setDeletedAt(null); // Set to null for enabling
			vendorService.findById(id).setUpdatedAt(new Date()); // Set to current date for disabling
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
