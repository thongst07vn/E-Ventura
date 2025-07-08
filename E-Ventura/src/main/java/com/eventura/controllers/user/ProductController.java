package com.eventura.controllers.user;

import java.awt.Dialog.ModalExclusionType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventura.configurations.AccountOAuth2User;
import com.eventura.entities.CartItems;
import com.eventura.entities.Carts;
import com.eventura.entities.Coupons;
import com.eventura.entities.ProductAttributes;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.ProductVariants;
import com.eventura.entities.Products;
import com.eventura.entities.Users;
import com.eventura.services.CartService;
import com.eventura.services.CategoryService;
import com.eventura.services.CouponsService;
import com.eventura.services.ProductService;
import com.eventura.services.ProductVariantService;
import com.eventura.services.UserService;
import com.eventura.services.VendorService;

@Controller
@RequestMapping({ "product" })

public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private CouponsService couponsService;
	@Autowired
	private CartService cartService;
	@Autowired
	private ProductVariantService productVariantService;

	@GetMapping({ "productdetails/{id}" })
	public String prouctDetails(@PathVariable("id") int id, ModelMap modelMap, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails) {
		Products product = productService.findById(id);
		List<Coupons> allCoupons = couponsService.findAllCoupons();

		double originalPrice = product.getPrice();
		double afterDiscountPrice = originalPrice;
		boolean hasDiscount = false;

		for (Coupons coupon : allCoupons) {
			if (coupon.getProducts() != null && coupon.getProducts().getId() == product.getId()) {
				if (coupon.getDiscountUnit().equals("percent")) {
					afterDiscountPrice = originalPrice - (originalPrice * coupon.getDiscountValue());
					modelMap.put("discountValue", "-" + coupon.getDiscountValue() + "%");
				} else if (coupon.getDiscountUnit().equals("money")) {
					afterDiscountPrice = originalPrice - coupon.getDiscountValue();
					modelMap.put("discountValue", "-$" + coupon.getDiscountValue());
				}
				hasDiscount = true;
				break;
			}
		}
		if (originalPrice != afterDiscountPrice) {
			modelMap.put("afterDiscountPrice", afterDiscountPrice);
		}

		modelMap.put("product", product);
		modelMap.put("vendorAddresses", userService.findAddressUser(product.getVendors().getId()));
		modelMap.put("productReviews", productService.findProductReview(id));
		modelMap.put("productAttributes", productService.findProductAttributeByProductId(product.getId()));
		List<ProductAttributes> productAttributes = productService.findProductAttributeByProductId(product.getId());
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

		return "customer/pages/product/productdetails";
	}

	@GetMapping({ "findbycategory/{id}" })
	public String findByCategory(@PathVariable("id") int id, ModelMap modelMap,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int pageSize,
			Authentication authentication, @AuthenticationPrincipal UserDetails userDetails) {

		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

		Page<Products> products = productService.findByCategoryIdPage(id, pageable);
		List<ProductCategories> categories = categoryService.findAll();
		modelMap.put("products", products.getContent());
		modelMap.put("categories", categories);
		modelMap.put("categoryId", id);

		modelMap.addAttribute("currentPages", page);
		modelMap.addAttribute("totalPage", products.getTotalPages());
		modelMap.addAttribute("lastPageIndex", products.getTotalPages() - 1);
		modelMap.addAttribute("pageSize", pageSize);
		return "customer/pages/home/shopgrid";
	}

	@GetMapping({ "search" })
	public String searchByKeyword(@RequestParam("keyword") String keyword, ModelMap modelMap,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int pageSize,
			Authentication authentication, @AuthenticationPrincipal UserDetails userDetails) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Products> products = productService.findByKeywordPage(keyword, pageable);
		List<ProductCategories> categories = categoryService.findAll();
		modelMap.put("products", products.getContent());
		modelMap.put("categories", categories);
		modelMap.put("keyword", keyword);

		modelMap.addAttribute("currentPages", page);
		modelMap.addAttribute("totalPage", products.getTotalPages());
		modelMap.addAttribute("lastPageIndex", products.getTotalPages() - 1);
		modelMap.addAttribute("pageSize", pageSize);
		return "customer/pages/home/shopgrid";
	}

	@PostMapping({ "addproducttocart" })
	public String searchByKeyword(@RequestParam("productId") int productId,
			@RequestParam("productVariantId") int productVariantId, @RequestParam("quantity") int quantity,
			@RequestParam("combination") String combination, ModelMap modelMap, Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
		Products product = productService.findById(productId);
		CartItems cartItem = new CartItems();
		cartItem.setCombination(combination.replace(",",""));
		cartItem.setProducts(product);
		cartItem.setProductVariants(productVariantService.findById(productVariantId));
		cartItem.setQuantity(quantity);
		cartItem.setCreatedAt(new Date());
		cartItem.setUpdatedAt(new Date());
		Users user = null;
		if (userDetails != null) {
			user = userService.findByEmail(userDetails.getUsername());

		} else if (authentication != null) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			user = userService.findByEmail(accountOAuth2User.getEmail());
		}
		if(user != null) {
			List<Carts> userCarts = cartService.findCartByUserId(user.getId());
			
			for(Carts cart : userCarts) {
				Set<CartItems> cartItemsSet = cart.getCartItemses();
			    List<CartItems> cartItemsList = new ArrayList<>(cartItemsSet);
				CartItems currentCart = cartItemsList.get(0);
				if(currentCart.getProducts().getVendors().getId() == product.getVendors().getId()) {
					for(CartItems currentItem : cartItemsList) {
						if(currentItem.getCombination().equals(combination.replace(",",""))) {
							if(currentItem.getQuantity()+quantity <= product.getQuantity()) {
								currentItem.setQuantity(currentItem.getQuantity()+quantity);														
							} else {
								currentItem.setQuantity(product.getQuantity());
								currentItem.setUpdatedAt(new Date());
							}
							cartService.saveCartItems(currentItem);	
							return "redirect:/product/productdetails/" + productId;							
						}
					}
					cartItem.setCarts(cart);
					
					if(cartService.saveCartItems(cartItem)) {
						redirectAttributes.addFlashAttribute("msg","Add to cart success");
					} else {
						redirectAttributes.addFlashAttribute("msg","Add to cart failed, error in cartItemsService");
					}
					return "redirect:/product/productdetails/" + productId;	
				}
			}
			Carts newCart = new Carts();
			
			newCart.setUsers(user);
			newCart.setCreatedAt(new Date());
			newCart.setUpdatedAt(new Date());
			if(cartService.saveCart(newCart)) {
				cartItem.setCarts(newCart);
				if(cartService.saveCartItems(cartItem)) {
					redirectAttributes.addFlashAttribute("msg","Add to cart success");
				} else {
					redirectAttributes.addFlashAttribute("msg","Add to cart failed, error in cartItemsService");
				}		    	
			} else {
				redirectAttributes.addFlashAttribute("msg","Add to cart failed, error in cartService");		    					
			}
		}
		
		return "redirect:/product/productdetails/" + productId;
	}

}
