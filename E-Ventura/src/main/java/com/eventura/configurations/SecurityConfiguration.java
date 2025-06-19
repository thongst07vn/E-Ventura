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


	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(accountService);
	}

	// --- Admin Security Configuration Chain ---
	@Bean
	@Order(1)
	public SecurityFilterChain adminFilterChain(HttpSecurity httpSecurity)
			throws Exception {
		return httpSecurity
			.securityMatcher("/admin/**")
			.cors(c -> c.disable()) 
			.csrf(c -> c.disable()) 
			.authorizeHttpRequests(a -> {
				a.requestMatchers(
					"/admin/login",       
					"/admin/register",
					"/admin/process-login",
					"/admin/assets/**"
				).permitAll()
				.requestMatchers("/admin/**").hasAnyRole("ADMIN") 
				.anyRequest().authenticated();
			})
			.formLogin(f -> {
				f.loginPage("/admin/login") 
				.loginProcessingUrl("/admin/process-login") 
				.usernameParameter("email") 
				.passwordParameter("password") 
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						System.out.println("Admin Login Success for user: " + authentication.getName());
						Map<String,String> redirectUrls = new HashMap<>();
						redirectUrls.put("ROLE_ADMIN","/admin/dashboard"); // Redirect ADMINs to dashboard
						String url ="/admin/login?error"; // Default fallback if no matching role found
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
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					                                    AuthenticationException exception) throws IOException {
						System.out.println("Admin Login Failure for email: " + request.getParameter("email"));
						response.sendRedirect("/admin/login?error");
					}
				});
			})
			.logout(f -> {
				f.logoutUrl("/admin/logout") // Admin-specific logout URL
				.logoutSuccessHandler(new LogoutSuccessHandler() {
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
						System.out.println("Admin Logout Success");
						response.sendRedirect("/admin/login?logout"); // Redirect to admin login page after logout
					}
				});
			})
			.exceptionHandling(ex -> {
				ex.accessDeniedPage("/accessdenied/404"); // Redirect on access denied
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
			.securityMatcher("/", "/customer/**", "/customer/login", "/login", "/customer/register", "/customer/process-login")
			.cors(c -> c.disable()) // Disables CORS for simplicity. In production, configure CORS appropriately.
			.csrf(c -> c.disable()) // Disables CSRF for simplicity. **Enable and handle CSRF tokens in production.**
			.authorizeHttpRequests(a -> {
				a.requestMatchers(
					"/",
					"/customer/home",
					"/customer/login",   
					"/customer/register",
					"/login",        // Allow unauthenticated access to the customer login page
					"/customer/process-login" // Allow unauthenticated access to the customer login processing URL
				).permitAll()
				.requestMatchers("/customer/**").hasAnyRole("CUSTOMER") // Require DOCTOR role for /doctor paths // Require PATIENT role for /patient paths
				.anyRequest().authenticated(); // Any other request matched by this chain must be authenticated
			})
			.formLogin(f -> {
				f.loginPage("/customer/login") // Specifies the custom customer login page URL
				.loginProcessingUrl("/customer/process-login") // URL where the customer login form submits
				.usernameParameter("email") // Name of the username parameter in the login form
				.passwordParameter("password") // Name of the password parameter in the login form
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						System.out.println("Admin Login Success for user: " + authentication.getName());
						Map<String,String> redirectUrls = new HashMap<>();
						redirectUrls.put("ROLE_CUSTOMER","/customer/home"); 
						String url ="/customer/login?error"; 
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
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					                                    AuthenticationException exception) throws IOException {
						System.out.println("Customer Login Failure for email: " + request.getParameter("email"));
						response.sendRedirect("/customer/login?error");
					}
				});
			})
			.logout(f -> {
				f.logoutUrl("/customer/logout") // Admin-specific logout URL
				.logoutSuccessHandler(new LogoutSuccessHandler() {
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
						System.out.println("Customer Logout Success");
						response.sendRedirect("/customer/login?logout"); // Redirect to admin login page after logout
					}
				});
			})
			.exceptionHandling(ex -> {
				ex.accessDeniedPage("/customer/login?error"); // Redirect on access denied
			})
			.build();
	}
	
	@Bean
	@Order(3)
	public SecurityFilterChain vendorFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
			.securityMatcher("/vendor/**")
			.cors(c -> c.disable())
			.csrf(c -> c.disable())
			.authorizeHttpRequests(a -> {
				a.requestMatchers(
					"/vendor/account/login",
					"/vendor/account/register",
					"/vendor/process-login",
					"/vendor/assets/**"
				).permitAll()
				.requestMatchers("/vendor/**").hasRole("VENDOR")
				.anyRequest().authenticated();
			})
			.formLogin(f -> {
				f.loginPage("/vendor/account/login")
				.loginProcessingUrl("/vendor/account/process-login")
				.usernameParameter("email")
				.passwordParameter("password")
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						System.out.println("Vendor Login Success for user: " + authentication.getName());
						Map<String,String> redirectUrls = new HashMap<>();
						redirectUrls.put("ROLE_VENDOR","/vendor/dashboard/home"); 
						String url ="/vendor/account/login?error"; 
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
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					                                    AuthenticationException exception) throws IOException {
						System.out.println("Vendor Login Failure for email: " + request.getParameter("email"));
						response.sendRedirect("/vendor/account/login?error");
					}
				});
			})
			.logout(f -> {
				f.logoutUrl("/vendor/account/logout")
				.logoutSuccessHandler(new LogoutSuccessHandler() {
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
					                            Authentication authentication) throws IOException {
						System.out.println("Vendor Logout Success");
						response.sendRedirect("/vendor/account/login?logout");
					}
				});
			})
			.exceptionHandling(ex -> {
				ex.accessDeniedPage("/accessdenied/404");
			})
			.build();
	}




	// --- General/Public Security Configuration Chain ---
	// This chain acts as a catch-all for any requests not explicitly matched by the more specific chains above.
	// @Order(3) ensures this chain is processed last.
