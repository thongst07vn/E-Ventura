package com.eventura.controllers.vendor;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.eventura.entities.OrdersCampaigns;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.Vendors;
import com.eventura.entities.Vouchers;
import com.eventura.entities.VouchersCampaigns;
import com.eventura.services.CampaignRedeemtionService;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.eventura.services.VendorProductCategoryService;
import com.eventura.services.VendorService;
import com.eventura.services.VouchersService;
import com.eventura.helpers.RandomStringCode;

import jakarta.servlet.http.HttpSession;

@Controller("vendorVoucherController")
@RequestMapping("vendor/voucher")
public class VoucherController  {
	
	@Autowired
	private VendorService vendorService;
	@Autowired
	private VouchersService vouchersService;
	@Autowired
	private CampaignRedeemtionService campaignRedeemtionService;
	@Autowired
	private ProductService productService;
	/*===================== VOUCHER =====================*/
	@GetMapping("list")
	public String voucherList(Model model, ModelMap modelMap, HttpSession session, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "show_all") String filter) {
		model.addAttribute("currentPage", "promotion");
		
		 Integer vendorId = (Integer) session.getAttribute("vendorId");
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Vouchers> voucherPage;
		switch (filter) {
			case "expired":
				voucherPage = vouchersService.findAllVoucherExpiredByVendorId(vendorId, pageable);
				break;
			case "invalid":
				voucherPage = vouchersService.findAllVoucherInValidByVendorId(vendorId, pageable);
				break;
			case "valid":
				voucherPage = vouchersService.findAllVoucherValidByVendorId(vendorId, pageable);
				break;
			case "show_all":
			default:
				voucherPage = vouchersService.findByVendorId(vendorId, pageable);
				break;
		}
		modelMap.put("vouchers", voucherPage.getContent());
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", voucherPage.getTotalPages());
		model.addAttribute("lastPageIndex", voucherPage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("selectedFilter", filter);
		
		model.addAttribute("voucher", new Vouchers());
		
		return "vendor/pages/voucher/list";
	}
	
	@GetMapping("searchByRedeemAllow")
	public String searchByKeyword(Model model, ModelMap modelMap, HttpSession session, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "show_all") String status) {
		model.addAttribute("currentPage", "promotion");
		
		 Integer vendorId = (Integer) session.getAttribute("vendorId");
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Vouchers> voucherPage;
		
		switch (status) {
		case "enable":
			voucherPage = vouchersService.findAllVoucherEnableByVendorId(vendorId, pageable);
			break;
		case "disable":
			voucherPage = vouchersService.findAllVoucherDisableByVendorId(vendorId, pageable);
			break;
		case "show_all":
		default:
			voucherPage = vouchersService.findByVendorId(vendorId, pageable);
			break;
	}
		
		modelMap.put("vouchers", voucherPage.getContent());
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", voucherPage.getTotalPages());
		model.addAttribute("lastPageIndex", voucherPage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("selectedStatus", status);
		model.addAttribute("voucher", new Vouchers());
		
		return "vendor/pages/voucher/list";
	}
	
	@PostMapping("add")
	public String voucherAdd(@ModelAttribute("vouchers") Vouchers vouchers, 
							 RedirectAttributes redirectAttributes, HttpSession session) {
		 Integer vendorId = (Integer) session.getAttribute("vendorId");
		 Vendors vendor = vendorService.findById(vendorId);

		if(vouchers.getDiscountUnit().equals("percent")){			
			vouchers.setDiscountValue(vouchers.getDiscountValue()/100);
		}
		vouchers.setVendors(vendor);
		vouchers.setRedeemAllowed(false);
		vouchers.setCreatedAt(new Date());
		vouchers.setUpadetedAt(new Date());
		
		if (vouchersService.save(vouchers)) {
			SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
			CampaignRedemptions campaignRedemptions = new CampaignRedemptions("voucher" + "_" + vendor.getName().toUpperCase() + "_" + formatter.format(vouchers.getCreatedAt()), new Date(), new Date());
			if(campaignRedeemtionService.saveCampaignRedeemtion(campaignRedemptions)) {
				for(int i = 0; i < vouchers.getQuantity(); i++) {
					VouchersCampaigns vouchersCampaigns = new VouchersCampaigns( vendor.getName().toUpperCase() + formatter.format(vouchers.getCreatedAt())+RandomStringCode.generateRandomAlphaNumeric(8),campaignRedemptions, vouchers, 1, 0);
					campaignRedeemtionService.saveVoucherCampaign(vouchersCampaigns);
				}
				redirectAttributes.addFlashAttribute("sweetAlert", "success");
				redirectAttributes.addFlashAttribute("message", "Voucher add successfully!");
				return "redirect:/vendor/voucher/list";
			}else {				
				redirectAttributes.addFlashAttribute("sweetAlert", "error");
				redirectAttributes.addFlashAttribute("message", "Failed to add!");
				return "redirect:/vendor/voucher/list";
			}
		}else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Failed to add!");
			return "redirect:/vendor/voucher/list";
		}
	}
	
	@GetMapping("detail/{id}")
	public String voucherDetails(Model model, ModelMap modelMap, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int pageSize, @PathVariable("id")int campaignId) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "redemptionDate"));
		List<VouchersCampaigns> vouchersCampaigns = campaignRedeemtionService.findAllVouchersCampaignsByVouchersIdWithredeemUsedQty(campaignId);
		Page<OrdersCampaigns> orderCampaignPage;

		// Check if the list is not empty before trying to get the first element
		if (vouchersCampaigns != null && !vouchersCampaigns.isEmpty()) { // Added !vouchersCampaigns.isEmpty()
		    // It's good practice to also check if getCampaignRedemptions() is not null
		    // though the NoSuchElementException is specifically from getFirst()
		    if (vouchersCampaigns.get(0).getCampaignRedemptions() != null) {
		        orderCampaignPage = campaignRedeemtionService.findOrderByCampaignRedeem(vouchersCampaigns.get(0).getCampaignRedemptions().getId(), pageable);
		        // It's possible findOrderByCampaignRedeem returns null, so handle that
		        if (orderCampaignPage == null) {
		            orderCampaignPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
		        }
		    } else {
		        orderCampaignPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
		    }
		} else {
		    orderCampaignPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
		}
		modelMap.put("orderCampaigns", orderCampaignPage.getContent());
		model.addAttribute("currentPages", page);
		model.addAttribute("totalPages", orderCampaignPage.getTotalPages());
		model.addAttribute("lastPageIndex", orderCampaignPage.getTotalPages() - 1);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("campaignId", campaignId);
		model.addAttribute("currentPage", "coupon");
		return "vendor/pages/voucher/detail";
	}
	
	@GetMapping("delete/{id}")
	public String delete(@PathVariable("id") int id,
					      RedirectAttributes redirectAttributes) {
		
		Vouchers voucher = vouchersService.findById(id);
		voucher.setDeletedAt(new Date());
		if(vouchersService.save(voucher)) {
			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Delete voucher successfully!");
		}else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Delete voucher failed!");
		}

		return "redirect:/vendor/voucher/list";
	}
	
}
