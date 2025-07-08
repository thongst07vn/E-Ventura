package com.eventura.controllers.user;

import java.awt.Window.Type;
import java.util.ArrayList;
import java.util.Comparator; // Import Comparator
import java.util.Date;
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
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.eventura.entities.Commissions;
import com.eventura.entities.Coupons;
import com.eventura.entities.CouponsCampaigns;
import com.eventura.entities.OrderItems;
import com.eventura.entities.OrderItemsOrderStatus;
import com.eventura.entities.OrderItemsOrderStatusId;
import com.eventura.entities.Orders;
import com.eventura.entities.OrdersCampaigns;
import com.eventura.entities.OrdersCampaignsId;
import com.eventura.entities.ProductVariants;
import com.eventura.entities.Products;
import com.eventura.entities.UserAddress;
import com.eventura.entities.Users;
import com.eventura.entities.VendorEarnings;
import com.eventura.entities.Vendors;
import com.eventura.entities.Vouchers;
import com.eventura.entities.VouchersCampaigns;
import com.eventura.helpers.RandomStringCode;
import com.eventura.services.AddressService;
import com.eventura.services.CampaignRedeemtionService;
import com.eventura.services.CartService;
import com.eventura.services.CategoryService;
import com.eventura.services.CommissionsService;
import com.eventura.services.CouponsService;
import com.eventura.services.OrderItemService;
import com.eventura.services.OrderService;
import com.eventura.services.OrderStatusService;
import com.eventura.services.ProductService;
import com.eventura.services.ProductVariantService;
import com.eventura.services.UserAddressService;
import com.eventura.services.UserService;
import com.eventura.services.VendorService;
import com.eventura.services.VouchersService;

