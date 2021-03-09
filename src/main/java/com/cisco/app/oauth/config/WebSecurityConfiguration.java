/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.oauth.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.cisco.app.oauth.providers.AuthenticationProviders;
import com.cisco.app.oauth.resolvers.AuthorizationRequestResolver;

@Configuration
@EnableWebSecurity(debug=false)
@EnableGlobalMethodSecurity(prePostEnabled = true)

/**
 * Web Security Configurer Adapter
 * @author jiwyatt
 * @since 12/12/2020
 */
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	
	private static List<String> clients = Arrays.asList("dbconnector");
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {	
		http
		.authorizeRequests()
		.antMatchers("/","/*", "/assets/**", "/rest/webexcc/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.oauth2Login()
		.authorizationEndpoint()
		.authorizationRequestResolver(new AuthorizationRequestResolver(clientRegistrationRepository(), oauthClientConfiguration()))
			.authorizationRequestRepository(cookieAuthorizationRequestRepository())
		.and()
		.clientRegistrationRepository(clientRegistrationRepository())
			.authorizedClientService(authorizedClientService())
		.and()
		.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/oauthLogout", "GET"))
			.logoutSuccessUrl(String.format("%s%s", oauthClientConfiguration().getIdentityBrokerUrl(), oauthClientConfiguration().getLogoutUri()))
			.clearAuthentication(true)
			.deleteCookies()
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID")
			.permitAll()
		.and()
		.cors()
		.and()
		.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

	private AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieAuthorizationRequestRepository() {
		return new HttpSessionOAuth2AuthorizationRequestRepository();
	}
	
	@Bean
	public OAuthClientConfiguration oauthClientConfiguration() {
		return new OAuthClientConfiguration();
	}
	
 
	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
	     	return super.userDetailsServiceBean();
	}
	
	@Bean
	public OAuth2AuthorizedClientService authorizedClientService() {	  
	    return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
	}
	
	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		List<ClientRegistration> registrations = clients.stream()
				.map(client -> getRegistration(client))
				.filter(registration -> registration != null)
				.collect(Collectors.toList());
		
		return new InMemoryClientRegistrationRepository(registrations);
	}
	
	private ClientRegistration getRegistration(String client) {
	    return AuthenticationProviders.DBCONNECTOR.getBuilder(client)
	    		.clientId(oauthClientConfiguration().getClientId())
	    		.clientSecret(oauthClientConfiguration().getClientSecret())
	    		.build();	    
	}
	
	@Bean
	public SessionRegistry sessionRegistry() {
	    SessionRegistry sessionRegistry = new SessionRegistryImpl();
	    return sessionRegistry;
	}

}
