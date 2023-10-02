package com.cisco.oauth.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CustomErrorController implements ErrorController {
	Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

	public CustomErrorController() {
    	logger.info("public class CustomErrorController:");
	}

	  @RequestMapping("/error")
	  @ResponseBody
	  String error(HttpServletRequest request, HttpServletResponse response) {
		  try {
		    	logger.info("response.sendRedirect to /");
			response.sendRedirect("/");
		} catch (IOException e) {
			e.printStackTrace();
		}
		  return "";
	  }

	  public String getErrorPath() {
	    return "/error";
	  }
	}

