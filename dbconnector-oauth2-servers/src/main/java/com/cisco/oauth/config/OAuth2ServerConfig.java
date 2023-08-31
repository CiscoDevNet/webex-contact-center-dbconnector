package com.cisco.oauth.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

/**
 * @author: Jim Wyatt
 */
@Configuration
@EnableWebSecurity
public class OAuth2ServerConfig {
	
	@Value("${spring.security.oauth2.client.registration.webexcc.client-id}")
	private String clientId;
	
	@Value("${spring.security.oauth2.client.registration.webexcc.client-secret}")
	private String clientSecret;
	
	@Value("${spring.security.oauth2.client.registration.webexcc.scope}")
	private String scopes;
	
	
	@Value("${spring.security.oauth2.client.resourceserver.jwt.issuer-uri}")
	private String issuerUri;
	
	@Value("${userDetails.webexcc.authorities}")
	private String webexccAuthority;


	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		return http.exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))).build();
	}

	@Bean
	RegisteredClientRepository registeredClientRepository() {
		List<RegisteredClient> registeredClients = new ArrayList<RegisteredClient>();
		// RegisteredClient ONE - used for https://admin.webex.com/wxcc/integrations/connectors 
		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId(clientId)
				.clientSecret("{noop}"+clientSecret)
				.clientAuthenticationMethods(s -> {
					s.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
					s.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
		})
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.scope(webexccAuthority)
				.clientSettings(ClientSettings.builder()
						.requireAuthorizationConsent(true)
						.requireProofKey(false).build())
				.tokenSettings(TokenSettings.builder()
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // Generate JWT token
				.idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)// idTokenSignatureAlgorithm: signature algorithm
				.accessTokenTimeToLive(Duration.ofDays(30 * 60))// accessTokenTimeToLive：access_token validity period
				.refreshTokenTimeToLive(Duration.ofDays(60 * 60))// refreshTokenTimeToLive：refresh_token validity period
				.reuseRefreshTokens(true)// reuseRefreshTokens: whether to reuse refresh tokens
				.build()).build();
		registeredClients.add(registeredClient);


		return new InMemoryRegisteredClientRepository(registeredClients);
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().issuer(issuerUri).build();
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = Jwks.generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	static class Jwks {

		private Jwks() {
		}

		public static RSAKey generateRsa() {
			KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
		}
	}

	static class KeyGeneratorUtils {

		private KeyGeneratorUtils() {
		}

		static KeyPair generateRsaKey() {
			KeyPair keyPair;
			try {
				KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
				keyPairGenerator.initialize(2048);
				keyPair = keyPairGenerator.generateKeyPair();
			} catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
			return keyPair;
		}
	}
}
