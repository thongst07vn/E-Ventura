package com.eventura.configurations;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class AccountOAuth2User implements OAuth2User {

	private OAuth2User oauth2User;
	
	public AccountOAuth2User(OAuth2User oauth2User) {
		this.oauth2User = oauth2User;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return oauth2User.getAuthorities();
	}

	@Override
	public String getName() {
		return oauth2User.getAttribute("name");
	}
	
	public String getFullname() {
		return oauth2User.getAttribute("name");
	}
	public String getEmail() {
		return oauth2User.getAttribute("email");
	}
	public String getAvatar() {
		return oauth2User.getAttribute("picture");
	}

	@Override
	public String toString() {
		return "AccountOAuth2User [oauth2User=" + oauth2User + "]";
	}
	
}
