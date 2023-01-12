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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
	private String response_type;

	@Value("${client_id}")
	private String client_id;

	@Value("${redirect_uri}")
	private String redirect_uri;

	@Value("${scope}")
	private String scope;

	@Value("${state}")
	private String state;

	@Value("${grant_type}")
	private String grant_type;

	@Value("${client_secret}")
	private String client_secret;

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
//		logger.info("inboundParameters:{}", inboundParameters);
		int redirectCount = 0;
		try {
			redirectCount = (int) request.getSession().getAttribute("redirectCount");
			redirectCount++;
			request.getSession().setAttribute("redirectCount", redirectCount);
		} catch (Exception e) {
			request.getSession().setAttribute("redirectCount", 1);
		}
		try {
			java.util.LinkedHashMap<?, ?> map = (java.util.LinkedHashMap<?, ?>) inboundParameters;
			try {
//				logger.info("code:{}", map.get("code"));
				/**
				 * STEP 3 - already logged in STEP 2 only if authorized STEP 1 only if it is the
				 * 1st time to the web page
				 */
				Authentication check = (Authentication) request.getSession().getAttribute("Authentication");
				logger.info("root:check:{}", check);

				if (check != null) {
					logger.info("Logged in");
					return check;
				}

				/**
				 * STEP 2 - get the user token after the authorize request
				 */

				if (map.get("error_description") != null) {
					return map.get("error_description");
				}

				logger.info("authorized but not logged in");
				if (redirectCount > 5) {
					// check your application.yml values
					request.getSession().setAttribute("redirectCount", 0);
					return "Authorized but not logged in error: invalid tenant";
				}
				// this will for an exception
				map.get("code").toString();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//						
				MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
				params.add("grant_type", grant_type);
				params.add("client_id", client_id);
				params.add("client_secret", client_secret);
				params.add("code", map.get("code").toString());
				params.add("redirect_uri", redirect_uri);

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

				String redirectUriEncode = URLEncoder.encode(redirect_uri, StandardCharsets.US_ASCII);
				String scopeEncode = URLEncoder.encode(scope, StandardCharsets.US_ASCII);
				String login = baseURL + "/authorize?response_type=" + response_type + "&client_id=" + client_id
						+ "&redirect_uri=" + redirectUriEncode + "&scope=" + scopeEncode + "&state=" + state;
				logger.info("login:{}", login);
//				response.sendRedirect(login);
			}
		} catch (Exception e) {
			return "{\"Exception\":\"" + e.getMessage() + "\"}";
		}

//		try {response.sendRedirect("index.html");} catch (IOException e) {logger.error("Exception:{}", e.getMessage());}
		return "<a href='" + serverUiUrl + "'>Login</a>";
	}

//	@RequestMapping(value = "/mylogout", method = RequestMethod.POST)
//	@ResponseBody
//	public void error(HttpServletRequest request, HttpServletResponse response) {
//		logger.info("mylogout");
//		try {
//			//https://idbroker.webex.com/idb/oauth2/v1/logout?goto=https%3A%2F%2Fportal-v2.wxcc-us1.cisco.com%2Fportal
//			response.sendRedirect("/");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * @see #userV2
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user")
	@Deprecated
	public Object user(HttpServletRequest request, HttpServletResponse response) {
		logger.info("user");
		logger.info("user:getSession().getId:{}", request.getSession().getId());
		try {
			this.addHeaders(response);

			try {
				Authentication check = (Authentication) request.getSession().getAttribute("Authentication");
				logger.info("root:check:{}", check);
				if (check != null) {
				} else {
					return true;
				}
			} catch (Exception e) {
				response.sendRedirect("/");
			}
		} catch (Exception e) {
			return "{\"Exception\":\"" + e.getMessage() + "\"}";
		}

		return "{}";
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return Authentication
	 */
	@RequestMapping("/userV2")
	public Object userV2(HttpServletRequest request, HttpServletResponse response) {
		logger.info("userV2");
		this.addHeaders(response);
		Authentication oAuthentication = (Authentication) request.getSession().getAttribute("Authentication");
		logger.info("userV2 :oAuthentication {},{}", request.getSession().getId(), oAuthentication);
		return oAuthentication;
	}

	@RequestMapping("/login")
	public Object login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam final Map<String, String> inboundParameters) {
		logger.info("authorizeAndLogin");
//		logger.info("inboundParameters:{}", inboundParameters);
		String loginUrl;
		this.addHeaders(response);

		int redirectCount = 0;
		try {
			redirectCount = (int) request.getSession().getAttribute("redirectCount");
			redirectCount++;
			request.getSession().setAttribute("redirectCount", redirectCount);
		} catch (Exception e) {
			request.getSession().setAttribute("redirectCount", 1);
		}
		try {
			java.util.LinkedHashMap<?, ?> map = (java.util.LinkedHashMap<?, ?>) inboundParameters;
			try {

				/**
				 * STEP 2 - get the user token after the authorize request
				 */

				if (map.get("error_description") != null) {
					return map.get("error_description");
				}

				logger.info("authorized but not logged in");
				if (redirectCount > 5) {
					// check your application.yml values
					request.getSession().setAttribute("redirectCount", 0);
					return "{\"Exception\":\"Authorized but not logged in error: invalid tenant\"}";
				}
				// this will for an exception
				map.get("code").toString();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//						
				MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
				params.add("grant_type", grant_type);
				params.add("client_id", client_id);
				params.add("client_secret", client_secret);
				params.add("code", map.get("code").toString());
				params.add("redirect_uri", redirect_uri);

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

//				apiService.setAuthentication(oAuthentication);

//				access_token = "" + map1.get("access_token");
//				expires_in = "" + map1.get("expires_in");

//				return homePage(request, response, oAuthentication);
				logger.info("response2:oAuthentication {},{}", request.getSession().getId(),
						om.writeValueAsString(oAuthentication));
				request.getSession().setAttribute("Authentication", oAuthentication);

				String s = om.writeValueAsString(oAuthentication);
				return s;
			} catch (Exception e) {
//				e.printStackTrace();
				/**
				 * STEP 1 - make the authorize request
				 */
				logger.info("1st time to web site");

				String redirectUriEncode = URLEncoder.encode(redirect_uri, StandardCharsets.US_ASCII);
				String scopeEncode = URLEncoder.encode(scope, StandardCharsets.US_ASCII);
				loginUrl = baseURL + "/authorize?response_type=" + response_type + "&client_id=" + client_id
						+ "&redirect_uri=" + redirectUriEncode + "&scope=" + scopeEncode + "&state=" + state;
				logger.info("loginUrl:{}", loginUrl);

//				response.sendRedirect(login);
			}
		} catch (Exception e) {
			return "{\"Exception\":\"" + e.getMessage() + "\"}";
		}

//		try {response.sendRedirect("index.html");} catch (IOException e) {logger.error("Exception:{}", e.getMessage());}
		Authentication oAuthentication = (Authentication) request.getSession().getAttribute("Authentication");
		logger.info("final:oAuthentication {},{}", request.getSession().getId(), oAuthentication);
		return oAuthentication;
	}

	@RequestMapping("/mylogoutV2")
	public Object mylogout(HttpServletRequest request, HttpServletResponse response,
			@RequestParam final Map<String, String> inboundParameters) {
		logger.info("mylogoutV2");
		this.addHeaders(response);
		request.getSession().setAttribute("Authentication", null);
		request.getSession().removeAttribute("Authentication");
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
		response.setHeader("Access-Control-Allow-Origin", redirect_uri);
		response.setHeader("Access-Control-Allow-Credentials", "true");
	}
}