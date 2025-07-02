package com.eventura.controllers.admin; // Adjust package as needed

import com.eventura.entities.Vouchers;
import com.eventura.services.VouchersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController // Use @RestController for REST APIs returning JSON
@RequestMapping("/admin/voucher")
public class VoucherAPI { // You might want to create a separate REST controller or add to existing
                                         // admin controller

    @Autowired
    private VouchersService vouchersService; // Assuming you have a VoucherRepository

    @PostMapping("/updateRedeemAllowed/{id}")
    public ResponseEntity<Map<String, String>> updateRedeemAllowed(@PathVariable int id, @RequestBody Map<String, Boolean> payload) {
        Vouchers optionalVoucher = vouchersService.findById(id);
        Map<String, String> response = new HashMap<>();

        if (optionalVoucher != null) {
        	Boolean redeemAllowed = payload.get("redeemAllowed");// Get the boolean value from the payload

            if (redeemAllowed != null) {
            	optionalVoucher.setRedeemAllowed(redeemAllowed);
            	vouchersService.save(optionalVoucher);
                response.put("message", "Coupon redeemAllowed status updated successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Invalid payload: 'redeemAllowed' is missing.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } else {
            response.put("message", "Coupon not found with ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}