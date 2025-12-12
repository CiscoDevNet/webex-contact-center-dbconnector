/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.controller;
 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cisco.app.dbconnector.model.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller for Angular routes
 * @author jiwyatt
 * @since 12/12/2020
 */
@Controller
public class WebControllerUI {
	Logger logger = LoggerFactory.getLogger(WebControllerUI.class);

	public WebControllerUI() {
		super();
		logger.info("public WebControllerUI");	}

	@GetMapping({ "/", "/logout", "/help", "/support", "/about", "/loggedin", "/endpoint", "/connector", "/grid-view", "mylogin" })
	public String angularRoutes(HttpServletRequest request, HttpServletResponse response) {
		request.getAttribute("access_token");
		Authentication check = (Authentication) request.getAttribute("oAuthentication");
		logger.debug("WebControllerUI: check:{}", check);
		
		logger.debug("WebControllerUI");
		return "forward:/index.html";
//		return "forward:/";
	}
	
	@GetMapping({ "/mylogout"})
	@ResponseBody
	public Object mylogout(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("WebControllerUI");
		logger.debug("mylogout");
		request.setAttribute("oAuthentication", null);
		
//		return "forward:/index.html";
		return "{}";
	}

}