package com.eventura.controllers.vendor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventura.entities.CampaignRedemptions;
import com.eventura.entities.Coupons;
import com.eventura.entities.CouponsCampaigns;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.Vendors;
import com.eventura.entities.Vouchers;
import com.eventura.entities.VouchersCampaigns;
import com.eventura.services.CampaignRedeemtionService;
import com.eventura.services.CategoryService;
import com.eventura.services.CouponsService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.eventura.services.VendorProductCategoryService;
import com.eventura.services.VendorService;
import com.example.demo.helpers.RandomStringCode;

import jakarta.servlet.http.HttpSession;

@Controller("vendorCouponController")
@RequestMapping("vendor/coupon")
public class CouponController  {
	
	@Autowired
	private CouponsService couponService;
	@Autowired
	private ProductService productService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private CampaignRedeemtionService campaignRedeemtionService;
	
	/*===================== Coupon =====================*/
	@GetMapping("list")
	public String voucherList(Model model, ModelMap modelMap, HttpSession session, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "show_all") String filter) {
		model.addAttribute("currentPage", "promotion");
		
		 Integer vendorId = (Integer) session.getAttribute("vendorId");
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Coupons> couponPage;
		switch (filter) {
			case "expired":
				couponPage = couponService.findAllCouponExpiredByVendorId(vendorId, pageable);
				break;
			case "invalid":
				couponPage = couponService.findAllCouponInValidByVendorId(vendorId, pageable);
				break;
			case "valid":
				couponPage = couponService.findAllCouponValidByVendorId(vendorId, pageable);
				break;
			case "show_all":
			default:
				couponPage = couponService.findByVendorId(vendorId, pageable);
				break;
		}
		modelMap.put("coupons", couponPage.getContent());
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", couponPage.getTotalPages());
		model.addAttribute("lastPageIndex", couponPage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("selectedFilter", filter);
		model.addAttribute("coupon", new Coupons());
		model.addAttribute("products", productService.findByVendorId(vendorId));

		
		return "vendor/pages/coupon/list";
	}
	
	@GetMapping("searchByRedeemAllow")
	public String searchByKeyword(Model model, ModelMap modelMap, HttpSession session, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "show_all") String status) {
		model.addAttribute("currentPage", "promotion");
		
		 Integer vendorId = (Integer) session.getAttribute("vendorId");
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Coupons> couponPage;
		
		switch (status) {
		case "enable":
			couponPage = couponService.findAllCouponsEnableByVendorId(vendorId, pageable);
			break;
		case "disable":
			couponPage = couponService.findAllCouponsDisableByVendorId(vendorId, pageable);
			break;
		case "show_all":
		default:
			couponPage = couponService.findByVendorId(vendorId, pageable);
			break;
	}
		
		modelMap.put("coupons", couponPage.getContent());
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", couponPage.getTotalPages());
		model.addAttribute("lastPageIndex", couponPage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("selectedStatus", status);
		model.addAttribute("coupon", new Coupons());
		
		return "vendor/pages/coupon/list";
	}
	
	@PostMapping("add")
	public String voucherAdd(@ModelAttribute("coupon") Coupons coupon, 
							 RedirectAttributes redirectAttributes, HttpSession session) {
		 Integer vendorId = (Integer) session.getAttribute("vendorId");
		 Vendors vendor = vendorService.findById(vendorId);

		if(coupon.getDiscountUnit().equals("percent")){			
			coupon.setDiscountValue(coupon.getDiscountValue()/100);
		}
		coupon.setVendors(vendor);
		coupon.setRedeemAllowed(false);
		coupon.setCreatedAt(new Date());
		coupon.setUpdatedAt(new Date());
		coupon.setProducts(productService.findById(coupon.getProducts().getId()));
		
		if (couponService.save(coupon)) {
			SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
			CampaignRedemptions campaignRedemptions = new CampaignRedemptions("coupon" + "_" + vendor.getName().toUpperCase() + "_" + formatter.format(coupon.getCreatedAt()), new Date(), new Date());
			if(campaignRedeemtionService.saveCampaignRedeemtion(campaignRedemptions)) {
				for(int i = 0; i < coupon.getQuantity(); i++) {
					CouponsCampaigns couponsCampaigns = new CouponsCampaigns( vendor.getName().toUpperCase() + formatter.format(coupon.getCreatedAt())+RandomStringCode.generateRandomAlphaNumeric(8),campaignRedemptions, coupon, 1, 0);
					campaignRedeemtionService.saveCouponCampaign(couponsCampaigns);
				}
				redirectAttributes.addFlashAttribute("sweetAlert", "success");
				redirectAttributes.addFlashAttribute("message", "Coupon add successfully!");
				return "redirect:/vendor/coupon/list";
			}else {				
				redirectAttributes.addFlashAttribute("sweetAlert", "error");
				redirectAttributes.addFlashAttribute("message", "Failed to add!");
				return "redirect:/vendor/coupon/list";
			}
		}else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Failed to add!");
			return "redirect:/vendor/coupon/list";
		}
	}
	
	@GetMapping("delete/{id}")
	public String delete(@PathVariable("id") int id,
					      RedirectAttributes redirectAttributes) {
		
		Coupons coupon = couponService.findById(id);
		coupon.setDeletedAt(new Date());
		if(couponService.save(coupon)) {
			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Delete coupon successfully!");
		}else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Delete coupon failed!");
		}

		return "redirect:/vendor/coupon/list";
	}
	
	
	@GetMapping("edit")
	public String couponEdit(ModelMap modelMap) {
		modelMap.put("currentPage", "promotion");

		return "vendor/pages/coupon/edit";
	}
}
