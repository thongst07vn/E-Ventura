package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eventura.entities.CartItems;
import com.eventura.entities.Carts;
import com.eventura.entities.ProductReviews;
import com.eventura.entities.Products;
import com.eventura.repositories.CartItemsRepository;
import com.eventura.repositories.CartRepository;
import com.eventura.repositories.ProductRepository;
import com.eventura.repositories.ProductReviewsRepository;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductReviewsRepository productReviewsRepository;
	
	@Autowired
	private CartItemsRepository cartItemsRepository;
	@Autowired
	private CartRepository cartRepository;

	@Override
	public List<CartItems> findAllItemsByCartId(int id) {		
		return cartItemsRepository.findAllCartItemsByCartId(id);
	}

	@Override
	public List<Carts> findCartByUserId(int id) {

		return cartRepository.findCartByUserId(id);
	}

	@Override
	public boolean saveCartItems(CartItems cartItems) {
		try {
			cartItemsRepository.save(cartItems);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveCart(Carts carts) {
		try {
			cartRepository.save(carts);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	

}
