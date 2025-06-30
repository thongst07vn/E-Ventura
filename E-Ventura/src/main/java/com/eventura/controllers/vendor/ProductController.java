package com.eventura.controllers.vendor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventura.dtos.ProductDTO;
import com.eventura.entities.ProductReviews;
import com.eventura.entities.Products;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.ProductVariantService;
import com.eventura.services.VendorProductCategoryService;

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

	/* ===================== PRODUCT ===================== */
	@GetMapping("list")
	public String productList(ModelMap modelMap, HttpSession session, @RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "product");
		Integer vendorId = (Integer) session.getAttribute("vendorId");

		int pageSize = 5;
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
		return "vendor/pages/product/list";
	}

	@GetMapping("searchByCategory")
	public String searchByVendorCategory(ModelMap modelMap, HttpSession session,
										@RequestParam("categoryId") int categoryId, 
										@RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "product");
		Integer vendorId = (Integer) session.getAttribute("vendorId");

		int pageSize = 5;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage;

		if (categoryId == 0) {
			productPage = productService.findByVendorIdPage(vendorId, pageable);
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
					productPage.getTotalElements() // Lấy tổng số phần tử từ productPage
			);
			modelMap.put("products", productDTOPage.getContent());
//			map.put("products", productPage.getContent());
		} else {
			productPage = productService.findByVendorCategoryPage(vendorId, categoryId, pageable);
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
	public String searchByKeyword(ModelMap modelMap, HttpSession session,
										@RequestParam("keyword") String keyword, 
										@RequestParam(defaultValue = "0") int page) {
		modelMap.put("currentPage", "product");
		Integer vendorId = (Integer) session.getAttribute("vendorId");

		System.out.println("ncc");
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> productPage = productService.findByKeywordAndVendorIdPage(keyword, vendorId, pageable);
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
	public String productReview(ModelMap modelMap, HttpSession session, 
								@RequestParam("categoryId") int categoryId,
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
