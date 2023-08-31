package com.cisco.oauth.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

/**
 * @author: Jim Wyatt
 */
@Configuration
public class SecurityConfig {
	//ADMIN
	@Value("${userDetails.admin.username}")
	private String adminUsername;
	@Value("${userDetails.admin.password}")
	private String adminPassword;
	@Value("${userDetails.admin.authorities}")
	private String adminAuthorities;
	
	//USER
	@Value("${userDetails.user.username}")
	private String userUsername;
	@Value("${userDetails.user.password}")
	private String userPassword;
	@Value("${userDetails.user.authorities}")
	private String userAuthorities;

//	@Autowired
//	ExpiredTokenFilter oExpiredTokenFilter;
	
	@Autowired
	JwtRequestFilter oJwtRequestFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/people/me")
                .access(AuthorizationManagers.anyOf(
                        AuthorityAuthorizationManager.hasAnyAuthority((adminAuthorities)),
                        AuthorityAuthorizationManager.hasAnyAuthority((userAuthorities)),
                        AuthorityAuthorizationManager.hasRole(("ADMIN")),
                        AuthorityAuthorizationManager.hasRole("USER")))

                .requestMatchers("/rest/**") //API calls
                .access(AuthorizationManagers.anyOf(
                        AuthorityAuthorizationManager.hasAnyAuthority((adminAuthorities)),
                        AuthorityAuthorizationManager.hasAnyAuthority((userAuthorities)),
                        AuthorityAuthorizationManager.hasRole(("ADMIN")),
                        AuthorityAuthorizationManager.hasRole("USER")))
                
                
                .requestMatchers("/**") //WEB SITE
                .access(AuthorizationManagers.anyOf(
                        AuthorityAuthorizationManager.hasAnyAuthority((adminAuthorities)),
                        AuthorityAuthorizationManager.hasRole("ADMIN")))

                .anyRequest().authenticated()).csrf(withDefaults());
        
        http.addFilterBefore(oJwtRequestFilter, AuthorizationFilter.class);
        http.formLogin(withDefaults());
        http.csrf().disable();
        
		return http.build();
	} 
	
	



	@Bean
	UserDetailsService users() {
		List<UserDetails> users = new ArrayList<UserDetails>();
		// ONE - used for the dbConnector web page
		UserDetails user = User
				.withUsername(adminUsername)
				.password("{noop}" + adminPassword)
				.roles("ADMIN")
				.authorities(adminAuthorities)
				.build();
		users.add(user);
		// TWO - - used for the dbConnector web page
		user = User
				.withUsername(userUsername)
				.password("{noop}" + userPassword)
				.roles("USER")
				.authorities(userAuthorities)
				.build();
		users.add(user);
		return new InMemoryUserDetailsManager(users);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	

}
