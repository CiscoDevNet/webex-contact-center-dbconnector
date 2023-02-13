/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cisco.app.dbconnector.model.Authentication;

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
		logger.info("public WebControllerUI");
	}

	@RequestMapping({ "/index", "/logout", "/help", "/support", "/about", "/loggedin", "/endpoint", "/connector", "/GridView" })
	public String angularRoutes(HttpServletRequest request, HttpServletResponse response) {
		request.getAttribute("access_token");
		Authentication check = (Authentication) request.getAttribute("oAuthentication");
		logger.info("WebControllerUI: check:{}", check);
		
		logger.info("WebControllerUI");
		return "forward:/index.html";
//		return "forward:/";
	}
	
	@RequestMapping({ "/mylogout"})
	@ResponseBody
	public Object mylogout(HttpServletRequest request, HttpServletResponse response) {
		logger.info("WebControllerUI");
		logger.info("mylogout");
		request.setAttribute("oAuthentication", null);
		
//		return "forward:/index.html";
		return "{}";
	}

}