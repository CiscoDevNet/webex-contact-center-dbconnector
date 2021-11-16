/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.cisco.app.dbconnector.model.Authentication;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller for rest calls 
 * @author jiwyatt
 * @since 12/12/2020
 * @see https://devportal.wxcc-us1.cisco.com/documentation/getting-started
 * @see https://app.smartsheet.com/b/form/7af787e752834bbfba604bfc85a5eff1
 */
@RestController
@RequestMapping("/")
public class WebController {
	Logger logger = LoggerFactory.getLogger(WebController.class);

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
	public Object root(HttpServletRequest request, HttpServletResponse response, @RequestParam final Map<String, String> inboundParameters) {
		logger.info("root");
//		logger.info("inboundParameters:{}", inboundParameters);
		try {
			java.util.LinkedHashMap<?, ?> map = (java.util.LinkedHashMap<?, ?>) inboundParameters;
			try {
//				logger.info("code:{}", map.get("code"));
				/**
				 * STEP 3 - already logged in STEP 2 only if authorized STEP 1 only if it is the
				 * 1st time to the web page
				 */
				Authentication check = (Authentication) request.getSession().getAttribute("oAuthentication");
				logger.info("root:check:{}", check);

				if (check != null) {
					logger.info("Logged in");
					return homePage(request, response, check);
				}

				/**
				 * STEP 2 - get the token after the authorize request
				 */
				logger.info("authorized but not logged in");
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

				HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<MultiValueMap<String, String>>(params, headers);
				ResponseEntity<String> response2 = new RestTemplate().postForEntity(baseURL + "/access_token", request2, String.class);
				logger.info("response2: {}", response2.getBody());
				ObjectMapper om = new ObjectMapper();
				Authentication oAuthentication = om.readValue(response2.getBody(), Authentication.class);
				String[] t = oAuthentication.access_token.split("_");

				String orginzationId = t[t.length - 1];
				oAuthentication.orginzationId = orginzationId;

//				apiService.setAuthentication(oAuthentication);

//				access_token = "" + map1.get("access_token");
//				expires_in = "" + map1.get("expires_in");

				return homePage(request, response, oAuthentication);
			} catch (Exception e) {
				/**
				 * STEP 1 - make the authorize request
				 */
				logger.info("1st time to web site");

				String redirectUriEncode = URLEncoder.encode(redirect_uri, StandardCharsets.US_ASCII);
				String scopeEncode = URLEncoder.encode(scope, StandardCharsets.US_ASCII);
				String login = baseURL + "/authorize?response_type=" + response_type + "&client_id=" + client_id + "&redirect_uri=" + redirectUriEncode + "&scope=" + scopeEncode + "&state=" + state;
				logger.info("login:{}", login);

				response.sendRedirect(login);
			}
		} catch (Exception e) {
			return "{\"Exception\":\"" + e.getMessage() + "\"}";
		}

//		try {response.sendRedirect("index.html");} catch (IOException e) {logger.error("Exception:{}", e.getMessage());}
		return "{}";
	}

	private ModelAndView homePage(HttpServletRequest request, HttpServletResponse response, com.cisco.app.dbconnector.model.Authentication oAuthentication) throws Exception {
		logger.info("homePage");
		logger.info("oAuthentication:{}", oAuthentication);
		request.getSession().setAttribute("oAuthentication", oAuthentication);
//		response.sendRedirect("/index.html");
		java.util.LinkedHashMap<String,Object> map =   new java.util.LinkedHashMap<String, Object>();
		request.setAttribute("oAuthentication", oAuthentication);
		map.put("oAuthentication", oAuthentication);
		map.put("access_token", oAuthentication.access_token);
		return new ModelAndView("index", map);
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

	@RequestMapping("/user")
	public Object user(HttpServletRequest request, HttpServletResponse response) {
		logger.info("user");
		try {
			try {
				Authentication check = (Authentication) request.getSession().getAttribute("oAuthentication");
				logger.info("root:check:{}", check);
				if(check == null) {
				}
				else {
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

}