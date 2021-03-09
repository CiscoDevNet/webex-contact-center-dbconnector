/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.oauth.providers;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

/**
 * AuthenticationProviders class
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
public enum AuthenticationProviders {

	DBCONNECTOR {
		@Override
		public Builder getBuilder(String registrationId) {		
			ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.BASIC);
//			builder.scope("Identity:SCIM");
			builder.scope("Identity:SCIM", "Identity:OAuthToken", "Identity:OAuthClient", "cloud-contact-center:pod_conv");
			builder.authorizationUri("https://idbroker.webex.com/idb/oauth2/v1/authorize");
			builder.tokenUri("https://idbroker.webex.com/idb/oauth2/v1/access_token");
			builder.userInfoUri("https://identity.webex.com/identity/scim/v1/Users/me");
			builder.userNameAttributeName("userName");
			builder.clientName("dbconnector");
			return builder;
		}
	},
	GITHUB {
		@Override
		public Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId,	ClientAuthenticationMethod.BASIC);
			builder.scope("read:user");
			builder.authorizationUri("https://github.com/login/oauth/authorize");
			builder.tokenUri("https://github.com/login/oauth/access_token");
			builder.userInfoUri("https://api.github.com/user");
			builder.userNameAttributeName("id");
			builder.clientName("GitHub");
			return builder;
		}
	},
	FACEBOOK {
		@Override
		public Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId,	ClientAuthenticationMethod.POST);
			builder.scope("public_profile", "email");
			builder.authorizationUri("https://www.facebook.com/v2.8/dialog/oauth");
			builder.tokenUri("https://graph.facebook.com/v2.8/oauth/access_token");
			builder.userInfoUri("https://graph.facebook.com/me");
			builder.userNameAttributeName("id");
			builder.clientName("Facebook");
			return builder;
		}
	},
	GOOGLE {
		@Override
		public Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId,	ClientAuthenticationMethod.BASIC);
			builder.scope("openid", "profile", "email");
			builder.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth");
			builder.tokenUri("https://www.googleapis.com/oauth2/v4/token");
			builder.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs");
			builder.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo");
			builder.userNameAttributeName(IdTokenClaimNames.SUB);
			builder.clientName("Google");
			return builder;
		}
	},
	OKTA {
		@Override
		public Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId,	ClientAuthenticationMethod.BASIC);
			builder.scope("openid", "profile", "email", "address", "phone");
			builder.userNameAttributeName(IdTokenClaimNames.SUB);
			builder.clientName("Okta");
			return builder;
		}
	};

	private static final String DEFAULT_LOGIN_REDIRECT_URL = "{baseUrl}/login/oauth2/code/{registrationId}";

	protected final ClientRegistration.Builder getBuilder(
			String registrationId, ClientAuthenticationMethod method) {
		ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
		builder.clientAuthenticationMethod(method);
		builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
		builder.redirectUriTemplate(DEFAULT_LOGIN_REDIRECT_URL);
		return builder;
	}

	public abstract ClientRegistration.Builder getBuilder(String registrationId);
}
