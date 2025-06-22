package com.eventura.controllers.user;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventura.configurations.AccountOAuth2User;
import com.eventura.entities.Users;
import com.eventura.services.UserService;

@RestController
@RequestMapping("api/password")
public class PasswordApiController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/checkpassword")
	public Boolean checkPassword(@RequestBody String inputPassword, @AuthenticationPrincipal UserDetails userDetails,Authentication authentication){
		
		String[] partsArray = inputPassword.replaceAll("[{}\"]", "").split(":");
		List<String> passwordArray = Arrays.asList(partsArray);
		Users currentUser = null;
	    // 1. Centralize currentUser retrieval
	    if (userDetails != null) {
	        currentUser = userService.findByEmail(userDetails.getUsername());
	        if(currentUser.getPassword() == null) {
	        	currentUser.setPassword(BCrypt.hashpw("123456789@T", BCrypt.gensalt()));
	        }
	    } else if (authentication != null && authentication.getPrincipal() instanceof AccountOAuth2User) {
	        AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
	        currentUser = userService.findByEmail(accountOAuth2User.getEmail());
	    }
	    return BCrypt.checkpw(passwordArray.get(1),currentUser.getPassword());
	}
}
