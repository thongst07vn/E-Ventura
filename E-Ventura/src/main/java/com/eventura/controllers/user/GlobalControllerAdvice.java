package com.eventura.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.eventura.configurations.AccountOAuth2User;
import com.eventura.entities.CartItems;
import com.eventura.entities.Carts;
import com.eventura.entities.Users;
import com.eventura.services.CartService;
import com.eventura.services.UserService;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CartService cartService; // Your cart service
    @Autowired
    private UserService userService;
    /**
     * Adds the cart item count to the model of every request.
     */
    @ModelAttribute("cartItemCount")
    public int cartItemCount(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails) {
    	Users user = null;
    	if (userDetails != null) {
			user = userService.findByEmail(userDetails.getUsername());
		} else if (authentication != null) {
			AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
			user = userService.findByEmail(accountOAuth2User.getEmail());
		}	             
         if(user != null) {
             List<Carts> userCarts = cartService.findCartByUserId(user.getId());
             int cartItems = 0;
             if (!userCarts.isEmpty()) {
                 for(Carts cart : userCarts) {
                 	cartItems += cartService.findAllItemsByCartId(cart.getId()).size();
                 }              
                 return cartItems;
             }   
         }
              
        // Return 0 if the user is not authenticated or has no cart
        return 0;
    }
}