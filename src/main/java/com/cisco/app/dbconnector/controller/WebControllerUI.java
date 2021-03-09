/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	}

	@RequestMapping({ "/", "/logout", "/help", "/support", "/about", "/loggedin", "/endpoint", "/connector", "/GridView" })
	public String a() {
		return "forward:/index.html";
	}

}