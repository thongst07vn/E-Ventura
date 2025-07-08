package com.eventura.controllers.user;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.eventura.configurations.AccountOAuth2User;
import com.eventura.dtos.CartItemsDTO;
import com.eventura.entities.CartItems;
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
import com.eventura.services.PayPalService;
import com.eventura.services.ProductService;
import com.eventura.services.ProductVariantService;
import com.eventura.services.UserAddressService;
import com.eventura.services.UserService;
import com.eventura.services.VendorService;
import com.eventura.services.VouchersService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping({"/paypal"})
@CrossOrigin("/http://localhost:8386/")
public class PayPalController {
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
	
	@Value("${base_url}")
	private static String base_url;
	@Autowired
	private PayPalService payPalService;
	
	private static final String SUCCESS_URL= "http://localhost:8386/paypal/success";
	private static final String CANCEL_URL= "http://localhost:8386/paypal/cancel";
	
	@PostMapping("/pay")
	public String makePayment(@RequestParam double amount,
			@RequestBody Orders newOrder,
			@RequestParam("cartItems") String cartItems,
			@RequestParam("vouchers") String vouchers, 
			@RequestParam("voucherEventura") String voucherEventura,
			@RequestParam("userId") String userId,
			HttpSession httpSession) {
				Users user = userService.findById(Integer.parseInt(userId));			
				System.out.println(user.getId());
				httpSession.setAttribute("newOrder", newOrder);
				httpSession.setAttribute("cartItems", cartItems);
				httpSession.setAttribute("vouchers", vouchers);
				httpSession.setAttribute("voucherEventura", voucherEventura);
				httpSession.setAttribute("user", user);
		try {
			Payment payment = payPalService.createPayment(amount, "USD", "paypal", "sale", "payment description", CANCEL_URL, SUCCESS_URL);
			
			for(Links links : payment.getLinks()) {
				if(links.getRel().equals("approval_url")) {
					return "Redirect to: "+links.getHref();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Error processing the payment";
	}
	
	
	@GetMapping("/success")
	public ResponseEntity<?>  paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpSession httpSession) {
		try {
			Payment payment = payPalService.execute(paymentId, payerId);
			if(payment.getState().equals("approved")){
				//Order
				Orders newOrder = (Orders)httpSession.getAttribute("newOrder");
				Users user = (Users) httpSession.getAttribute("user");
				String cartItems = (String) httpSession.getAttribute("cartItems");
				String vouchers = (String) httpSession.getAttribute("vouchers");
				String voucherEventura = (String) httpSession.getAttribute("voucherEventura");
				newOrder.setUsers(user);
				newOrder.setPaymentDate(new Date());
				newOrder.setCreatedAt(new Date());
				newOrder.setUpdatedAt(new Date());
				newOrder.setName(
						newOrder.getUsers().getUsername() + "_Order_" + RandomStringCode.generateRandomAlphaNumeric(8));
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
							if(!commissionsService.saveCommission(commissions)) {
								System.out.println("save commission wrong "+commissions.getId());
							}
						}
					}

				} else {
					System.out.println("Save Order wrong");
				}
				//redirect
				
				httpSession.removeAttribute("newOrder");
				httpSession.removeAttribute("cartItems");
				httpSession.removeAttribute("vouchers");
				httpSession.removeAttribute("voucherEventura");
				httpSession.removeAttribute("user");
				String redirectUrl = UriComponentsBuilder.fromUriString("/customer/cart/cartitems")
						.queryParam("paypalSuccess", "true")
						.queryParam("paymentId", paymentId) // Pass paymentId for potential backend use
						.queryParam("PayerID", payerId)     // Pass PayerID for potential backend use
						.build().toUriString();
				
				
				HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create(redirectUrl));                
                return new ResponseEntity<>(headers, HttpStatus.FOUND); 
				
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed");
			}
		} catch (PayPalRESTException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	@GetMapping("/")
	public String home() {
		return "payment";
	}
	@GetMapping("/cancel")
	public String cancel() {
		return "payment canceled";
	}
	
	
}
