package com.cisco.oauth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author: Jim Wyatt
 */
@Controller
public class AuthenticationController {
	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	
	
	public AuthenticationController() {
		super();
    	logger.info("public class AuthenticationController:");
	}

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object root(Authentication authentication) {
    	logger.info("/:");
//    	try {
//    		return authenticationToJson(authentication);
//		} catch (Exception e) {
//	    	return tokenToJson(authentication);
//		}
    	return "index.html";
	}

	
	@GetMapping(path = "/people/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object article(Authentication authentication) {
    	logger.info("/people/me:");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	String s = gson.toJson(authentication);
    	logger.info("/people/me:{}", s);
    	s = s.replaceAll("\"password\": \"\\{noop\\}user\",", "");
    	logger.info("/people/me:{}", s);
//    	try {
    		return s;
//		} catch (Exception e) {
//	    	return tokenToJson(authentication);
//		}
//    	return "{}";
    }


    

	

	private Object authenticationToJson(Authentication authentication) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(authentication);
	}
	
	private Object tokenToJson(Authentication authentication) {
		org.springframework.security.oauth2.jwt.Jwt jwt = (Jwt) authentication.getPrincipal();	    	
		String s = "{"
				+ "\"id\":\"" + jwt.getId() + "\","
				+ "\"subject\":\"" + jwt.getSubject() + "\","
				+ "\"token\":\"" + jwt.getTokenValue() + "\","
				+ "\"audience\":\"" + jwt.getAudience() + "\","
				+ "\"claims\":\"" + jwt.getClaims() + "\","
				+ "\"expiresAt\":\"" + jwt.getExpiresAt() + "\","
				+ "\"headers\":\"" + jwt.getHeaders() + "\","
				+ "\"issuedAt\":\"" + jwt.getIssuedAt() + "\","
				+ "\"issuer\":\"" + jwt.getIssuer() + "\","
				+ "\"notBefore\":\"" + jwt.getNotBefore() + "\""
				+ "}";
		return s;
	}	
    
}
