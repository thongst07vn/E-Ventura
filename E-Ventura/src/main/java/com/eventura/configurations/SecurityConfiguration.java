package com.eventura.configurations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.eventura.services.AccountService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private AccountService accountService;

	// Defines the password encoder to be used for hashing passwords.
	// BCryptPasswordEncoder is highly recommended for strong password hashing.
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	// Configures the global AuthenticationManager to use the custom UserDetailsService.
	// This tells Spring Security how to load user details for authentication.
	@Autowired
	public void configGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(accountService);
	}

	// --- Admin Security Configuration Chain ---
	// This SecurityFilterChain is responsible for securing admin-related paths and the admin login flow.
	// @Order(1) ensures this chain is processed first, as it handles more specific paths.
	@Bean
	@Order(1)
	public SecurityFilterChain adminFilterChain(HttpSecurity httpSecurity)
			throws Exception {
		return httpSecurity
			// securityMatcher defines which requests this specific filter chain will handle.
			// It will match requests for /admin/**, the admin login page, and its processing URL.
			.securityMatcher("/admin/**", "/admin/login", "/admin/process-login")
			.cors(c -> c.disable()) // Disables CORS for simplicity. In production, configure CORS appropriately.
			.csrf(c -> c.disable()) // Disables CSRF for simplicity. **Enable and handle CSRF tokens in production.**
			.authorizeHttpRequests(a -> {
				a.requestMatchers(
					"/admin/login",        // Allow unauthenticated access to the admin login page
					"/admin/process-login" // Allow unauthenticated access to the admin login processing URL
				).permitAll()
				.requestMatchers("/admin/**").hasAnyRole("ADMIN") // Require ADMIN role for all paths under /admin/
				.anyRequest().authenticated(); // Any other request matched by this chain must be authenticated
			})
			.formLogin(f -> {
				f.loginPage("/admin/login") // Specifies the custom admin login page URL
				.loginProcessingUrl("/account/admin/process-login") // URL where the admin login form submits
				.usernameParameter("email") // Name of the username parameter in the login form
				.passwordParameter("password") // Name of the password parameter in the login form
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						System.out.println("Admin Login Success for user: " + authentication.getName());
						Map<String,String> redirectUrls = new HashMap<>();
						redirectUrls.put("ROLE_ADMIN","/admin/dashboard"); // Redirect ADMINs to dashboard
						String url ="/account/admin/login?error"; // Default fallback if no matching role found
						for(GrantedAuthority authority : authentication.getAuthorities()) {
							if(redirectUrls.containsKey(authority.getAuthority())) {
								url = redirectUrls.get(authority.getAuthority());
								break;
							}
						}
						response.sendRedirect(url);
					}
				})
				.failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest request,
							HttpServletResponse response, AuthenticationException exception)
							throws IOException, ServletException {
						System.out.println("Admin Login Failure for email: " + request.getParameter("email"));
						response.sendRedirect("/account/admin/login?error"); // Redirect back to admin login with error
					}
				});
			})
			.logout(f -> {
				f.logoutUrl("/account/admin/logout") // Admin-specific logout URL
				.logoutSuccessHandler(new LogoutSuccessHandler() {
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
						System.out.println("Admin Logout Success");
						response.sendRedirect("/account/admin/login?logout"); // Redirect to admin login page after logout
					}
				});
			})
			.exceptionHandling(ex -> {
				ex.accessDeniedPage("/account/accessdenied"); // Redirect on access denied
			})
			.build();
	}

	// --- Customer/Doctor/Patient Security Configuration Chain ---
	// This SecurityFilterChain is responsible for securing customer, doctor, patient paths and their login flow.
	// @Order(2) ensures this chain is processed after the admin chain.
	@Bean
	@Order(2)
	public SecurityFilterChain customerFilterChain(HttpSecurity httpSecurity)
			throws Exception {
		return httpSecurity
			// securityMatcher defines which requests this specific filter chain will handle.
			// It will match requests for /doctor/**, /patient/**, the customer login page, and its processing URL.
			.securityMatcher("/", "/customer/**", "/customer/login", "/login", "/customer/process-login")
			.cors(c -> c.disable()) // Disables CORS for simplicity. In production, configure CORS appropriately.
			.csrf(c -> c.disable()) // Disables CSRF for simplicity. **Enable and handle CSRF tokens in production.**
			.authorizeHttpRequests(a -> {
				a.requestMatchers(
					"/",
					"/customer",
					"/customer/home",
					"/customer/login",        // Allow unauthenticated access to the customer login page
					"/login",        // Allow unauthenticated access to the customer login page
					"/customer/process-login" // Allow unauthenticated access to the customer login processing URL
				).permitAll()
				.requestMatchers("/customer/**").hasAnyRole("CUSTOMER") // Require DOCTOR role for /doctor paths // Require PATIENT role for /patient paths
				.anyRequest().authenticated(); // Any other request matched by this chain must be authenticated
			})
			.formLogin(f -> {
				f.loginPage("/login") // Specifies the custom customer login page URL
				.loginProcessingUrl("/account/customer/process-login") // URL where the customer login form submits
				.usernameParameter("email") // Name of the username parameter in the login form
				.passwordParameter("password") // Name of the password parameter in the login form
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						System.out.println("Customer/Doctor/Patient Login Success for user: " + authentication.getName());
						Map<String,String> redirectUrls = new HashMap<>();
						redirectUrls.put("ROLE_DOCTOR","/doctor/home");  // Redirect DOCTORs to their home
						redirectUrls.put("ROLE_PATIENT","/patient/home"); // Redirect PATIENTs to their home
						String url ="/account/customer/login?error"; // Default fallback if no matching role found
						for(GrantedAuthority authority : authentication.getAuthorities()) {
							if(redirectUrls.containsKey(authority.getAuthority())) {
								url = redirectUrls.get(authority.getAuthority());
								break;
							}
						}
						response.sendRedirect(url);
					}
				})
				.failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest request,
							HttpServletResponse response, AuthenticationException exception)
							throws IOException, ServletException {
						System.out.println("Customer/Doctor/Patient Login Failure for email: " + request.getParameter("email"));
						response.sendRedirect("/account/customer/login?error"); // Redirect back to customer login with error
					}
				});
			})
			.logout(f -> {
				f.logoutUrl("/account/customer/logout") // Customer-specific logout URL
				.logoutSuccessHandler(new LogoutSuccessHandler() {
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
						System.out.println("Customer/Doctor/Patient Logout Success");
						response.sendRedirect("/account/customer/login?logout"); // Redirect to customer login page after logout
					}
				});
			})
			.exceptionHandling(ex -> {
				ex.accessDeniedPage("/account/accessdenied"); // Redirect on access denied
			})
			.build();
	}

	// --- General/Public Security Configuration Chain ---
	// This chain acts as a catch-all for any requests not explicitly matched by the more specific chains above.
	// @Order(3) ensures this chain is processed last.
	@Bean
	@Order(3)
	public SecurityFilterChain generalFilterChain(HttpSecurity httpSecurity)
			throws Exception {
		return httpSecurity
			// No securityMatcher at the top level for this chain.
			// It will implicitly match '/**' for any requests not already handled by higher-order chains.
			.cors(c -> c.disable()) // Disables CORS for simplicity.
			.csrf(c -> c.disable()) // Disables CSRF for simplicity. **Enable and handle CSRF tokens in production.**
			.authorizeHttpRequests(a -> {
				a.requestMatchers(
					"/",             // Allow unauthenticated access to the root
					"/client/**",
					"/admin/assets/**",					// Allow unauthenticated access to static resources like CSS/JS
					"/account/register", // Allow unauthenticated access to registration page
					"/account/accessdenied" // Allow unauthenticated access to access denied page
				).permitAll()
//				.requestMatchers("/account/edit").hasAnyRole("ADMIN","DOCTOR","PATIENT") // Shared path requiring any of these roles
				.anyRequest().authenticated(); // All other requests not handled by previous chains require authentication
			})
			// This general chain typically does NOT define its own formLogin() as dedicated chains handle authentication.
			.logout(f -> {
				f.logoutUrl("/account/logout") // General logout URL for users not using specific admin/customer logout
				.logoutSuccessHandler(new LogoutSuccessHandler() {
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
						System.out.println("General Logout Success");
						// Redirect to a general login page or home after logout
						response.sendRedirect("/account/login"); // Or your main application's landing page
					}
				});
			})
			.exceptionHandling(ex -> {
				ex.accessDeniedPage("/account/accessdenied"); // Redirect on access denied
			})
			.build();
	}
}