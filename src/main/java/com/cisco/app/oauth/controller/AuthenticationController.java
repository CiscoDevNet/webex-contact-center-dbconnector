/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.oauth.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * oAuth2 Authentication Controller
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
@Controller
@RequestMapping("/user")
public class AuthenticationController  {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@GetMapping("/user")
	@ResponseBody
	public Object getUser(HttpServletRequest request, @AuthenticationPrincipal OAuth2User principal) {
		long tstart = System.currentTimeMillis();
		try {
			if (principal != null) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (auth != null) {
					return "{\"response\":\"" + auth.isAuthenticated() + "\"}";
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			logExecutionTime(request, tstart, "/user/getUser");
		}
		return "{\"response\":\"false\"}";
	}

	public OAuth2User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return ((OAuth2AuthenticationToken) auth).getPrincipal();
	}

	private void logExecutionTime(HttpServletRequest request, long tstart, String method) {
		try {
			logger.info( "Done in " + (System.currentTimeMillis() - tstart) + " milli seconds"  + request.getSession().getId() + " - " + method + "");
		} catch (Exception e) {
			logger.error("", e);
		}
	}


}
