/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.cisco.app.dbconnector.model.Authentication;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller for rest calls
 * 
 * @author jiwyatt
 * @since 12/12/2020
 * @see https://devportal.wxcc-us1.cisco.com/documentation/getting-started
 * @see https://app.smartsheet.com/b/form/7af787e752834bbfba604bfc85a5eff1
 */
@RestController
@RequestMapping("/")
@CrossOrigin(origins = { "${redirect_uri}" })
public class WebController {
	Logger logger = LoggerFactory.getLogger(WebController.class);

	@Value("${redirect_uri}")
	private String serverUiUrl;

	@Value("${baseURL}")
	private String baseURL;

	@Value("${response_type}")
	private String responseType;

	@Value("${client_id}")
	private String clientId;

	@Value("${redirect_uri}")
	private String redirectUri;

	@Value("${scope}")
	private String scope;

	@Value("${state}")
	private String state;

	@Value("${grant_type}")
	private String grantType;

	@Value("${client_secret}")
	private String clientSecret;

	private static final String REDIRECT_CNT = "redirectCount";
	private static final String AUTHENTICATFION = "Authentication";
	private static final String EXCEPTION = "Exception";
	private static final String ERROR = "error_description";

	public WebController() {
		super();
		logger.info("public WebController");
	}

	/**
	 * force all traffic to index.html
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/")
	public Object root(HttpServletRequest request, HttpServletResponse response,
			@RequestParam final Map<String, String> inboundParameters) {
		logger.info("root");
		logger.debug("inboundParameters:{}", inboundParameters);
		int redirectCount = 0;
		try {
			redirectCount = (int) request.getSession().getAttribute(REDIRECT_CNT);
			redirectCount++;
			request.getSession().setAttribute(REDIRECT_CNT, redirectCount);
		} catch (Exception e) {
			request.getSession().setAttribute(REDIRECT_CNT, 1);
		}
		try {
			java.util.LinkedHashMap<?, ?> map = (java.util.LinkedHashMap<?, ?>) inboundParameters;
			try {
				logger.debug("code:{}", map.get("code"));
				/**
				 * STEP 3 - already logged in STEP 2 only if authorized STEP 1 only if it is the
				 * 1st time to the web page
				 */
				Authentication check = (Authentication) request.getSession().getAttribute(AUTHENTICATFION);
				logger.debug("check:{}", check);

				if (check != null) {
					logger.info("Logged in");
					return check;
				}

				/**
				 * STEP 2 - get the user token after the authorize request
				 */

				if (map.get(ERROR) != null) {
					return map.get(ERROR);
				}

				logger.info("authorized but not logged in");
				if (redirectCount > 5) {
					// check your application.yml values
					request.getSession().setAttribute(REDIRECT_CNT, 0);
					return "Authorized but not logged in error: invalid tenant";
				}
				// this will for an exception
				map.get("code").toString();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//						
				MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
				params.add("grant_type", grantType);
				params.add("client_id", clientId);
				params.add("client_secret", clientSecret);
				params.add("code", map.get("code").toString());
				params.add("redirect_uri", redirectUri);

				HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<MultiValueMap<String, String>>(
						params, headers);
				ResponseEntity<String> response2 = new RestTemplate().postForEntity(baseURL + "/access_token", request2,
						String.class);
				logger.info("response2: {}", response2.getBody());
				ObjectMapper om = new ObjectMapper();
				Authentication oAuthentication = om.readValue(response2.getBody(), Authentication.class);
				String[] t = oAuthentication.access_token.split("_");

