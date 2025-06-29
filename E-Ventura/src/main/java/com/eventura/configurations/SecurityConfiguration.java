package com.eventura.configurations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eventura.entities.Users;
import com.eventura.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountOAuth2UserServices accountOAuth2UserServices;
	
	@Autowired
	private OAuth2LoginSuccessHandler auth2LoginSuccessHandler;


	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}


	@Autowired
	public void configGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userService);
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
			.securityMatcher("/admin/**", "/admin/login", "admin/register", "/admin/process-login")
			.cors(c -> c.disable()) // Disables CORS for simplicity. In production, configure CORS appropriately.
			.csrf(c -> c.disable()) // Disables CSRF for simplicity. **Enable and handle CSRF tokens in production.**
			.authorizeHttpRequests(a -> {
				a.requestMatchers(
					"/admin/login",  
					"/admin/register",
					"/admin/process-login",
					"/admin/assets/**"
					// Allow unauthenticated access to the admin login processing URL
				).permitAll()
				.requestMatchers("/admin/**").hasAnyRole("ADMIN") // Require ADMIN role for all paths under /admin/
				.anyRequest().authenticated(); 
			})
			.formLogin(f -> {
				f.loginPage("/admin/login") // Specifies the custom admin login page URL
				.loginProcessingUrl("/admin/process-login") // URL where the admin login form submits
				.usernameParameter("email") // Name of the username parameter in the login form
				.passwordParameter("password") // Name of the password parameter in the login form
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						System.out.println("Admin Login Success for user: " + authentication.getName());
						boolean isVendor = false;
				        for (GrantedAuthority authority : authentication.getAuthorities()) {
				            if ("ROLE_VENDOR".equals(authority.getAuthority()) || "ROLE_CUSTOMER".equals(authority.getAuthority())) {
				                isVendor = true;
				                break;
				            }
				        }

				        if (isVendor) {
				            // Logout the vendor user
				            SecurityContextHolder.clearContext(); // Clear security context
				            // Optionally, invalidate session
				            request.getSession().invalidate();
				            // Redirect to login with error parameter
				            response.sendRedirect("/admin/login?error=Restricted");
				            return;
				        }
						Map<String,String> redirectUrls = new HashMap<>();
						redirectUrls.put("ROLE_ADMIN","/admin/dashboard"); // Redirect ADMINs to dashboard // Redirect ADMINs to dashboard
						redirectUrls.put("ROLE_VENDOR","/admin/logout");
						redirectUrls.put("ROLE_CUSTOMER","/admin/logout");
						String url ="/admin/login?error=Wrong%20Password%20or%20Email"; // Default fallback if no matching role found
						for(GrantedAuthority authority : authentication.getAuthorities()) {
							System.out.println(authentication.getName());
							if(redirectUrls.containsKey(authority.getAuthority())) {
								System.out.println(url);
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
						response.sendRedirect("/admin/login?error=Wrong%20Password%20or%20Email"); // Redirect back to admin login with error
					}
				});
			})
			.addFilterAfter(new DeletedUserCheckFilter(userService), SecurityContextHolderFilter.class)
			.addFilterBefore(new OncePerRequestFilter() {
			    @Override
			    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			            throws ServletException, IOException {
			        String uri = request.getRequestURI();
			        if (uri.equals("/admin/login")) {
			            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
			                response.sendRedirect("/admin/dashboard");
			                return;
			            }
			        }
			        filterChain.doFilter(request, response);
			    }
			}, UsernamePasswordAuthenticationFilter.class)
			
			.logout(f -> {
				f.logoutUrl("/admin/logout") // Admin-specific logout URL
				.logoutSuccessHandler(new LogoutSuccessHandler() {
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
						System.out.println("Admin Logout Success");
						response.sendRedirect("/admin/login"); // Redirect to admin login page after logout
					}
				});
			})
			.exceptionHandling(ex -> {
				ex.accessDeniedPage("/admin/login?error=Unauthorized"); // Redirect on access denied
			}).build();
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
			.securityMatcher("/", "/customer/**", "/customer/login", "/login/**", "/customer/register", "/customer/process-login", "/oauth2/**","/api/password/**","/product/**")
			.cors(c -> c.disable()) // Disables CORS for simplicity. In production, configure CORS appropriately.
			.csrf(c -> c.disable()) // Disables CSRF for simplicity. **Enable and handle CSRF tokens in production.**
			.authorizeHttpRequests(a -> {
				a.requestMatchers(
					"/",
					"/customer/home",
					"/customer/login",   
					"/customer/register",
					"/login/**", // Allow unauthenticated access to the customer login page
					"/customer/process-login", // Allow unauthenticated access to the customer login processing URL
					"/oauth2/**"
				).permitAll()
//				.requestMatchers("/customer/**").hasAnyRole("CUSTOMER","OAUTH2_USER") // Require DOCTOR role for /doctor paths // Require PATIENT role for /patient paths
				.requestMatchers("/customer/**").hasAnyAuthority("ROLE_CUSTOMER","OAUTH2_USER") // Require DOCTOR role for /doctor paths // Require PATIENT role for /patient paths
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
						boolean isVendor = false;
				        for (GrantedAuthority authority : authentication.getAuthorities()) {
				            if ("ROLE_VENDOR".equals(authority.getAuthority()) || "ROLE_ADMIN".equals(authority.getAuthority())) {
				                isVendor = true;
				                break;
				            }
				        }

				        if (isVendor) {
				            // Logout the vendor user
				            SecurityContextHolder.clearContext(); // Clear security context
				            // Optionally, invalidate session
				            request.getSession().invalidate();
				            // Redirect to login with error parameter
				            response.sendRedirect("/customer/login?error=Restricted");
				            return;
				        }
						Map<String,String> redirectUrls = new HashMap<>();
						redirectUrls.put("ROLE_CUSTOMER","/customer/home"); 
						redirectUrls.put("ROLE_ADMIN","/customer/logout"); 
						redirectUrls.put("ROLE_VENDOR","/customer/logout"); 
						String url ="/customer/login?error=Wrong%20Password%20or%20Email"; 
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
						response.sendRedirect("/customer/login?error=Wrong%20Password%20or%20Email");
					}
				});
			})
			.oauth2Login(f->{
				f.loginPage("/customer/login")				
					.userInfoEndpoint().userService(accountOAuth2UserServices).and().successHandler(auth2LoginSuccessHandler);
			})
			.addFilterAfter(new DeletedUserCheckFilter(userService), SecurityContextHolderFilter.class)
			.addFilterBefore(new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                        throws ServletException, IOException {
                    String uri = request.getRequestURI();
                    // Only apply this logic to the customer login page
                    if (uri.equals("/customer/login") || uri.equals("/login")) { // Include /login if that's also a path for your customer login form
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        // Check if the user is authenticated and not an anonymous user
                        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                            response.sendRedirect("/customer/home");
                            return; // IMPORTANT: Stop the filter chain
                        }
                    }
                    filterChain.doFilter(request, response); // Continue the filter chain if not redirecting
                }
            }, UsernamePasswordAuthenticationFilter.class)
			.logout(f -> {
				f.logoutUrl("/customer/logout") // Customer-specific logout URL
				.logoutSuccessHandler(new LogoutSuccessHandler() {
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
						if (request.getSession() != null) {
					        request.setAttribute("hasLoggedHomeAccess",false);
					    }
						response.sendRedirect("/customer/login?logout"); // Redirect to customer login page after logout
					}
				});
			})
			.exceptionHandling(ex -> {
				ex.accessDeniedPage("/customer/login?error=Unauthorized"); // Redirect on access denied
			})
			.build();
	}
	
	@Bean
	@Order(3)
	public SecurityFilterChain vendorFilterChain(HttpSecurity httpSecurity)
			throws Exception {
		return httpSecurity
			// securityMatcher defines which requests this specific filter chain will handle.
			// It will match requests for /doctor/**, /patient/**, the customer login page, and its processing URL.
			.securityMatcher("/vendor/**", "/vendor/account/login", "/vendor/process-login")
			.cors(c -> c.disable()) // Disables CORS for simplicity. In production, configure CORS appropriately.
			.csrf(c -> c.disable()) // Disables CSRF for simplicity. **Enable and handle CSRF tokens in production.**
			.authorizeHttpRequests(a -> {
				a.requestMatchers(
					"/vendor/account/login",        // Allow unauthenticated access to the customer login page
					"/vendor/account/register",
					"/vendor/account/test",
					"/vendor/process-login",
					"/vendor/assets/**",
					"/vendor/account/verify"
				).permitAll()
				.requestMatchers("/vendor/**").hasAnyRole("VENDOR") // Require DOCTOR role for /doctor paths // Require PATIENT role for /patient paths
				.anyRequest().authenticated(); // Any other request matched by this chain must be authenticated
			})
			.formLogin(f -> {
				f.loginPage("/vendor/account/login") // Specifies the custom customer login page URL
				.loginProcessingUrl("/vendor/process-login") // URL where the customer login form submits
				.usernameParameter("email") // Name of the username parameter in the login form
				.passwordParameter("password") // Name of the password parameter in the login form
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						/* Email */
						String email = authentication.getName();
						/* users */
						Users user = userService.findByEmail(email);
						request.getSession().setAttribute("vendorId", user.getId());
						boolean isVendor = false;
				        for (GrantedAuthority authority : authentication.getAuthorities()) {
				            if ("ROLE_CUSTOMER".equals(authority.getAuthority()) || "ROLE_ADMIN".equals(authority.getAuthority())) {
				                isVendor = true;
				                break;
				            }
				        }

				        if (isVendor) {
				            // Logout the vendor user
				            SecurityContextHolder.clearContext(); // Clear security context
				            // Optionally, invalidate session
				            request.getSession().invalidate();
				            // Redirect to login with error parameter
				            response.sendRedirect("/vendor/account/login?error=Restricted");
				            return;
				        }
						Map<String,String> redirectUrls = new HashMap<>();
						redirectUrls.put("ROLE_VENDOR","/vendor/dashboard/home");  // Redirect DOCTORs to their home
						redirectUrls.put("ROLE_CUSTOMER","/vendor/account/logout");  // Redirect DOCTORs to their home
						redirectUrls.put("ROLE_ADMIN","/vendor/account/logout");  // Redirect DOCTORs to their home
						String url ="/vendor/account/login?error=Wrong%20Password%20or%20Email"; // Default fallback if no matching role found
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
						response.sendRedirect("/vendor/account/login?error=Wrong%20Password%20or%20Email"); // Redirect back to customer login with error
					}
				});
			})
			.addFilterAfter(new DeletedUserCheckFilter(userService), SecurityContextHolderFilter.class)
			.logout(f -> {
				f.logoutUrl("/vendor/account/logout") // Customer-specific logout URL
				.logoutSuccessHandler(new LogoutSuccessHandler() {
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
						response.sendRedirect("/vendor/account/login?logout"); // Redirect to customer login page after logout
					}
				});
			})
			.exceptionHandling(ex -> {
				ex.accessDeniedPage("/vendor/account/login?error=Unauthorized"); // Redirect on access denied
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
//					"/accessdenied/404" // Allow unauthenticated access to access denied page
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
//				ex.accessDeniedPage("/accessdenied/404"); // Redirect on access denied
//			})
//			.build();
//	}
}