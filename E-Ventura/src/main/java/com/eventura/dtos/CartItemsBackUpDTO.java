package com.eventura.dtos;

import com.eventura.entities.CartItems;

public class CartItemsBackUpDTO {
    private CartItems cartItem;
    private double afterDiscountPrice;
    private double originalPrice; // Thêm trường này
    private boolean hasDiscount; // Thêm trường này
    
    // Existing constructor (can be updated or kept)
    public CartItemsBackUpDTO(CartItems cartItem, double afterDiscountPrice) {
        this.cartItem = cartItem;
        this.afterDiscountPrice = afterDiscountPrice;
        this.originalPrice = cartItem.getProducts().getPrice();
        this.hasDiscount = (afterDiscountPrice != originalPrice);
    }

    // New constructor with full parameters
    public CartItemsBackUpDTO(CartItems cartItem, double afterDiscountPrice, double originalPrice, boolean hasDiscount) {
        this.cartItem = cartItem;
        this.afterDiscountPrice = afterDiscountPrice;
        this.originalPrice = originalPrice;
        this.hasDiscount = hasDiscount;
    }
    
    // Getters and setters
    public CartItems getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItems cartItem) {
        this.cartItem = cartItem;
    }

    public double getAfterDiscountPrice() {
        return afterDiscountPrice;
    }

    public void setAfterDiscountPrice(double afterDiscountPrice) {
        this.afterDiscountPrice = afterDiscountPrice;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public boolean isHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }
}