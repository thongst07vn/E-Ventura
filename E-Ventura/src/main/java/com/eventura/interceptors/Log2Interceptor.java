package com.eventura.interceptors;

import java.util.Date;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.eventura.configurations.AccountOAuth2User;
import com.eventura.entities.ActivityLog;
import com.eventura.entities.Users;
import com.eventura.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Import HttpSession

@Component
public class Log2Interceptor implements HandlerInterceptor {
	private final UserService userService; // Assuming you have a UserService to fetch user details
	public Log2Interceptor(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
			if(user != null) {
				if (user.getRoles().getId()==3) {
					if ("/customer/home".equals(request.getRequestURI())) {
						 HttpSession session = request.getSession();
	                     Boolean hasLoggedForHome = (Boolean) session.getAttribute("hasLoggedForHome");
			            if (hasLoggedForHome == null || !hasLoggedForHome) {			            	
			            	ActivityLog activityLog = new ActivityLog();
			            	activityLog.setUsers(user);
			            	activityLog.setIpLog(request.getLocalAddr());
			            	activityLog.setCreatedAt(new Date());
			            	activityLog.setUpdatedAt(new Date());
			            	if(request.getHeader("User-Agent").contains("Window")) {
			            		activityLog.setDeviceLog("PC");
			            	}else {
			            		activityLog.setDeviceLog("Phone");
			            	}
			            	userService.saveActivityLog(activityLog);
			            	session.setAttribute("hasLoggedForHome", true);
			            }
					}
				}
			}
		}
		return true;
	}
}