				String orginzationId = t[t.length - 1];
				oAuthentication.orginzationId = orginzationId;

//				apiService.setAuthentication(oAuthentication);

//				access_token = "" + map1.get("access_token");
//				expires_in = "" + map1.get("expires_in");

//				return homePage(request, response, oAuthentication);
			} catch (Exception e) {
				/**
				 * STEP 1 - make the authorize request
				 */
				logger.info("1st time to web site");

				String redirectUriEncode = URLEncoder.encode(redirectUri, StandardCharsets.US_ASCII);
				String scopeEncode = URLEncoder.encode(scope, StandardCharsets.US_ASCII);
				String login = baseURL + "/authorize?response_type=" + responseType + "&client_id=" + clientId
						+ "&redirect_uri=" + redirectUriEncode + "&scope=" + scopeEncode + "&state=" + state;
				logger.info("login:{}", login);
			}
		} catch (Exception e) {
			return "{\"" + EXCEPTION + "\":\"" + e.getMessage() + "\"}";
		}

		return "<a href='" + serverUiUrl + "'>Login</a>";
	}

	/**
	 * @see #userV2
	 * @param request
	 * @param response
	 * @return
	 * @deprecated (see userV2 will remove next version)
	 */
	@RequestMapping("/user")
	@Deprecated(since="1.1", forRemoval=true)
	public Object user(HttpServletRequest request, HttpServletResponse response) {
		logger.info("user");
		logger.info("user:getSession().getId:{}", request.getSession().getId());
		try {
			this.addHeaders(response);

			try {
				Authentication check = (Authentication) request.getSession().getAttribute(AUTHENTICATFION);
				logger.debug("check:{}", check);
				if (check != null) {
					return "{}";
				} else {
					return true;
				}
			} catch (Exception e) {
				response.sendRedirect("/");
			}
		} catch (Exception e) {
			return "{\"" + EXCEPTION + "\":\"" + e.getMessage() + "\"}";
		}

		return "{}";
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return Authentication
	 */
	@GetMapping("/userV2")
	public Object userV2(HttpServletRequest request, HttpServletResponse response) {
		logger.info("userV2");
		this.addHeaders(response);
		Authentication oAuthentication = (Authentication) request.getSession().getAttribute(AUTHENTICATFION);
		logger.info("userV2 :oAuthentication {},{}", request.getSession().getId(), oAuthentication);
		return oAuthentication;
	}

	@RequestMapping("/login")
	public Object login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam final Map<String, String> inboundParameters) {
		logger.info("authorizeAndLogin");
		logger.debug("inboundParameters:{}", inboundParameters);
		String loginUrl;
		this.addHeaders(response);

		int redirectCount = 0;
		try {
			redirectCount = (int) request.getSession().getAttribute(REDIRECT_CNT);
			redirectCount++;
			request.getSession().setAttribute(REDIRECT_CNT, redirectCount);
		} catch (Exception e) {
			request.getSession().setAttribute(REDIRECT_CNT, 1);
		}
		try {
			java.util.LinkedHashMap<?, ?> map = (java.util.LinkedHashMap<?, ?>) inboundParameters;
			try {

				/**
				 * STEP 2 - get the user token after the authorize request
				 */

				if (map.get(ERROR) != null) {
					return map.get(ERROR);
				}

				logger.info("authorized but not logged in");
				if (redirectCount > 5) {
					// check your application.yml values
					request.getSession().setAttribute(REDIRECT_CNT, 0);
					return "{\"" + EXCEPTION + "\":\"Authorized but not logged in error: invalid tenant\"}";
				}
				// this will for an exception
				map.get("code").toString();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//						
				MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
				params.add("grant_type", grantType);
				params.add("client_id", clientId);
				params.add("client_secret", clientSecret);
				params.add("code", map.get("code").toString());
				params.add("redirect_uri", redirectUri);

				String url = baseURL + "/access_token";
				logger.info("response2:url: {}", url);
				logger.info("response2:params {}", params);

				HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<MultiValueMap<String, String>>(
						params, headers);
				ResponseEntity<String> response2 = new RestTemplate().postForEntity(url, request2, String.class);
				logger.info("response2: {}", response2.getBody());
				ObjectMapper om = new ObjectMapper();
				Authentication oAuthentication = om.readValue(response2.getBody(), Authentication.class);
				String[] t = oAuthentication.access_token.split("_");

				String orginzationId = t[t.length - 1];
				oAuthentication.orginzationId = orginzationId;

				if (logger.isDebugEnabled()) {
					logger.debug("response2:oAuthentication {},{}", request.getSession().getId(),
							om.writeValueAsString(oAuthentication));
				}
				request.getSession().setAttribute(AUTHENTICATFION, oAuthentication);

				return om.writeValueAsString(oAuthentication);
			} catch (Exception e) {
				/**
				 * STEP 1 - make the authorize request
				 */
				logger.info("1st time to web site");

				String redirectUriEncode = URLEncoder.encode(redirectUri, StandardCharsets.US_ASCII);
				String scopeEncode = URLEncoder.encode(scope, StandardCharsets.US_ASCII);
				loginUrl = baseURL + "/authorize?response_type=" + responseType + "&client_id=" + clientId
						+ "&redirect_uri=" + redirectUriEncode + "&scope=" + scopeEncode + "&state=" + state;
				logger.info("loginUrl:{}", loginUrl);
			}
		} catch (Exception e) {
			return "{\"" + EXCEPTION + "\":\"" + e.getMessage() + "\"}";
		}

		Authentication oAuthentication = (Authentication) request.getSession().getAttribute(AUTHENTICATFION);
		logger.info("final:oAuthentication {},{}", request.getSession().getId(), oAuthentication);
		return oAuthentication;
	}

	@RequestMapping("/mylogoutV2")
	public Object mylogout(HttpServletRequest request, HttpServletResponse response,
			@RequestParam final Map<String, String> inboundParameters) {
		logger.info("mylogoutV2");
		this.addHeaders(response);
		request.getSession().setAttribute(AUTHENTICATFION, null);
		request.getSession().removeAttribute(AUTHENTICATFION);
		return "{}";
	}

	/**
	 * 
	 * @param response
	 */
	private void addHeaders(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Methods", "POST, GET,  DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with, content-type");
		response.setHeader("Access-Control-Allow-Origin", redirectUri);
		response.setHeader("Access-Control-Allow-Credentials", "true");
	}
	
	@GetMapping("/doesItWork")
	public Object doesItWork(HttpServletRequest request, HttpServletResponse response) {
		return "{\"it\":\"does\"}";
	}	
}