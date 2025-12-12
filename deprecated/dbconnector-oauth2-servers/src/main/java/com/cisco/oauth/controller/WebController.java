package com.cisco.oauth.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/")
public class WebController {
	Logger logger = LoggerFactory.getLogger(WebController.class);

	public WebController() {
		super();
		logger.info("public WebController");
	}

	@GetMapping(path = "/webexcc/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object webexcc(Authentication authentication, HttpServletRequest request, HttpServletResponse response, @RequestParam final Map<String, String> inboundParameters) {
		logger.debug("webexcc");
		logger.debug("inboundParameters:{}", inboundParameters);		
		return "{\"jim\":\"wyatt\"}";
	}
}
