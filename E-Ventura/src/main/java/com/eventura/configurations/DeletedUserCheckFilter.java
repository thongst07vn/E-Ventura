package com.eventura.configurations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eventura.entities.Users;
import com.eventura.services.UserService;

import java.io.IOException;

public class DeletedUserCheckFilter extends OncePerRequestFilter {

	private final UserService userService; // Assuming you have a UserService to fetch user details

	public DeletedUserCheckFilter(UserService userService) {
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Check if the user is authenticated and not an anonymous user
		if (authentication != null && authentication.isAuthenticated()
				&& !(authentication instanceof AnonymousAuthenticationToken)) {
			Object principal = authentication.getPrincipal();
			String username = null;
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else if (principal instanceof OAuth2User) {
				OAuth2User oauth2User = (OAuth2User) principal;
				username = oauth2User.getAttribute("email");

			}
			Users user = userService.findByEmail(username);
			if (user != null && user.getDeletedAt() != null) {
				// User is deleted, invalidate session and redirect
				SecurityContextHolder.clearContext(); // Clear security context
				request.getSession().invalidate(); // Invalidate session
				String redirectUrl = "";
				if (request.getRequestURI().startsWith("/admin")) {
					redirectUrl = "/admin/login?error=Account%20Baned";
				} else if (request.getRequestURI().startsWith("/vendor")) {
					redirectUrl = "/vendor/account/login?error=Account%20Baned";
				} else {
					redirectUrl = "/customer/login?error=Account%20Baned";
				}
				response.sendRedirect(redirectUrl);
				return; // Stop further processing
			}

		}
		filterChain.doFilter(request, response); // Continue the filter chain
	}
}
