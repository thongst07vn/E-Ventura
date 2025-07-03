package com.eventura.controllers.vendor;

import com.eventura.entities.ProductAttributes;
import com.eventura.entities.ProductVariants;
import com.eventura.services.ProductAttributeService;
import com.eventura.services.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
@RestController
@RequestMapping("/vendor/api/product-variant")
public class ProductVariantAPI {

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private ProductAttributeService productAttributeService;

    // API để cập nhật thông tin ProductVariant
    @PutMapping("/update/{productId}/{attributeId}/{id}")
    public ResponseEntity<?> updateProductVariant(
            @PathVariable("productId") Integer productId, // Truyền productId từ URL
            @PathVariable("attributeId") Integer attributeId, // Truyền attributeId từ URL
            @PathVariable("id") Integer id,               // ProductVariant ID
            @RequestParam("field") String field, 
            @RequestParam("value") String value) {

        // In ra productId để kiểm tra
        System.out.println("Product ID: " + productId);

        // Tìm ProductVariant theo id
        Optional<ProductVariants> productVariantOptional = productVariantService.findById(id);
        if (!productVariantOptional.isPresent()) {
            return ResponseEntity.badRequest().body("ProductVariant not found");
        }

        ProductVariants productVariant = productVariantOptional.get();

        // Kiểm tra trùng lặp attribute name
        if ("attribute".equals(field)) {
        	ProductVariants existingVariant = productVariantService.findByProductIdAndProductAttributeAndValue(productId, attributeId, value);
        	if(existingVariant != null) {
                return ResponseEntity.badRequest().body("Attribute name already exists");
        	}
        }

        // Cập nhật các trường khác (value, quantity)
        switch (field) {
        	case "attribute":
        		ProductAttributes attributes = productAttributeService.findByName(value);
        		if(attributes == null) {
        			attributes = new ProductAttributes();
        			attributes.setName(value);
        			attributes.setCreatedAt(new Date());
        			attributes.setUpdatedAt(new Date());
        	        productAttributeService.save(attributes);
        		}else {
            		productVariant.setProductAttributes(attributes); // Giả sử muốn cập nhật tên thuộc tính

        		}
        		break;
            case "value":
                productVariant.setValue(value);
                break;
            case "quantity":
                productVariant.setQuantity(Integer.parseInt(value));
                break;
            default:
                return ResponseEntity.badRequest().body("Invalid field");
        }

        // Lưu lại thay đổi
        productVariantService.saveProductVariants(productVariant);

        // Trả về ResponseEntity với thông báo thành công
        return ResponseEntity.ok("ProductVariant updated successfully");
    }
}

