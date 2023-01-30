/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cisco.app.dbconnector.model.Authentication;
import com.cisco.app.dbconnector.model.BasicAuth;
import com.cisco.app.dbconnector.model.DbConnection;
import com.cisco.app.dbconnector.model.Endpoint;
import com.cisco.app.dbconnector.model.MySql;
import com.cisco.app.dbconnector.model.SqlServer;
import com.cisco.app.dbconnector.service.DatabaseUtility;
import com.cisco.app.dbconnector.service.FileSystemInterface;
import com.cisco.app.util.Memory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller for rest calls 
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
@RestController
@RequestMapping("/rest")
public class WebControllerWebexCC {
	Logger logger = LoggerFactory.getLogger(WebControllerWebexCC.class);
	
 
	@Resource(name="${filesystem.fileSystemInterface}")	
	private FileSystemInterface fileSystem;
	
	@Autowired
	private DatabaseUtility db;
	private BasicAuth basicAuth;
	private Map<String, Object> endpointMap = new ConcurrentHashMap<String, Object>();

	public WebControllerWebexCC() {
		super();
		logger.info("public WebControllerRest");
	}

	/**
	 * WEBEXCC end-point -- Security for this method is only none or BASIC Authentication<br>
	 * DO not apply web site security to this method 
	 * @param request
	 * @param response
	 * @param endpointName
	 * @param inboundParameters
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/webexcc/{endpointName}")
	public Object webexccEndpoint(HttpServletRequest request, HttpServletResponse response, @PathVariable String endpointName, @RequestParam final Map<String, String> inboundParameters) {
		long tstart = System.currentTimeMillis();
		logger.debug("webexcc/" + endpointName);
		logger.debug("body:" + inboundParameters);
		try {
			String authorization = request.getHeader("authorization");
			try {
				if (basicAuth == null) {
					basicAuth = fileSystem.readBasicAuthFromFile();
				}
			} catch (java.io.FileNotFoundException e) {
				// OK if this is installation
				// or authorization is turned off
				logger.warn("Basic Authentication turned off", e.getMessage());
				basicAuth = new BasicAuth();
				fileSystem.writeBasicAuthToFile(basicAuth);
			}
			if (basicAuth.getIsBasicAuthenticationRequired()) {
				// note
				// String encoding = "Basic " + Base64.getEncoder().encodeToString("ivrincontact:ivrincontact!01".getBytes("UTF-8"));
				logger.debug("authorization:" + authorization);
				logger.debug("encoding     :" + basicAuth.getValue());
				if (!basicAuth.getValue().equals(authorization)) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return "{\"Exception\":\"401 not authorized\"}";
				}
			} else {
				logger.warn("Basic Authentication is turned off");
			}
		} catch (Exception e) {
			logger.error("", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return "{\"Exception\":\"" + e.getMessage() + "\"}";
		}
		try {
			if (endpointMap.size() == 0) {
				List<Endpoint> lists = fileSystem.loadEndpointsFromFile();
				for (Endpoint oEndpoint : lists) {
					endpointMap.put(oEndpoint.getName(), oEndpoint);
				}
			}
			Endpoint oEndpoint = (Endpoint) endpointMap.get(endpointName);
			logger.info("oEndpoint/webexcc/:" + oEndpoint);
			if (oEndpoint == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return "{\"Exception\":\"BAD END POINT\"}";
			}
			for (Map.Entry<String, String> entry : inboundParameters.entrySet()) {
				logger.debug("inboundParameter: " + entry.getKey() + " = " + entry.getValue());
				oEndpoint.updateNameValueList(entry);
			}
			return db.execute(oEndpoint);
		} catch (Exception e) {
			logger.error("", e);
			return "{\"Exception\":\"" + e + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/webexcc/" + endpointName);
		}
	}

	private long printMemoryCounter = 0;

	/**
	 * log each request for performance print java VM memory every 1000 request
	 * 
	 * @param request
	 * @param tstart
	 * @param method
	 */
	private void logExecutionTime(HttpServletRequest request, long tstart, String method) {
		printMemoryCounter++;
		if (printMemoryCounter % 1000 == 0) {
			Memory.main(null);
		}
		try {
			logger.info( "Done in " + (System.currentTimeMillis() - tstart) + " milli seconds"  + request.getSession().getId() + " - " + method + "");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@PostConstruct
	private void seedDataFiles() {
		try {
			fileSystem.seedDataFiles();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}