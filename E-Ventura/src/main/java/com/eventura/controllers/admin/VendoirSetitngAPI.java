package com.eventura.controllers.admin;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventura.dtos.CategoryDTO;
import com.eventura.dtos.UserAddressDTO;
import com.eventura.dtos.VendorSettingDTO;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.UserAddress;
import com.eventura.entities.VendorSettings;
import com.eventura.repositories.VendorSettingRepository;
import com.eventura.services.CategoryService;
import com.eventura.services.VendorSettingService;

@RestController
@RequestMapping("/vendor-settings")
public class VendoirSetitngAPI {

    @Autowired
    private VendorSettingService vendorSettingService;

    @PostMapping("/update")
    public ResponseEntity<?> updateVendorSetting(@RequestBody VendorSettingDTO updateDto) {
        VendorSettings optionalSetting = vendorSettingService.findById(updateDto.getId());

        if (optionalSetting != null) {

            switch (updateDto.getFieldName()) {
                case "vendorType":
                	optionalSetting.setVendorType(updateDto.getValue());
                    break;
                case "commissionValue":
                    try {
                        // The JavaScript now ensures 'value' is already the correct decimal (0.0 - 1.0)
                        double commissionValue = Double.parseDouble(updateDto.getValue());
                        // Add server-side validation as a fallback (good practice)
                        if (commissionValue < 0.0 || commissionValue > 1.0) {
                            return ResponseEntity.badRequest().body("{\"message\": \"Commission value must be between 0 and 1.\"}");
                        }
                        optionalSetting.setCommissionValue(commissionValue);
                    } catch (NumberFormatException e) {
                        return ResponseEntity.badRequest().body("{\"message\": \"Invalid commission value format.\"}");
                    }
                    break;
                default:
                    return ResponseEntity.badRequest().body("{\"message\": \"Invalid field name for update.\"}");
            }

            vendorSettingService.save(optionalSetting);
            return ResponseEntity.ok().body("{\"message\": \"Update successful\"}");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/update-status")
    public ResponseEntity<?> updateVendorSettingStatus(@RequestBody VendorSettingDTO updateDto) {
    	VendorSettings optionalSetting = vendorSettingService.findById(updateDto.getId());

        if (optionalSetting != null ) {

            if ("null".equalsIgnoreCase(updateDto.getDeletedAt())) {
                // Set deletedAt to null (active status)
            	optionalSetting.setDeletedAt(null);
            } else {
                // Set deletedAt to current date (inactive/deleted status)
                // You might parse a timestamp if sent from frontend, but new Date() is simpler here
            	optionalSetting.setDeletedAt(new Date()); // Using java.util.Date
                // If using LocalDateTime: setting.setDeletedAt(LocalDateTime.now());
            }

            vendorSettingService.save(optionalSetting);
            return ResponseEntity.ok().body("{\"message\": \"Status updated successfully\"}");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}