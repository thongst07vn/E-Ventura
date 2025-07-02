package com.eventura.controllers.admin; // Adjust package as needed

import com.eventura.entities.Coupons;
import com.eventura.services.CouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController // Use @RestController for REST APIs returning JSON
@RequestMapping("/admin/coupon")
public class CouponAPI { // You might want to create a separate REST controller or add to existing
                                         // admin controller

    @Autowired
    private CouponsService couponsService; // Assuming you have a CouponRepository

    @PostMapping("/updateRedeemAllowed/{id}")
    public ResponseEntity<Map<String, String>> updateRedeemAllowed(@PathVariable int id, @RequestBody Map<String, Boolean> payload) {
        Coupons optionalCoupon = couponsService.findById(id);
        Map<String, String> response = new HashMap<>();

        if (optionalCoupon != null) {
        	Boolean redeemAllowed = payload.get("redeemAllowed");// Get the boolean value from the payload

            if (redeemAllowed != null) {
            	optionalCoupon.setRedeemAllowed(redeemAllowed);
                couponsService.save(optionalCoupon);
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