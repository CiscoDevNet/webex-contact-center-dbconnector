/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.oauth.resolvers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import com.cisco.app.oauth.config.OAuthClientConfiguration;

 /**
  * OAuth2AuthorizationRequestResolver class 
  * @author jiwyatt
  * @since 12/12/2020
  *
  */
public class AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
	Logger logger = LoggerFactory.getLogger(AuthorizationRequestResolver.class);

	private ClientRegistrationRepository repository;
	
	private OAuthClientConfiguration configuration;
	
	private OAuth2AuthorizationRequestResolver resolver;
	
	private static final String STATE = "dbconnectorAuth";
		
    public AuthorizationRequestResolver(ClientRegistrationRepository repository, OAuthClientConfiguration configuration) {
    	this.repository = repository;
    	this.configuration = configuration;
        this.resolver = new DefaultOAuth2AuthorizationRequestResolver(repository, "/oauth2/authorization");
    }
    
	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		 OAuth2AuthorizationRequest authRequest = resolver.resolve(request);
	        if(authRequest != null) {
	            authRequest = customizeAuthorizationRequest(authRequest);
	        }
	        return authRequest;
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
		OAuth2AuthorizationRequest authRequest = resolver.resolve(request, clientRegistrationId);
        if(authRequest != null) {
            authRequest = customizeAuthorizationRequest(authRequest);
        }
        return authRequest;
	}

	private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest request) {   

    	return OAuth2AuthorizationRequest
    		      .from(request)
    		      .redirectUri(configuration.getRedirectUrl())
    		      .scope(configuration.getServiceScopes())
    		      .state(STATE)
    		      .authorizationRequestUri(getEncodedAuthorizationUri())
    		      .build();
    }
    
    private String getEncodedAuthorizationUri() {
		String authUrl = String.valueOf("");
		
		try {
			
			authUrl = String.format("%s%s?response_type=code&client_id=%s&scope=%s&state=%s&redirect_uri=%s", 
						configuration.getIdentityBrokerUrl(),
						configuration.getAuthorizationUri(),
						configuration.getClientId(),
						URLEncoder.encode(configuration.getServiceScopes(), StandardCharsets.UTF_8.toString()),
						STATE,
						URLEncoder.encode(configuration.getRedirectUrl(), StandardCharsets.UTF_8.toString()));
			logger.info("authUrl:" + authUrl);
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		}
		
		return authUrl;
	}
}
