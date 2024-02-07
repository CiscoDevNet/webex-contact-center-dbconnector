package com.cisco.oauth.config;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

	@Value("${spring.security.oauth2.client.registration.webexcc.client-id}")
	private String clientId;
	
	@Value("${userDetails.webexcc.authorities}")
	private String webexccAuthority;

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String webIssuer;

	public JwtRequestFilter() {
		super();
		logger.info("public JwtRequestFilter");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		logger.info("doFilter: HttpServletRequest:getContextPath: " + request.getRequestURL());

		try {
			Authentication oAuthentication = SecurityContextHolder.getContext().getAuthentication();
			this.logRequestDetails(httpRequest, oAuthentication);
			// internal call
			if(request.getRequestURL().toString().endsWith("/rest/webexcc/reloadRules")) {
				this.setAuthentication(request);
				filterChain.doFilter(request, response);
				return;
			}
			// this is machine to machine JWT verification
			if (oAuthentication == null || oAuthentication.getPrincipal().equals("anonymousUser")) {

				SecurityContextImpl sci = (SecurityContextImpl) httpRequest.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
				logger.debug("sci:" + sci);
				Principal principal = httpRequest.getUserPrincipal();
				logger.debug("principal:" + principal);

				String[] chunks = httpRequest.getHeader("Authorization").split("\\.");
				logger.debug("chunks:" + chunks);
				for (int i = 0; i < chunks.length; i++) {
					logger.debug(i + ":chunks:" + chunks[i]);

				}

//			    String header = new String(Base64.decodeBase64(chunks[0].toString()), "UTF-8");
				String payload = new String(Base64.decodeBase64(chunks[1].toString()), "UTF-8");

//			    logger.debug("header:" + header);

				logger.debug("payload:" + payload);
				Map<String, Object> result = new ObjectMapper().readValue(payload, HashMap.class);
//				
				logger.debug("result:nbf:" + result.get("nbf"));
				logger.debug("result:scope:" + result.get("scope"));
				logger.debug("result:iss:" + result.get("iss"));
				logger.debug("result:exp:" + result.get("exp"));
				logger.debug("result:iat:" + result.get("iat"));
				boolean b = isJwtValid(result);
				if (!b) {
					throw new Exception("isJwtValid is not valid");
				}

				setAuthentication(request);
				filterChain.doFilter(request, response);
				return;
			}

		} catch (Exception e) {
			logger.error("Request is invalid");

			//https://stackoverflow.com/questions/61989976/spring-boot-error-exceeded-maxredirects-probably-stuck-in-a-redirect-loop
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			String errorMsg = "{\"error\":\"UNAUTHORIZED\",\"statusCode\":\"" + HttpServletResponse.SC_UNAUTHORIZED + "\"}";
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMsg);
			return;
//			throw new ServletException("{\"error\":\"UNAUTHORIZED\",\"statusCode\":\"" + HttpServletResponse.SC_UNAUTHORIZED + "\"}");

		}
 	

		filterChain.doFilter(request, response);
	}

	private void logRequestDetails(HttpServletRequest httpRequest, Authentication oAuthentication) {
		logger.debug("oAuthentication             :" + oAuthentication);
		logger.debug("oAuthentication.getPrincipal:" + oAuthentication.getPrincipal());

		Enumeration<String> headerNames = httpRequest.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				logger.debug("doFilter: Header: " + httpRequest.getHeader(headerNames.nextElement()));
			}
		}
	}

	private void setAuthentication(HttpServletRequest request) {
		UserDetails userDetails = User
				.withUsername("user")
				.password("{noop}" + "user")
				.roles("USER")
				.authorities("SCOPE_dbconnector.read")
				.build();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		// After setting the Authentication in the context, we specify that the current
		// user is authenticated. So it passes the Spring Security Configurations
		// successfully.
		// MAGIC HAPPENS... HERE TO SET SPRING SECURITY STUFF ... MAGIC HAPPENS
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		logger.debug("private void setAuthentication");

	}

	private boolean isJwtValid(Map<String, Object> result) throws Exception {
		boolean b = false;
		b = isSubValid((String) result.get("sub"));
		if(!b) {return false;}
		b = isScopeValid((java.util.List<?>) result.get("scope"));
		if(!b) {return false;}
		b = areTimestampsValid((Integer) result.get("iat"), (Integer) result.get("exp"));
		if(!b) {return false;}
		b = isIssuerValid((String) result.get("iss"));
		if(!b) {return false;}
		return true;
	}

	
	private boolean isIssuerValid(String check) {
		if(webIssuer.toLowerCase().equals(check.toLowerCase())) {return true;}
		logger.error("Token issuer issue");
		return false;
	
	}

	private boolean areTimestampsValid(Integer issued, Integer expires) throws Exception {
		long now = Instant.now().getEpochSecond();
		
		if(now > issued &&  now < expires) {return true;}
		logger.error("Timestamps are not valid");
		return false;
	}

	private boolean isScopeValid(java.util.List<?> check) throws Exception{
		if(check.contains(webexccAuthority)) {return true;}
		logger.error("Scope is not valid");
		return false;
	}

	private boolean isSubValid(String check) throws Exception{
		if(clientId.equals(check)) {return true;}
		logger.error("clientId is not valid");
		return false;
	}

}