//	@Bean
//	@Order(4)
//	public SecurityFilterChain generalFilterChain(HttpSecurity httpSecurity)
//			throws Exception {
//		return httpSecurity
//			// No securityMatcher at the top level for this chain.
//			// It will implicitly match '/**' for any requests not already handled by higher-order chains.
//			.cors(c -> c.disable()) // Disables CORS for simplicity.
//			.csrf(c -> c.disable()) // Disables CSRF for simplicity. **Enable and handle CSRF tokens in production.**
//			.authorizeHttpRequests(a -> {
//				a.requestMatchers(
//					"/",             // Allow unauthenticated access to the root
//					"/client/**",
//					"/admin/assets/**",					// Allow unauthenticated access to static resources like CSS/JS
//					"/account/register", // Allow unauthenticated access to registration page
//					"/account/accessdenied" // Allow unauthenticated access to access denied page
//				).permitAll()
////				.requestMatchers("/account/edit").hasAnyRole("ADMIN","DOCTOR","PATIENT") // Shared path requiring any of these roles
//				.anyRequest().authenticated(); // All other requests not handled by previous chains require authentication
//			})
//			// This general chain typically does NOT define its own formLogin() as dedicated chains handle authentication.
//			.logout(f -> {
//				f.logoutUrl("/account/logout") // General logout URL for users not using specific admin/customer logout
//				.logoutSuccessHandler(new LogoutSuccessHandler() {
//					@Override
//					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//							throws IOException, ServletException {
//						System.out.println("General Logout Success");
//						// Redirect to a general login page or home after logout
//						response.sendRedirect("/account/login"); // Or your main application's landing page
//					}
//				});
//			})
//			.exceptionHandling(ex -> {
//				ex.accessDeniedPage("/account/accessdenied"); // Redirect on access denied
//			})
//			.build();
//	}
}