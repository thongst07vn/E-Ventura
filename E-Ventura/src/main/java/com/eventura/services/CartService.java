package com.eventura.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventura.entities.CartItems;
import com.eventura.entities.Carts;
import com.eventura.entities.ProductReviews;
import com.eventura.entities.Products;
import com.eventura.entities.Users;

public interface CartService {
	
	public List<Carts> findCartByUserId(int id);
	public List<CartItems> findAllItemsByCartId(int id);

	public boolean saveCartItems(CartItems cartItems);
	public boolean saveCart(Carts carts);
}
