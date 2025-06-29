package com.eventura.dtos;

import com.eventura.entities.CartItems;

public class CartItemsDTO {
    private CartItems cartItem;
    private double afterDiscountPrice;
    private double originalPrice; // Thêm trường này
    private boolean hasDiscount; // Thêm trường này
    private String combination;
    // Existing constructor (can be updated or kept)
    public CartItemsDTO(CartItems cartItem, double afterDiscountPrice, String combination) {
        this.cartItem = cartItem;
        this.afterDiscountPrice = afterDiscountPrice;
        this.originalPrice = cartItem.getProducts().getPrice();
        this.hasDiscount = (afterDiscountPrice != originalPrice);
        this.combination = combination;
    }

    // New constructor with full parameters
    public CartItemsDTO(CartItems cartItem, double afterDiscountPrice, double originalPrice, boolean hasDiscount, String combination) {
        this.cartItem = cartItem;
        this.afterDiscountPrice = afterDiscountPrice;
        this.originalPrice = originalPrice;
        this.hasDiscount = hasDiscount;
        this.combination = combination;
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

	public String getCombination() {
		return combination;
	}

	public void setCombination(String combination) {
		this.combination = combination;
	}
    
}