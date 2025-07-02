package com.eventura.controllers.user;

import java.util.ArrayList;
import java.util.Comparator; // Import Comparator
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors; // Not strictly needed for this, but good to have for stream operations

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventura.configurations.AccountOAuth2User;

import com.eventura.dtos.CartItemsDTO;
import com.eventura.entities.CartItems;
import com.eventura.entities.Carts;
import com.eventura.entities.Coupons;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.Vendors;
import com.eventura.entities.Vouchers;
import com.eventura.services.AddressService;
import com.eventura.services.CartService;
import com.eventura.services.CategoryService;
import com.eventura.services.CouponsService;
import com.eventura.services.ProductService;
import com.eventura.services.ProductVariantService;
import com.eventura.services.UserAddressService;
import com.eventura.services.UserService;
import com.eventura.services.VendorService;
import com.eventura.services.VouchersService;

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
	@Autowired
	private UserAddressService userAddressService;
	@Autowired
	private AddressService addressService;	
	@Autowired
	private VouchersService vouchersService;
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
	@GetMapping({"deletecartitem/{cartItemId}"})
	public String deleteCartItem(@PathVariable("cartItemId") int cartItemId,RedirectAttributes redirectAttributes) {
			if(cartService.deleteCartItems(cartItemId)) {
				redirectAttributes.addFlashAttribute("msg","update cart item success");
			} else {
				redirectAttributes.addFlashAttribute("msg","update cart item failed");				
			}
		return "redirect:/customer/cart/cartitems";
	}
	
	@GetMapping({"checkout"})
	public String checkout(Authentication authentication, ModelMap modelMap, @AuthenticationPrincipal UserDetails userDetails, @RequestParam("checkoutItems") String checkoutItems) {
		Users user = null;			
		UserAddress addAddressVariable = new UserAddress();
		List<CartItems> checkoutList = new ArrayList();
		Map<Vendors, List<CartItemsDTO>> groupedCheckoutList = new HashMap<>();
		Map<Integer, List<Vouchers>> voucherByVendorList = new HashMap<>();
		List<Vouchers> voucherByEventura = new ArrayList<>();

		List<Coupons> allCoupons = couponsService.findAllCoupons();	
		double subTotal = 0;
		if (userDetails != null) {
			user = userService.findByEmail(userDetails.getUsername());
		} else if (authentication != null) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			user = userService.findByEmail(accountOAuth2User.getEmail());
		}
		String[] splitedIds = checkoutItems.split(",");
		for(int i=0; i <splitedIds.length; i++) {
			checkoutList.add(cartService.findCartItemsById(Integer.parseInt(splitedIds[i])));			
		}
		for (CartItems item : checkoutList) {
			double originalPrice = item.getProducts().getPrice();
			double afterDiscountPrice = originalPrice; 
			boolean hasDiscount = false;
			
			for(Coupons coupon: allCoupons) {
				if(coupon.getProducts() != null && coupon.getProducts().getId() == item.getProducts().getId()) {
					if(coupon.getDiscountUnit().equals("percent")) {
						afterDiscountPrice = originalPrice - (originalPrice * coupon.getDiscountValue());							
					} else if(coupon.getDiscountUnit().equals("money")) {
						afterDiscountPrice = originalPrice - coupon.getDiscountValue();
					}
					hasDiscount = true; 
					break; 
				}
			}
			String[] combinationArray = item.getCombination().split("-");
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
		    
		    
			CartItemsDTO dto = new CartItemsDTO(item, afterDiscountPrice, originalPrice, hasDiscount, combinationString);
			
			Vendors vendor = item.getProducts().getVendors();
			groupedCheckoutList
			    .computeIfAbsent(vendor, k -> new ArrayList<>())
			    .add(dto);
			if(dto.isHasDiscount()) {
				subTotal += (afterDiscountPrice*item.getQuantity());   
			} else {
				subTotal += (originalPrice*item.getQuantity());				
			}
	    }
		for(Vendors vendor : groupedCheckoutList.keySet()) {
			double totalVendor = 0;
			if(groupedCheckoutList.containsKey(vendor)) {
				for (CartItemsDTO dto : groupedCheckoutList.get(vendor)) {
					if(dto.isHasDiscount()) {
						totalVendor += (dto.getAfterDiscountPrice()*dto.getCartItem().getQuantity());   
					} else {
						totalVendor += (dto.getOriginalPrice()*dto.getCartItem().getQuantity());				
					}
				}
								
			}
			double finalCurrentVendorTotal = totalVendor;
			voucherByVendorList.computeIfAbsent(vendor.getId(), k -> vouchersService.findAllVoucherByVendorId(vendor.getId(), finalCurrentVendorTotal));
		}
		voucherByEventura = vouchersService.findAllVoucherByEventura(subTotal);
		modelMap.put("voucherByEventura", voucherByEventura);
		modelMap.put("voucherByVendorList", voucherByVendorList);
		modelMap.put("checkoutItems", checkoutItems);
		modelMap.put("provinces", addressService.findAllProvinces());
		modelMap.put("user", user);
		modelMap.put("addAddressVariable", addAddressVariable);
		modelMap.put("subTotal", subTotal);
		modelMap.put("groupedCheckoutList", groupedCheckoutList);
		modelMap.put("userAdresses", userService.findAddressUser(user.getId()));
		return "customer/pages/cart/checkout";
	}
	
	
	
	@PostMapping("/updatecartitemquantity/{cartItemId}")
    @ResponseBody // Trả về dữ liệu JSON
    public ResponseEntity<Map<String, Object>> updateCartItemQuantity(
            @PathVariable int cartItemId,
            @RequestBody Map<String, Integer> payload) { // Nhận JSON payload
        Map<String, Object> response = new HashMap<>();

        if (!payload.containsKey("quantity")) {
            response.put("success", false);
            response.put("message", "Thiếu tham số quantity.");
            return ResponseEntity.badRequest().body(response);
        }

        Integer newQuantity = payload.get("quantity");
        if (newQuantity == null || newQuantity < 1) {
            response.put("success", false);
            response.put("message", "Số lượng không hợp lệ.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // --- Logic cập nhật số lượng trong cơ sở dữ liệu ---
            // Gọi service để cập nhật số lượng cho cartItemId
            // Ví dụ: cartService.updateCartItemQuantity(cartItemId, newQuantity);
        	CartItems cartItem = cartService.findCartItemsById(cartItemId);
        	cartItem.setQuantity((int)newQuantity);
        	cartService.saveCartItems(cartItem);
            // Giả lập logic cập nhật và tính toán lại giá
            // Trong thực tế, bạn sẽ lấy thông tin sản phẩm từ DB để tính giá

            response.put("success", true);
            response.put("newQuantity", newQuantity);
            response.put("message", "Cập nhật số lượng thành công.");
            // --- Hết logic cập nhật ---

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Log lỗi chi tiết
            System.err.println("Lỗi khi cập nhật cart item " + cartItemId + ": " + e.getMessage());
            response.put("success", false);
            response.put("message", "Không thể cập nhật số lượng. Vui lòng thử lại.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
	
}