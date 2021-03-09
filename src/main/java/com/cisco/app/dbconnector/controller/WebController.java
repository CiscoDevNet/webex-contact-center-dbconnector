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
public class WebController {
	Logger logger = LoggerFactory.getLogger(WebController.class);
	
 
	@Resource(name="${filesystem.fileSystemInterface}")	
	private FileSystemInterface fileSystem;
	
	@Autowired
	private DatabaseUtility db;
	private BasicAuth basicAuth;
	private Map<String, Object> endpointMap = new ConcurrentHashMap<String, Object>();

	public WebController() {
		super();
	}

	/**
	 * force all traffic to index.html
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/")
	public void root(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect("/index.html");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@RequestMapping(value = "/mylogout", method = RequestMethod.POST)
	@ResponseBody
	public void error(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect("/logout");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get the active connector AKA data/connector.obj
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/connector", method = RequestMethod.GET)
	@ResponseBody
	public Object getConnector(HttpServletRequest request, HttpServletResponse response) {
		long tstart = System.currentTimeMillis();
		try {
			return fileSystem.readConnectorFromFile();
		} catch (Exception e) {
			logger.error(e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"Exception\":\"" + encoded + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/connectorGet");
		}
	}

	/**
	 * get the server connection object by server type
	 * 
	 * @param request
	 * @param response
	 * @param serverType
	 * @return
	 */
	@RequestMapping(value = "/connector/{serverType}", method = RequestMethod.GET)
	@ResponseBody
	public Object getConnectorByServerType(HttpServletRequest request, HttpServletResponse response, @PathVariable String serverType) {
		long tstart = System.currentTimeMillis();
		logger.info("serverType:" + serverType);

		try {
			if (DbConnection.SERVER_TYPE_MYSQL.equals(serverType)) {
				return fileSystem.readConnectorFromFile(serverType);
			} else if (DbConnection.SERVER_TYPE_SQL_SERVER.equals(serverType)) {
				return fileSystem.readConnectorFromFile(serverType);
			} else {
				logger.warn("NOT a valid SERVER_TYPE {}:", serverType);
				return fileSystem.readConnectorFromFile();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"Exception\":\"" + encoded + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/getConnectorByServerType");
		}
	}

	/**
	 * save the database connection and then test the connection
	 * 
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/connector", method = RequestMethod.POST)
	@ResponseBody
	public Object postConnector(HttpServletRequest request, HttpServletResponse response, @RequestBody Object body) {
		long tstart = System.currentTimeMillis();
		logger.info("body:" + body);
		try {
//			java.util.LinkedHashMap<?, ?> map = (java.util.LinkedHashMap<?, ?>) body;
			ObjectMapper mapper = new ObjectMapper();
			DbConnection oDbConnection = mapper.convertValue(body, new TypeReference<MySql>() {
			});
			if (!db.dbConnectionIsValid(oDbConnection)) {
				String encoded = new String(Base64.getEncoder().encode("Invalid Connection Pool setting".toString().getBytes()));
				return "{\"Exception\":\"" + encoded + "\"}";
			}
			if (oDbConnection.getType().equals(DbConnection.SERVER_TYPE_SQL_SERVER)) {
				oDbConnection = mapper.convertValue(body, new TypeReference<SqlServer>() {
				});
			} else if (oDbConnection.getType().equals(DbConnection.SERVER_TYPE_MYSQL)) {
				oDbConnection = mapper.convertValue(body, new TypeReference<MySql>() {
				});
			} else {
				return "{\"Exception\";\"Not a valid server type\"}";
			}
			/**
			 * write Connector to file
			 */
			fileSystem.writeConnectorToFile(oDbConnection);

			logger.info("oDbConnection:" + oDbConnection);
//			db.setDataSource(oConnector);
			if (db.testConnector(oDbConnection)) {
				return "{\"response\":\"SUCCESS \\n---- Very Nice!!!! ---- \\nYou can now build your endpoints\"}";
			} else {
				return "{\"response\":\"Failure\"}";
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"Exception\":\"" + encoded + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/postConnector");
		}
	}

