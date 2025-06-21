package com.eventura.configurations;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.eventura.entities.Users;
import com.eventura.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private UserService userService; 
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
		String email = accountOAuth2User.getEmail();
		String name = accountOAuth2User.getName();
		if(userService.findByEmail(email)==null) {
			Users user = new Users();
			user.setEmail(email);
			user.setUsername(name);
			if(accountOAuth2User.getAvatar() != null) {
				user.setAvatar(accountOAuth2User.getAvatar());
			}
			user.setCreatedAt(new Date());
			userService.save(user);
		}
		super.onAuthenticationSuccess(request, response, authentication);	
	}
	
}