@Controller
@RequestMapping({ "customer/cart" })
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
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private CommissionsService commissionsService;
	@Autowired
	private CampaignRedeemtionService campaignRedeemtionService;
	@Autowired
	private OrderStatusService orderStatusService;

	// User Cart
	@GetMapping({ "cartitems" })
	public String cartList(Authentication authentication, ModelMap modelMap,
			@AuthenticationPrincipal UserDetails userDetails) {
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

		for (Carts cart : userCarts) {
			// Sắp xếp cartItems trong mỗi Carts trước khi xử lý
			// Đảm bảo CartItems có trường 'createdAt' và getter tương ứng.
			List<CartItems> sortedCartItems = cart.getCartItemses().stream()
					.sorted(Comparator.comparing(CartItems::getCreatedAt)) // Sắp xếp tại đây!
					.collect(Collectors.toList());

			for (CartItems cartItem : sortedCartItems) { // Lặp qua danh sách đã sắp xếp
				double originalPrice = cartItem.getProducts().getPrice();
				double afterDiscountPrice = originalPrice;
				boolean hasDiscount = false;

				for (Coupons coupon : allCoupons) {
					if (coupon.getProducts() != null
							&& coupon.getProducts().getId() == cartItem.getProducts().getId()) {
						if (coupon.getDiscountUnit().equals("percent")) {
							afterDiscountPrice = originalPrice - (originalPrice * coupon.getDiscountValue());
						} else if (coupon.getDiscountUnit().equals("money")) {
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

				CartItemsDTO dto = new CartItemsDTO(cartItem, afterDiscountPrice, originalPrice, hasDiscount,
						combinationString);

				Vendors vendor = cartItem.getProducts().getVendors();
				cartItemsGroupedByVendor.computeIfAbsent(vendor, k -> new ArrayList<>()).add(dto);

				String key = cartItem.getProducts().getId() + "-"
						+ (cartItem.getProductVariants() != null ? cartItem.getProductVariants().getId()
								: "no-variant");
				cartItemDtoLookupMap.put(key, dto);
			}
		}

		modelMap.put("cartItemsGroupedByVendor", cartItemsGroupedByVendor);
		modelMap.put("coupons", allCoupons);
		modelMap.put("cartItemDtoLookupMap", cartItemDtoLookupMap);

		return "customer/pages/cart/cartlist";
	}

	@GetMapping({ "deletecartitem/{cartItemId}" })
	public String deleteCartItem(@PathVariable("cartItemId") int cartItemId, RedirectAttributes redirectAttributes) {
		CartItems cartItem = cartService.findCartItemsById(cartItemId);
		if (cartService.deleteCartItems(cartItemId)) {
			if(cartService.findCartById(cartItem.getCarts().getId()).getCartItemses().isEmpty()) {
				if(!cartService.deleteCart(cartService.findCartById(cartItem.getCarts().getId()))){								
					System.out.println("delete cart wrong " + cartItem.getId());						
				}
			}	
			redirectAttributes.addFlashAttribute("msg", "update cart item success");
		} else {
			redirectAttributes.addFlashAttribute("msg", "update cart item failed");
		}
		return "redirect:/customer/cart/cartitems";
	}

	@GetMapping({ "checkout" })
	public String checkout(Authentication authentication, ModelMap modelMap,
			@AuthenticationPrincipal UserDetails userDetails, @RequestParam("checkoutItems") String checkoutItems) {
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
		for (int i = 0; i < splitedIds.length; i++) {
			checkoutList.add(cartService.findCartItemsById(Integer.parseInt(splitedIds[i])));
		}
		for (CartItems item : checkoutList) {
			double originalPrice = item.getProducts().getPrice();
			double afterDiscountPrice = originalPrice;
			boolean hasDiscount = false;

			for (Coupons coupon : allCoupons) {
				if (coupon.getProducts() != null && coupon.getProducts().getId() == item.getProducts().getId()) {
					if (coupon.getDiscountUnit().equals("percent")) {
						afterDiscountPrice = originalPrice - (originalPrice * coupon.getDiscountValue());
					} else if (coupon.getDiscountUnit().equals("money")) {
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

			CartItemsDTO dto = new CartItemsDTO(item, afterDiscountPrice, originalPrice, hasDiscount,
					combinationString);

			Vendors vendor = item.getProducts().getVendors();
			groupedCheckoutList.computeIfAbsent(vendor, k -> new ArrayList<>()).add(dto);
			if (dto.isHasDiscount()) {
				subTotal += (afterDiscountPrice * item.getQuantity());
			} else {
				subTotal += (originalPrice * item.getQuantity());
			}
		}
		for (Vendors vendor : groupedCheckoutList.keySet()) {
			double totalVendor = 0;
			if (groupedCheckoutList.containsKey(vendor)) {
				for (CartItemsDTO dto : groupedCheckoutList.get(vendor)) {
					if (dto.isHasDiscount()) {
						totalVendor += (dto.getAfterDiscountPrice() * dto.getCartItem().getQuantity());
					} else {
						totalVendor += (dto.getOriginalPrice() * dto.getCartItem().getQuantity());
					}
				}

			}
			double finalCurrentVendorTotal = totalVendor;
			voucherByVendorList.computeIfAbsent(vendor.getId(),
					k -> vouchersService.findAllVoucherByVendorId(vendor.getId(), finalCurrentVendorTotal));
		}
		voucherByEventura = vouchersService.findAllVoucherByEventura(subTotal);
		double total = subTotal;

		modelMap.put("voucherByEventura", voucherByEventura);
		modelMap.put("voucherByVendorList", voucherByVendorList);
		modelMap.put("checkoutItems", checkoutItems);
		modelMap.put("provinces", addressService.findAllProvinces());
		modelMap.put("user", user);
		modelMap.put("addAddressVariable", addAddressVariable);
		modelMap.put("subTotal", subTotal);
		modelMap.put("total", total);
		modelMap.put("groupedCheckoutList", groupedCheckoutList);
		List<UserAddress> userAdresses = userService.findAddressUser(user.getId());

		modelMap.put("userAdresses", userAdresses);
		Orders newOrder = new Orders();
		newOrder.setPaymentMethod("COD");
		newOrder.setUserAddress(userAdresses.get(0).getAddress() + " "
				+ userAdresses.get(0).getWards().getAdministrativeUnits().getShortName() + " "
				+ userAdresses.get(0).getWards().getName() + " "
				+ userAdresses.get(0).getDistricts().getAdministrativeUnits().getShortName() + " "
				+ userAdresses.get(0).getDistricts().getName() + " "
				+ userAdresses.get(0).getProvinces().getAdministrativeUnits().getShortName() + " "
				+ userAdresses.get(0).getProvinces().getName());
		modelMap.put("newOrder", newOrder);
		return "customer/pages/cart/checkout";
	}

	@PostMapping("/apply-total-voucher")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> applyTotalVoucher(@RequestBody Map<String, String> payload) {
		String voucherId = payload.get("voucherId");
		String currentTotal = payload.get("currentTotal");
		Map<String, Object> response = new HashMap<>();

		double discountAmount = 0;
		boolean voucherIsValid = false;
		Vouchers voucher = vouchersService.findById(Integer.parseInt(voucherId));
		if (voucher.getDiscountUnit().equals("percent")) {
			discountAmount = Double.parseDouble(currentTotal) * voucher.getDiscountValue();
		} else if (voucher.getDiscountUnit().equals("money")) {
			discountAmount = voucher.getDiscountValue();
		}
		if (discountAmount > voucher.getMaxDiscountAmount()) {
			discountAmount = voucher.getMaxDiscountAmount();
		}
		if (discountAmount > 0) {
			voucherIsValid = true;
		}

		if (voucherIsValid) {
			response.put("success", true);
			response.put("discountAmount", discountAmount);
			response.put("voucherId", voucherId);
			response.put("message", "Voucher applied successfully.");
		} else {
			response.put("success", false);
			response.put("message", "Mã voucher không hợp lệ hoặc đã hết hạn.");
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/apply-voucher")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> applyVoucher(@RequestBody Map<String, String> payload) {
		
		String vendorId = payload.get("vendorId");
		String voucherId = payload.get("voucherId");
		String userId = payload.get("userId");
		String cartItemsIdses = payload.get("cartItemsIdses");
		String[] cartItemIds = cartItemsIdses.split(",");
		List<CartItems> cartItems = new ArrayList<>();
		for(String i : cartItemIds) {
			cartItems.add(cartService.findCartItemsById(Integer.parseInt(i)));
		}
		List<Coupons> allCoupons = couponsService.findAllCoupons();
		Map<String, Object> response = new HashMap<>();

		double oldSubTotal = 0;
		Vouchers voucher = vouchersService.findById(Integer.parseInt(voucherId));
		List<Carts> userCarts = cartService.findCartByUserId(Integer.parseInt(userId));
		
			for (CartItems cartItem : cartItems) {
				if (cartItem.getProducts().getVendors().getId() == Integer.parseInt(vendorId)) {
					for (Coupons coupon : allCoupons) {
						if (coupon.getProducts() != null
								&& coupon.getProducts().getId() == cartItem.getProducts().getId()) {
							if (coupon.getDiscountUnit().equals("percent")) {
								oldSubTotal += (cartItem.getProducts().getPrice()
										- (cartItem.getProducts().getPrice() * coupon.getDiscountValue()))
										* cartItem.getQuantity();
								break;
							} else if (coupon.getDiscountUnit().equals("money")) {
								oldSubTotal += (cartItem.getProducts().getPrice()
										- coupon.getDiscountValue() * cartItem.getQuantity());
								break;
							}
						} else {
							oldSubTotal += cartItem.getProducts().getPrice() * cartItem.getQuantity();
							break;
						}
					}
				}
			}
		

		boolean voucherIsValid = false;
		double discountAmount = 0;
		if (voucher.getDiscountUnit().equals("percent")) {
			discountAmount = oldSubTotal * voucher.getDiscountValue();
		} else if (voucher.getDiscountUnit().equals("money")) {
			discountAmount = oldSubTotal - voucher.getDiscountValue();
		}
		System.out.println(oldSubTotal);
		if (discountAmount > voucher.getMaxDiscountAmount()) {
			discountAmount = voucher.getMaxDiscountAmount();
		}
		if (discountAmount > 0) {
			voucherIsValid = true;
		}
		if (voucherIsValid) {
			response.put("success", true);
			response.put("discountAmount", discountAmount);
			response.put("message", "Voucher applied successfully.");
		} else {
			response.put("success", false);
			response.put("message", "Mã voucher không hợp lệ hoặc đã hết hạn.");
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/updatecartitemquantity/{cartItemId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateCartItemQuantity(@PathVariable int cartItemId,
			@RequestBody Map<String, Integer> payload) {
		Map<String, Object> response = new HashMap<>();
		List<Double> vendorEarning = new ArrayList<>();
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
			CartItems cartItem = cartService.findCartItemsById(cartItemId);
			cartItem.setQuantity((int) newQuantity);
			cartService.saveCartItems(cartItem);

			response.put("success", true);
			response.put("newQuantity", newQuantity);
			response.put("message", "Cập nhật số lượng thành công.");

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			System.err.println("Lỗi khi cập nhật cart item " + cartItemId + ": " + e.getMessage());
			response.put("success", false);
			response.put("message", "Không thể cập nhật số lượng. Vui lòng thử lại.");
			return ResponseEntity.internalServerError().body(response);
		}
	}

	@PostMapping({ "process-checkout" })
	public String processCheckout(Authentication authentication, @ModelAttribute("newOrder") Orders newOrder,
			@AuthenticationPrincipal UserDetails userDetails, @RequestParam("cartItems") String cartItems,
			@RequestParam("vouchers") String vouchers, @RequestParam("voucherEventura") String voucherEventura, @RequestParam("shippingAmount") double shippingAmount) {
		// Order
		Users user = null;
		if (userDetails != null) {
			user = userService.findByEmail(userDetails.getUsername());
		} else if (authentication != null) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			user = userService.findByEmail(accountOAuth2User.getEmail());
		}
		newOrder.setUsers(user);
		newOrder.setPaymentDate(new Date());
		newOrder.setCreatedAt(new Date());
		newOrder.setUpdatedAt(new Date());
		newOrder.setName(
				newOrder.getUsers().getUsername() + "_Order_" + RandomStringCode.generateRandomAlphaNumeric(8));
		newOrder.setTotalAmount(newOrder.getTotalAmount() + shippingAmount);
		// Oder Items
		// Save Order
		if (orderService.saveOrder(newOrder)) {
			// find Order Items
			if (!cartItems.equals("")) {
				List<CartItems> checkoutList = new ArrayList();
				List<Coupons> allCoupons = couponsService.findAllCoupons();
				Map<Vendors, List<CartItemsDTO>> groupedCheckoutList = new HashMap<>();
				String[] splitedIds = cartItems.split(",");
				for (int i = 0; i < splitedIds.length; i++) {
					checkoutList.add(cartService.findCartItemsById(Integer.parseInt(splitedIds[i])));
				}
				for (CartItems item : checkoutList) {
					double originalPrice = item.getProducts().getPrice();
					double afterDiscountPrice = originalPrice;
					boolean hasDiscount = false;
					
					for (Coupons coupon : allCoupons) {
						if (coupon.getProducts() != null
								&& coupon.getProducts().getId() == item.getProducts().getId()) {

							CouponsCampaigns couponsCampaigns = campaignRedeemtionService
									.findAllCouponsCampaignsByCouponsId(coupon.getId()).get(0);
							couponsCampaigns.setRedeemUsedQty(1);
							if (campaignRedeemtionService.saveCouponCampaign(couponsCampaigns)) {
								coupon.setQuantity(coupon.getQuantity() - 1);
								if (couponsService.save(coupon)) {
									OrdersCampaignsId ordersCampaignsId = new OrdersCampaignsId(newOrder.getId(),
											couponsCampaigns.getCampaignRedemptions().getId());
									OrdersCampaigns ordersCampaigns = new OrdersCampaigns(ordersCampaignsId,
											couponsCampaigns.getCampaignRedemptions(), newOrder, new Date());
									if (!campaignRedeemtionService.saveOrderCampaign(ordersCampaigns)) {
										System.out.println("Save Order Campaign wrong " + coupon.getId());										
									} 
								} else {
									System.out.println("Save Coupon wrong " + coupon.getId());
								}

							} else {
								System.out.println("Save Coupon Campaign wrong " + coupon.getId());
							}

							if (coupon.getDiscountUnit().equals("percent")) {
								afterDiscountPrice = originalPrice - (originalPrice * coupon.getDiscountValue());
							} else if (coupon.getDiscountUnit().equals("money")) {
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
						ProductVariants productVariant = productVariantService.findById(Integer.parseInt(variantId));
						productVariant.setQuantity(productVariant.getQuantity()-item.getQuantity());
						if(productVariant.getQuantity() <0) {
							productVariant.setQuantity(0);
						}
						productVariantService.saveProductVariants(productVariant);
						combinationStringBuilder.append(value);

						// Append a hyphen if it's not the last element
						if (i < combinationArray.length - 1) {
							combinationStringBuilder.append("-");
						}
					}
					String combinationString = combinationStringBuilder.toString();
					CartItemsDTO dto = new CartItemsDTO(item, afterDiscountPrice, originalPrice, hasDiscount,
							combinationString);
					Products product = productService.findById(item.getProducts().getId());
					product.setQuantity(product.getQuantity()-item.getQuantity());
					if(product.getQuantity()<0) {
						product.setQuantity(0);
					}
					productService.save(product);
					Vendors vendor = item.getProducts().getVendors();
					groupedCheckoutList.computeIfAbsent(vendor, k -> new ArrayList<>()).add(dto);
					// save Order Item
					OrderItems orderItem = new OrderItems();
					orderItem.setCreatedAt(new Date());
					orderItem.setUpdatedAt(new Date());
					orderItem.setOrders(newOrder);
					orderItem.setProducts(dto.getCartItem().getProducts());
					orderItem.setProductVariants(dto.getCartItem().getProductVariants());
					orderItem.setQuantity(dto.getCartItem().getQuantity());
					if (dto.isHasDiscount()) {
						orderItem.setPrice(afterDiscountPrice);
					} else {
						orderItem.setPrice(originalPrice);
					}
					// Save Order Item
					
					if(orderItemService.saveOrderItems(orderItem)) {
						OrderItemsOrderStatusId orderItemsOrderStatusId = new OrderItemsOrderStatusId(orderItem.getId(),orderStatusService.findById(1).getId());
						OrderItemsOrderStatus orderItemsOrderStatus = new OrderItemsOrderStatus(orderItemsOrderStatusId, orderItem, orderStatusService.findById(1), new Date());
						if(!orderStatusService.saveOrderItemsOrderStatus(orderItemsOrderStatus)) {							
							System.out.println("save order item order status wrong " + orderItem.getId());						
						}
					}else {
						System.out.println("save order item wrong " + orderItem.getId());						
					}
					if(cartService.deleteCartItem(item)) {
						if(cartService.findCartById(item.getCarts().getId()).getCartItemses().isEmpty()) {
							if(!cartService.deleteCart(cartService.findCartById(item.getCarts().getId()))){								
								System.out.println("delete cart wrong " + item.getId());						
							}
						}
												
					}else {
						System.out.println("delete cart item wrong " + item.getId());						
						
					}
				}
				// find applied voucher
				List<Vouchers> appliedVoucher = new ArrayList<>();
				if (!vouchers.equals("")) {
					String[] voucherIds = vouchers.split(",");
					for (int i = 0; i < voucherIds.length; i++) {
						Vouchers voucher = vouchersService.findById(Integer.parseInt(voucherIds[i]));
						VouchersCampaigns vouchersCampaigns = campaignRedeemtionService
								.findAllVouchersCampaignsByVouchersId(voucher.getId()).get(0);
						vouchersCampaigns.setRedeemUsedQty(1);
						if (campaignRedeemtionService.saveVoucherCampaign(vouchersCampaigns)) {
							voucher.setQuantity(voucher.getQuantity() - 1);
							if(vouchersService.save(voucher)) {
								OrdersCampaignsId ordersCampaignsId = new OrdersCampaignsId(newOrder.getId(),
										vouchersCampaigns.getCampaignRedemptions().getId());
								OrdersCampaigns ordersCampaigns = new OrdersCampaigns(ordersCampaignsId,
										vouchersCampaigns.getCampaignRedemptions(), newOrder, new Date());
								if(!campaignRedeemtionService.saveOrderCampaign(ordersCampaigns)) {
									System.out.println("save Order campaign voucher wrong " + voucher.getId());
								}
							} else {
								System.out.println("save voucher wrong " + voucher.getId());
							}
								
						} else {
							System.out.println("save voucher campaign wrong " + voucher.getId());
						}
						appliedVoucher.add(voucher);
					}
				}
				// calculate commission
				List<Commissions> commissionBeforeTotalVoucher = new ArrayList<>();
				double totalCommission = 0;
				for (Map.Entry<Vendors, List<CartItemsDTO>> entry : groupedCheckoutList.entrySet()) {
					Vendors vendor = entry.getKey();
					List<CartItemsDTO> cartItemsList = entry.getValue();
					double vendorOrderTotal = 0;

					for (CartItemsDTO dto : cartItemsList) {
						if (dto.isHasDiscount()) {
							vendorOrderTotal += dto.getAfterDiscountPrice() * dto.getCartItem().getQuantity();
						} else {
							vendorOrderTotal += dto.getOriginalPrice() * dto.getCartItem().getQuantity();
						}
					}
					if (!appliedVoucher.isEmpty()) {
						for (Vouchers voucher : appliedVoucher) {
							if (voucher.getVendors().getId() == vendor.getId()) {
								if (voucher.getDiscountUnit().equals("percent")) {
									if (vendorOrderTotal * voucher.getDiscountValue() > voucher
											.getMaxDiscountAmount()) {
										vendorOrderTotal -= voucher.getMaxDiscountAmount();
									} else {
										vendorOrderTotal -= vendorOrderTotal * voucher.getDiscountValue();
									}
								} else if (voucher.getDiscountUnit().equals("money")) {
									vendorOrderTotal -= voucher.getDiscountValue();
								}
							}
						}
					}
					double vendorOrderAfterVoucher = vendorOrderTotal * vendor.getVendorSettings().getCommissionValue();
					VendorEarnings vendorEarning = new VendorEarnings();
					vendorEarning.setCreatedAt(new Date());
					vendorEarning.setUpdatedAt(new Date());
					vendorEarning.setOrders(newOrder);
					vendorEarning.setVendors(vendor);
					vendorEarning.setAmount(vendorOrderAfterVoucher);
					if(!vendorService.saveVendorEarning(vendorEarning)) {
						System.out.println("save vendor earning wrong " + vendorEarning.getId());
					}
					Commissions commission = new Commissions();
					commission.setVendors(vendor);
					commission.setOrders(newOrder);
					commission.setAmount(vendorOrderTotal - vendorOrderAfterVoucher);
					System.out.println("commission Ammout: "+commission.getAmount());
					commission.setCreatedAt(new Date());
					commission.setUpdatedAt(new Date());
					commissionBeforeTotalVoucher.add(commission);
					totalCommission += commission.getAmount();
					System.out.println("commission totalCommission	: "+totalCommission);
				}
				if (!voucherEventura.equals("")) {
					Vouchers voucher = vouchersService.findById(Integer.parseInt(voucherEventura));
					VouchersCampaigns vouchersCampaigns = campaignRedeemtionService
							.findAllVouchersCampaignsByVouchersId(voucher.getId()).get(0);
					vouchersCampaigns.setRedeemUsedQty(1);
					if (campaignRedeemtionService.saveVoucherCampaign(vouchersCampaigns)) {
						voucher.setQuantity(voucher.getQuantity() - 1);
						if(vouchersService.save(voucher)) {
							OrdersCampaignsId ordersCampaignsId = new OrdersCampaignsId(newOrder.getId(),
									vouchersCampaigns.getCampaignRedemptions().getId());
							OrdersCampaigns ordersCampaigns = new OrdersCampaigns(ordersCampaignsId,
									vouchersCampaigns.getCampaignRedemptions(), newOrder, new Date());
							if(!campaignRedeemtionService.saveOrderCampaign(ordersCampaigns)) {
								System.out.println("save Order campaign voucher wrong " + voucher.getId());
							}
						} else {
							System.out.println("save voucher wrong " + voucher.getId());
						}
							
					} else {
						System.out.println("save voucher campaign wrong " + voucher.getId());
					}
					double discountAmount = 0;
					if (voucher.getDiscountUnit().equals("percent")) {
						discountAmount = totalCommission * voucher.getDiscountValue();
					} else if (voucher.getDiscountUnit().equals("money")) {
						discountAmount = voucher.getDiscountValue();
					}
					if (discountAmount > voucher.getMaxDiscountAmount()) {
						discountAmount = voucher.getMaxDiscountAmount();
					}

					boolean noAmmountBigger = true;
					for (Commissions commission : commissionBeforeTotalVoucher) {
						if (commission.getAmount() > discountAmount) {
							commission.setAmount(commission.getAmount() - discountAmount);
							noAmmountBigger = false;
							break;
						}
					}
					if(noAmmountBigger) {
						for (Commissions commission : commissionBeforeTotalVoucher) {
							commission.setAmount(0);
						}
					}
				}
				for (Commissions commissions : commissionBeforeTotalVoucher) {
					System.out.println("save commission amout sau khi  tính toán "+commissions.getAmount());		
					commissions.setAmount(commissions.getAmount() + shippingAmount);
					if(!commissionsService.saveCommission(commissions)) {
						System.out.println("save commission wrong "+commissions.getId());
					}
				}
			}

		} else {
			System.out.println("Save Order wrong");
		}
		return "redirect:/customer/cart/cartitems";
	}
}