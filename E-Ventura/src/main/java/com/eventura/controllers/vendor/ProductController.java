package com.eventura.controllers.vendor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import com.eventura.dtos.ProductDTO;
import com.eventura.entities.Medias;
import com.eventura.entities.ProductAttributes;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.ProductReviews;
import com.eventura.entities.ProductVariants;
import com.eventura.entities.Products;
import com.eventura.entities.Roles;
import com.eventura.entities.Vendors;
import com.eventura.services.CategoryService;
import com.eventura.services.MediaService;
import com.eventura.services.ProductService;
import com.eventura.services.ProductVariantService;
import com.eventura.services.VendorProductCategoryService;
import com.eventura.services.VendorService;
import com.example.demo.helpers.FileHelper;

import jakarta.servlet.http.HttpSession;

@Controller("vendorProductController")
@RequestMapping("vendor/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductVariantService productVariantService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private MediaService mediaService;

	/* ===================== PRODUCT ===================== */
	@GetMapping("list")
	public String productList(ModelMap modelMap, HttpSession session, @RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "product");
		Integer vendorId = (Integer) session.getAttribute("vendorId");

		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage = productService.findProductByVendorAndDeletePage(vendorId, pageable);
		List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

		// Lặp qua productPage và thêm các ProductDTO vào List
		for (Products product : productPage) {
			if (product.getDeletedAt() == null && !product.isDeleted()) {

				if (!productService.findProductReview(product.getId()).isEmpty()) {
					productDTOList.add(new ProductDTO(product, productService.avgProductReview(product.getId())));
				} else {
					productDTOList.add(new ProductDTO(product, 0));
				}
			}
		}
		Page<ProductDTO> productDTOPage = new PageImpl<ProductDTO>(productDTOList, productPage.getPageable(),
				productPage.getTotalElements());

		modelMap.put("products", productDTOPage.getContent());
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", productPage.getTotalPages());
		modelMap.put("lastPageIndex", productPage.getTotalPages() - 1);
		modelMap.put("minRating", 0);
		

		modelMap.put("categories", categoryService.findAll());
		return "vendor/pages/product/list";
	}

	@GetMapping("searchByCategory")
	public String searchByVendorCategory(ModelMap modelMap, HttpSession session,
			@RequestParam("categoryId") int categoryId, @RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "product");
		Integer vendorId = (Integer) session.getAttribute("vendorId");

		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage;

		if (categoryId == 0) {
			productPage = productService.findProductByVendorAndDeletePage(vendorId, pageable);
			List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

			for (Products product : productPage) {
				if (product.getDeletedAt() == null && !product.isDeleted()) {

					if (!productService.findProductReview(product.getId()).isEmpty()) {
						productDTOList.add(new ProductDTO(product, productService.avgProductReview(product.getId())));
					} else {
						productDTOList.add(new ProductDTO(product, 0));
					}
				}

			}
			Page<ProductDTO> productDTOPage = new PageImpl<ProductDTO>(productDTOList, productPage.getPageable(),
					productPage.getTotalElements() // Lấy tổng số phần tử từ productPage
			);
			modelMap.put("products", productDTOPage.getContent());
//			map.put("products", productPage.getContent());
		} else {
			productPage = productService.findProductByVendorAndDeleteAndCategoryPage(vendorId, categoryId, pageable);
			List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

			for (Products product : productPage) {
				if (product.getDeletedAt() == null && !product.isDeleted()) {
					if (!productService.findProductReview(product.getId()).isEmpty()) {
						productDTOList.add(new ProductDTO(product, productService.avgProductReview(product.getId())));
					} else {
						productDTOList.add(new ProductDTO(product, 0));
					}
				}

			}
			productDTOList.sort(Comparator.comparing(ProductDTO::getRating, Comparator.reverseOrder()));
			Page<ProductDTO> productDTOPage = new PageImpl<ProductDTO>(productDTOList, productPage.getPageable(),
					productPage.getTotalElements() // Lấy tổng số phần tử từ productPage
			);
			modelMap.put("products", productDTOPage.getContent());
		}

		modelMap.put("currentPages", page);
		modelMap.put("totalPage", productPage.getTotalPages());
		modelMap.put("lastPageIndex", productPage.getTotalPages() - 1);

		modelMap.put("selectedCategoryId", categoryId);

		modelMap.put("categories", categoryService.findAll());
		return "vendor/pages/product/list";
	}

	@GetMapping("searchByKeyword")
	public String searchByKeyword(ModelMap modelMap, HttpSession session, @RequestParam("keyword") String keyword,
			@RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "product");
		Integer vendorId = (Integer) session.getAttribute("vendorId");

		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage = productService.findProductByVendorAndDeleteAndKeywordPage(vendorId, keyword, pageable);
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
				productPage.getTotalElements());

		modelMap.put("products", productDTOPage.getContent());
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", productPage.getTotalPages());
		modelMap.put("lastPageIndex", productPage.getTotalPages() - 1);
		modelMap.put("minRating", 0);
		modelMap.put("selectedKeyword", keyword);
		modelMap.put("categories", categoryService.findAll());

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

		modelMap.put("product", new Products());

		return "vendor/pages/product/add";
	}

	@PostMapping("add")
	public String productAdd(@ModelAttribute("product") Products product, @RequestParam("categoryId") int categoryId,
			@RequestParam("files") List<MultipartFile> files, HttpSession session,
			RedirectAttributes redirectAttributes) {

		if (product.getName() == null || product.getName().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("msgErrorName", "* Product's Name cannot be empty.");
			return "redirect:/vendor/product/add";
		}

		if (product.getPrice() <= 0) {
			redirectAttributes.addFlashAttribute("msgErrorPrice", "* Price must be greater than 0.");
			return "redirect:/vendor/product/add";
		}

		if (product.getQuantity() <= 0) {
			redirectAttributes.addFlashAttribute("msgErrorQuantity", "* Quantity must be greater than 0.");
			return "redirect:/vendor/product/add";
		}

		if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("msgErrorDescription", "* Description cannot be empty.");
			return "redirect:/vendor/product/add";
		}
		if (files == null || files.isEmpty() || files.stream().anyMatch(file -> file.isEmpty())) {
			redirectAttributes.addFlashAttribute("sweetAlert", "warning");
			redirectAttributes.addFlashAttribute("message", "You must Upload Image");
			return "redirect:/vendor/product/add";
		}

		/* VENDOR */
		Integer vendorId = (Integer) session.getAttribute("vendorId");
		Vendors vendor = vendorService.findById(vendorId);
		product.setVendors(vendor);

		/* CATEGORY */
		ProductCategories category = categoryService.findById(categoryId);
		product.setProductCategories(category);

		product.setCreatedAt(new Date());
		product.setUpdatedAt(new Date());
		product.setDeletedAt(null);

		/* IMAGE */
		// Lưu product trước để có ID
		if (productService.save(product)) {

			Set<Medias> mediasSet = new HashSet<>();

			if (files != null && !files.isEmpty()) {
				for (MultipartFile file : files) {
					try {
			            String fileName = FileHelper.generateFileName(file.getOriginalFilename());

						File imagesFolder = new ClassPathResource("static/assets/imgs/items").getFile();
						Path path = Paths.get(imagesFolder.getAbsolutePath() + File.separator + fileName);
						Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

						// Tạo media sau khi product đã có ID
						Medias media = new Medias();
						media.setName(fileName);
						media.setProducts(product);
						media.setCreatedAt(new Date());
						media.setUpdatedAt(new Date());
						mediaService.save(media); // Save media sau

						mediasSet.add(media);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			product.setMediases(mediasSet);
			productService.save(product);

			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Add Product Successfully");
			return "redirect:/vendor/product/list";

		} else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Add Product Failed");
			return "redirect:/vendor/product/add";
		}

	}

	@GetMapping("edit/{id}")
	public String productEdit(@PathVariable("id") int id, @ModelAttribute("product") Products product,
			ModelMap modelMap) {
		modelMap.put("currentPage", "product");

		modelMap.put("product", productService.findById(id));
		modelMap.put("productVariants", productVariantService.findByProductId(id));
		modelMap.put("categories", categoryService.findAll());
		
		modelMap.put("productVariant", new ProductVariants());
		modelMap.put("productAttribute", new ProductAttributes());



		return "vendor/pages/product/edit";
	}

	@PostMapping("edit")
	public String productEdit(@ModelAttribute("product") Products product,
			 @RequestParam("proId") int proId,
			 @RequestParam(value = "files") List<MultipartFile> files, HttpSession session,
			RedirectAttributes redirectAttributes) {
		if (product.getName() == null || product.getName().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("msgErrorName", "* Product's Name cannot be empty.");
			return "redirect:/vendor/product/edit/" + proId;
		}

		if (product.getPrice() <= 0) {
			redirectAttributes.addFlashAttribute("msgErrorPrice", "* Price must be greater than 0.");
			return "redirect:/vendor/product/edit/" + proId;
		}

		if (product.getQuantity() <= 0) {
			redirectAttributes.addFlashAttribute("msgErrorQuantity", "* Quantity must be greater than 0.");
			return "redirect:/vendor/product/edit/" + proId;
		}

		if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("msgErrorDescription", "* Description cannot be empty.");
			return "redirect:/vendor/product/edit/" + proId;
		}
		/* VENDOR */
		Integer vendorId = (Integer) session.getAttribute("vendorId");
		Vendors vendor = vendorService.findById(vendorId);
		product.setVendors(vendor);

		product.setCreatedAt(new Date());
		product.setUpdatedAt(new Date());
		product.setDeletedAt(product.getDeletedAt());

		System.out.println(product.getMediases().size());
		System.out.println(files.size());
		
		// Kiểm tra và xóa ảnh cũ trước khi thêm ảnh mới
		if (files != null && !files.isEmpty() && files.stream().anyMatch(file -> !file.isEmpty())) {
		    Set<Medias> existingMedias = productService.findById(proId).getMediases();
		    if (existingMedias != null && !existingMedias.isEmpty()) {
		        for (Medias media : existingMedias) {
		        	System.out.println("ten: "+ media.getName());
		            mediaService.delete(media.getId()); // Xóa ảnh cũ khỏi database

		            // Xóa ảnh khỏi thư mục lưu trữ nếu có
		            // Ví dụ: nếu dùng thư mục assets/imgs/items/
		            File imageFile = new File("D:/Document_in_school/Aptech/Spring/Final_Project2/E-Ventura/E-Ventura/target/classes/static/assets/imgs/items/" + media.getName());
		            if (imageFile.exists()) {
		                boolean deleted = imageFile.delete(); // Chỉ xóa tệp ảnh
		                if (deleted) {
		                    System.out.println("Deleted image: " + imageFile.getName());
		                } else {
		                    System.out.println("Failed to delete image: " + imageFile.getName());
		                }
		            }
		        }
		    }
			Set<Medias> mediasSet = new HashSet<>();

		    // Thêm ảnh mới vào database và thư mục
		    for (MultipartFile file : files) {
		        try {
		            String fileName = file.getOriginalFilename();

		            // Tạo thư mục nếu không tồn tại
		            File imagesFolder = new ClassPathResource("static/assets/imgs/items").getFile();
		            if (!imagesFolder.exists()) {
		                imagesFolder.mkdirs();
		            }

		            // Lưu file vào thư mục
		            Path path = Paths.get(imagesFolder.getAbsolutePath() + File.separator + fileName);
		            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		            // Tạo media và liên kết với sản phẩm
		            Medias mediaNew = new Medias();
		            mediaNew.setName(fileName);
		            mediaNew.setProducts(product);
		            mediaNew.setCreatedAt(new Date());
		            mediaNew.setUpdatedAt(new Date());
		            mediaService.save(mediaNew); // Save media mới

		            mediasSet.add(mediaNew);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
			product.setMediases(mediasSet);

		}



		if (productService.save(product)) {

			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Edit Product Successfully");
		} else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Edit Product Failed");
		}
		return "redirect:/vendor/product/list";

	}

	@GetMapping("delete/{id}")
	public String productDelete(@PathVariable("id") int id, 
						         ModelMap modelMap, RedirectAttributes redirectAttributes) {
		modelMap.put("currentPage", "product");
		
		Products productDelete = productService.findById(id);
		productDelete.setDeleted(true);
		
		if (productService.save(productDelete)) {

			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Delete Product Successfully");
			return "redirect:/vendor/product/list";

		} else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Delete Product Failed");
			return "redirect:/vendor/product/list";

		}
	}

	@GetMapping("review")
	public String productReview(ModelMap modelMap, HttpSession session, @RequestParam("categoryId") int categoryId,
			@RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "review");
		Integer vendorId = (Integer) session.getAttribute("vendorId");

		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage = productService.findByVendorIdPage(vendorId, pageable);
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
				productPage.getTotalElements());

		modelMap.put("products", productDTOPage.getContent());
		modelMap.put("currentPages", page);
		modelMap.put("totalPage", productPage.getTotalPages());
		modelMap.put("lastPageIndex", productPage.getTotalPages() - 1);
		modelMap.put("minRating", 0);

		modelMap.put("categories", categoryService.findAll());
		modelMap.put("selectedCategoryId", categoryId);

		return "vendor/pages/product/review";
	}

	@GetMapping("reviewDetail/{productId}")
	public String productReviewDetail(@PathVariable("productId") int productId, ModelMap modelMap) {
		modelMap.put("currentPage", "review");

		return "vendor/pages/product/reviewDetail";
	}

}
