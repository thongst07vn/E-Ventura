package com.eventura.controllers.user;

import java.util.ArrayList;
import java.util.Comparator; // Import Comparator
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors; // Not strictly needed for this, but good to have for stream operations

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventura.configurations.AccountOAuth2User;

import com.eventura.dtos.CartItemsDTO;
import com.eventura.entities.CartItems;
import com.eventura.entities.Carts;
import com.eventura.entities.Coupons;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors; 
import com.eventura.services.CartService;
import com.eventura.services.CategoryService;
import com.eventura.services.CouponsService;
import com.eventura.services.ProductService;
import com.eventura.services.ProductVariantService;
import com.eventura.services.UserService;
import com.eventura.services.VendorService;

@Controller
@RequestMapping({"customer/cart"})
public class CartController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;
	@Autowired
	private VendorService vendorService; 
	@Autowired
	private CartService cartService;
	@Autowired
	private CouponsService couponsService;
	@Autowired
	private ProductVariantService productVariantService;
	
	// User Cart
	@GetMapping({"cartitems"})
	public String cartList(Authentication authentication, ModelMap modelMap, @AuthenticationPrincipal UserDetails userDetails) {
		Users user = null;
		List<Coupons> allCoupons = couponsService.findAllCoupons();		
		
		if (userDetails != null) {
			user = userService.findByEmail(userDetails.getUsername());
		} else if (authentication != null) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			user = userService.findByEmail(accountOAuth2User.getEmail());
		}
		
		List<Carts> userCarts = cartService.findCartByUserId(user.getId());
		
		Map<Vendors, List<CartItemsDTO>> cartItemsGroupedByVendor = new HashMap<>();
		Map<String, CartItemsDTO> cartItemDtoLookupMap = new HashMap<>();

		for(Carts cart : userCarts) {
			// Sắp xếp cartItems trong mỗi Carts trước khi xử lý
			// Đảm bảo CartItems có trường 'createdAt' và getter tương ứng.
			List<CartItems> sortedCartItems = cart.getCartItemses().stream()
			                                        .sorted(Comparator.comparing(CartItems::getCreatedAt)) // Sắp xếp tại đây!
			                                        .collect(Collectors.toList());

			for(CartItems cartItem: sortedCartItems) { // Lặp qua danh sách đã sắp xếp
				double originalPrice = cartItem.getProducts().getPrice();
				double afterDiscountPrice = originalPrice; 
				boolean hasDiscount = false;
				
				for(Coupons coupon: allCoupons) {
					if(coupon.getProducts() != null && coupon.getProducts().getId() == cartItem.getProducts().getId()) {
						if(coupon.getDiscountUnit().equals("percent")) {
							afterDiscountPrice = originalPrice - (originalPrice * coupon.getDiscountValue());							
						} else if(coupon.getDiscountUnit().equals("money")) {
							afterDiscountPrice = originalPrice - coupon.getDiscountValue();
						}
						hasDiscount = true; 
						break; 
					}
				}
				String[] combinationArray = cartItem.getCombination().split("-");
				StringBuilder combinationStringBuilder = new StringBuilder(); // Use StringBuilder for efficiency

			    // Loop with an index to check for the last element
			    for (int i = 0; i < combinationArray.length; i++) {
			        String variantId = combinationArray[i];
			        
			        // Find the ProductVariant by ID and append its value
			        String value = productVariantService.findById(Integer.parseInt(variantId)).getValue();

			        combinationStringBuilder.append(value);

			        // Append a hyphen if it's not the last element
			        if (i < combinationArray.length - 1) {
			            combinationStringBuilder.append("-");
			        }
			    }
			    String combinationString = combinationStringBuilder.toString();
			    
			    
				CartItemsDTO dto = new CartItemsDTO(cartItem, afterDiscountPrice, originalPrice, hasDiscount, combinationString);
				
				Vendors vendor = cartItem.getProducts().getVendors();
				cartItemsGroupedByVendor
				    .computeIfAbsent(vendor, k -> new ArrayList<>())
				    .add(dto);

				String key = cartItem.getProducts().getId() + "-" + (cartItem.getProductVariants() != null ? cartItem.getProductVariants().getId() : "no-variant");
				cartItemDtoLookupMap.put(key, dto);
			}
		}

		modelMap.put("cartItemsGroupedByVendor", cartItemsGroupedByVendor);
		modelMap.put("coupons", allCoupons); 
		modelMap.put("cartItemDtoLookupMap", cartItemDtoLookupMap); 
		
		return "customer/pages/cart/cartlist";
	}
	
	@GetMapping({"checkout"})
	public String checkout(Authentication authentication, ModelMap modelMap, @AuthenticationPrincipal UserDetails userDetails) {
		return "customer/pages/cart/checkout";
	}
	
}