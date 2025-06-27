package com.eventura.dtos;

import java.util.Date;

import com.eventura.entities.CartItems;
import com.eventura.entities.Carts;
import com.eventura.entities.Districts;
import com.eventura.entities.Products;

public class CartItemsDTO {
   
	private int productId;
	private double afterDiscountPrice;
	private int productVariantId;
		
	public CartItemsDTO() {
		super();
	}
	
	public CartItemsDTO(int productId, double afterDiscountPrice, int productVariantId) {
		super();
		this.productId = productId;
		this.afterDiscountPrice = afterDiscountPrice;
		this.productVariantId = productVariantId;
	}

	public CartItemsDTO(CartItems cartItem, double afterDiscountPrice) {
		super();
		this.productId = cartItem.getProducts().getId();
		this.afterDiscountPrice = afterDiscountPrice;
		this.productVariantId = cartItem.getProductVariants().getId();
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public double getAfterDiscountPrice() {
		return afterDiscountPrice;
	}
	public void setAfterDiscountPrice(double afterDiscountPrice) {
		this.afterDiscountPrice = afterDiscountPrice;
	}

	public int getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(int productVariantId) {
		this.productVariantId = productVariantId;
	}
	
	
	
    
	

}
