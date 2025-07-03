package com.eventura.controllers.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.entities.Vouchers;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductService;
import com.eventura.services.UserService;
import com.eventura.services.VendorProductCategoryService;
import com.eventura.services.VendorService;
import com.eventura.services.VouchersService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorVoucherController")
@RequestMapping("vendor/voucher")
public class VoucherController  {
	
	@Autowired
	private VouchersService vouchersService;
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
	
	@GetMapping("add")
	public String voucherAdd(ModelMap modelMap) {
		modelMap.put("currentPage", "promotion");

		return "vendor/pages/voucher/add";
	}
	
	@GetMapping("edit")
	public String voucherEdit(ModelMap modelMap) {
		modelMap.put("currentPage", "promotion");

		return "vendor/pages/voucher/edit";
	}
	
}