	@RequestMapping(value = "/endpoints", method = RequestMethod.GET)
	@ResponseBody
	public Object getEndpoints(HttpServletRequest request, HttpServletResponse response) {
		long tstart = System.currentTimeMillis();

		try {
			return fileSystem.loadEndpointsFromFile();
		} catch (Exception e) {
			logger.error(e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"Exception\":\"" + encoded + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/getEndpoints");
		}
	}

	@RequestMapping(value = "/endpoint", method = RequestMethod.POST)
	@ResponseBody
	public Object postEndpoint(HttpServletRequest request, HttpServletResponse response, @RequestBody Object body) {
		long tstart = System.currentTimeMillis();
		logger.info("body:" + body);
		try {
			// java.util.LinkedHashMap<?, ?> map = (java.util.LinkedHashMap<?, ?>) body;
			ObjectMapper mapper = new ObjectMapper();
			Endpoint oEndpoint = null;
			oEndpoint = mapper.convertValue(body, new TypeReference<Endpoint>() {
			});
			/**
			 * write Connector to file
			 */
			fileSystem.writeEndpointToFile(oEndpoint);
			String json = db.execute(oEndpoint);
			logger.info("json: {}", json);
			String jsonResponse = new String(Base64.getEncoder().encode(json.getBytes()));
			String http = "" + db.getUrlParams(oEndpoint);
			String httpParams = new String(Base64.getEncoder().encode(http.getBytes()));
			String sql = "" + db.getSqlStatement(oEndpoint);
			String sqlStatement = new String(Base64.getEncoder().encode(sql.getBytes()));
			try {
				if (basicAuth == null) {
					basicAuth = fileSystem.readBasicAuthFromFile();
				}
			} catch (java.io.FileNotFoundException e) {
				// OK if this is installation
				logger.warn("authorization is turned off", e.getMessage());
				basicAuth = new BasicAuth();
				fileSystem.writeBasicAuthToFile(basicAuth);
			}
			if (basicAuth.getIsBasicAuthenticationRequired()) {
				if (basicAuth.getValue() == null || basicAuth.getValue().length() < 1) {
					String sw = "Basic Authentication is turned on but has not been configured properly.";
					String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
					return "{\"Exception\":\"" + encoded + "\"}";
				}
			}
			endpointMap.clear();
			return "{\"response\":\"SUCCESS\",\"jsonResponse\":\"" + jsonResponse + "\",\"httpParams\":\"" + httpParams + "\",\"sqlStatement\":\"" + sqlStatement + "\",\"authentication\":\"" + basicAuth.getIsBasicAuthenticationRequired() + "\"}";

		} catch (Exception e) {
			logger.error(e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"Exception\":\"" + encoded + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/postEndpoint");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/endpoint/{endpointName}", method = RequestMethod.DELETE)
	public Object deleteEndpoint(HttpServletRequest request, HttpServletResponse response, @PathVariable String endpointName) {
		long tstart = System.currentTimeMillis();
		try {
			logger.info("endpointName:" + endpointName);
			List<Endpoint> lists = fileSystem.loadEndpointsFromFile();
			for (Endpoint endpoint : lists) {
				if (endpoint.getName().equals(endpointName)) {
					fileSystem.deleteEndpoint(endpointName);
				}
			}
			//clear the map. it will get reloaded automatically 
			endpointMap.clear();			
			return fileSystem.loadEndpointsFromFile();
		} catch (java.io.FileNotFoundException e) {
			return "{\"Exception\":\"Connector not found\"}";
		} catch (Exception e) {
			logger.error("", e);
			return "{\"Exception\":\"" + e + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/deleteEndpoint");
		}
	}	
	
	@RequestMapping(value = "/basicauth", method = RequestMethod.GET)
	@ResponseBody

	public Object getBasicAuth(HttpServletRequest request, HttpServletResponse response) {
		long tstart = System.currentTimeMillis();
		try {
			return fileSystem.readBasicAuthFromFile();
		} catch (Exception e) {
			logger.error(e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"Exception\":\"" + encoded + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/getBasicAuth");
		}
	}

	@RequestMapping(value = "/basicauth", method = RequestMethod.POST)
	@ResponseBody
	public Object postBasicAuth(HttpServletRequest request, HttpServletResponse response, @RequestBody Object body) {
		long tstart = System.currentTimeMillis();
		logger.info("body:" + body);
		try {
			ObjectMapper mapper = new ObjectMapper();
			BasicAuth oBasicAuth = null;
			oBasicAuth = mapper.convertValue(body, new TypeReference<BasicAuth>() {
			});
			/**
			 * write Connector to file
			 */
			fileSystem.writeBasicAuthToFile(oBasicAuth);
			basicAuth = oBasicAuth;

			logger.info("oBasicAuth:" + oBasicAuth);
			return "{\"response\":\"Global setting for Basic Authentication for endpoints has been updated\"}";

		} catch (Exception e) {
			logger.error(e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"Exception\":\"" + encoded + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/postBasicAuth");
		}
	}

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

	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public String jim(HttpServletRequest request, HttpServletResponse response) {
		return "forward:/index.html";
	}

}