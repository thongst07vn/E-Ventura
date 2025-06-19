package com.eventura.configurations;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println(authentication);
		AccountOAuth2User accountOAuth2User = (AccountOAuth2User) authentication.getPrincipal();
		String email = accountOAuth2User.getEmail();
		String name = accountOAuth2User.getName();
		String avatar = accountOAuth2User.getAvatar();
		System.out.println("account email: " + email);
		System.out.println("account name: " + name);
		System.out.println("account info:" + accountOAuth2User.toString());
		super.onAuthenticationSuccess(request, response, authentication);
//		response.sendRedirect("/customer/home");		
	}
	
}
