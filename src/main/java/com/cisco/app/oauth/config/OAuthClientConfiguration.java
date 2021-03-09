/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * OAuth Client Configuration
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
@Configuration
@ConfigurationProperties("oauth.client")
public class OAuthClientConfiguration {

	private String clientName;

	private String clientId;

	private String clientSecret;

	private String serviceScopes;

	private String identityServiceUrl;

	private String identityBrokerUrl;

	private String authorizationUri;

	private String userProfileUri;

	private String accessTokenUri;

	private String validateTokenUri;

	private String revokeTokenUri;

	private String logoutUri;

	private String redirectUrl;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getServiceScopes() {
		return serviceScopes;
	}

	public void setServiceScopes(String serviceScopes) {
		this.serviceScopes = serviceScopes;
	}

	public String getIdentityServiceUrl() {
		return identityServiceUrl;
	}

	public void setIdentityServiceUrl(String identityServiceUrl) {
		this.identityServiceUrl = identityServiceUrl;
	}

	public String getIdentityBrokerUrl() {
		return identityBrokerUrl;
	}

	public void setIdentityBrokerUrl(String identityBrokerUrl) {
		this.identityBrokerUrl = identityBrokerUrl;
	}

	public String getAuthorizationUri() {
		return authorizationUri;
	}

	public void setAuthorizationUri(String authorizationUri) {
		this.authorizationUri = authorizationUri;
	}

	public String getUserProfileUri() {
		return userProfileUri;
	}

	public void setUserProfileUri(String userProfileUri) {
		this.userProfileUri = userProfileUri;
	}

	public String getAccessTokenUri() {
		return accessTokenUri;
	}

	public void setAccessTokenUri(String accessTokenUri) {
		this.accessTokenUri = accessTokenUri;
	}

	public String getValidateTokenUri() {
		return validateTokenUri;
	}

	public void setValidateTokenUri(String validateTokenUri) {
		this.validateTokenUri = validateTokenUri;
	}

	public String getRevokeTokenUri() {
		return revokeTokenUri;
	}

	public void setRevokeTokenUri(String revokeTokenUri) {
		this.revokeTokenUri = revokeTokenUri;
	}

	public String getLogoutUri() {
		return logoutUri;
	}

	public void setLogoutUri(String logoutUri) {
		this.logoutUri = logoutUri;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
