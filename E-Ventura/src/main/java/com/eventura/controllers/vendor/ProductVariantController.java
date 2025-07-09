
package com.eventura.controllers.vendor;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventura.entities.ProductAttributes;
import com.eventura.entities.ProductVariants;
import com.eventura.entities.Products;
import com.eventura.services.CategoryService;
import com.eventura.services.ProductAttributeService;
import com.eventura.services.ProductService;
import com.eventura.services.ProductVariantService;

import jakarta.servlet.http.HttpSession;

@Controller("vendorProductVariantController")
@RequestMapping("vendor/productVariant")
public class ProductVariantController  {
	
	@Autowired
	private ProductVariantService productVariantService;
	@Autowired
	private ProductAttributeService productAttributeService;
	@Autowired
	private ProductService productService;
	
	/*===================== PRODUCT VARIANT =====================*/
	@PostMapping("add")
	public String add(@ModelAttribute("productVariant") ProductVariants productVariant,
					  @ModelAttribute("productAttribute") ProductAttributes productAttribute,
					  @RequestParam("productId") int productId,
					  @ModelAttribute("product") Products product,
					   ModelMap modelMap, RedirectAttributes redirectAttributes) {
		System.out.println("Product ID: " + product.getId());
		System.out.println("Product Variant Value: " + productVariant.getValue());
		System.out.println("Product Variant Quantity: " + productVariant.getQuantity());
		System.out.println(productVariantService.getTotalVariantQuantityByProductId(productId));
		System.out.println(productService.findById(productId).getQuantity());
		
		if(productVariantService.getTotalVariantQuantityByProductId(productId) + productVariant.getQuantity() > productService.findById(productId).getQuantity()){
			redirectAttributes.addFlashAttribute("sweetAlert", "warning");
			redirectAttributes.addFlashAttribute("message", "Total variant quantity must less than or equal to total product quantity");
			return "redirect:/vendor/product/edit/" + productId;
		}

		
		// Kiểm tra sự tồn tại của ProductAttribute với name
	    ProductAttributes attribute = productAttributeService.findByName(productAttribute.getName());
	    

	    
	    if (attribute == null) {
	        // Nếu không có, tạo mới ProductAttribute
	        attribute = new ProductAttributes();
	        attribute.setName(productAttribute.getName());
	        attribute.setCreatedAt(new Date());
	        attribute.setUpdatedAt(new Date());
	        productAttributeService.save(attribute);  // Lưu ProductAttribute mới
		    System.out.println("atrtibute Trong if: " + attribute.getId() + '-' + attribute.getName());

	    } else {
	        // Nếu có, sử dụng attribute đã tồn tại
	        System.out.println("Found existing ProductAttribute with name: " + attribute.getId() + '-' + attribute.getName());
	    }
	    
	    ProductVariants variant = productVariantService.findByProductIdAndProductAttributeAndValue(productId, attribute.getId(), productVariant.getValue());
	    if(variant == null) {
	    	  // Gán Product và ProductAttribute cho ProductVariant
		    productVariant.setProducts(productService.findById(productId));
		    productVariant.setProductAttributes(attribute);
		    productVariant.setCreatedAt(new Date());
		    productVariant.setUpdatedAt(new Date());
	    }else {
	    	 redirectAttributes.addFlashAttribute("sweetAlert", "warning");
			redirectAttributes.addFlashAttribute("message", "Variant has existed");
			return "redirect:/vendor/product/edit/" + productId;
	    }	    

		
		if(productVariantService.saveProductVariants(productVariant)) {
			Products product1 = productService.findById(productId);
			product1.setDeletedAt(null);
			productService.save(product1);
			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Add Variant Successfully");
		}else {
			redirectAttributes.addFlashAttribute("sweetAlert", "error");
			redirectAttributes.addFlashAttribute("message", "Add Variant Failed");
		}
		
		
		return "redirect:/vendor/product/edit/" + productId;
	}
	
	@GetMapping("delete/{id}")
	public String delete(@PathVariable("id") int id, @RequestParam("productId") int productId,
						 ModelMap modelMap, RedirectAttributes redirectAttributes) {
		modelMap.put("currentPage", "product");

		if (productVariantService.delete(id)) {
			redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Delete Variant Successfully");
	    } else {
	    	redirectAttributes.addFlashAttribute("sweetAlert", "success");
			redirectAttributes.addFlashAttribute("message", "Delete Variant Successfully");
	    }
        return "redirect:/vendor/product/edit/" + productId;

	}
	
	@GetMapping("edit/{id}")
	public String edit(@PathVariable("id") int id, ModelMap modelMap) {
		modelMap.put("currentPage", "product");

		modelMap.put("productVariant", productVariantService.findById(id));
		
		
		
		
		return "vendor/pages/productVariant/edit";
	}
	
}
