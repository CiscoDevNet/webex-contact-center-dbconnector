/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.cisco.app.dbconnector.model.Authentication;
import com.cisco.app.dbconnector.model.BasicAuth;
import com.cisco.app.dbconnector.model.DbConnection;
import com.cisco.app.dbconnector.model.Endpoint;
import com.cisco.app.dbconnector.model.MySql;
import com.cisco.app.dbconnector.model.SqlServer;
import com.cisco.app.dbconnector.model.Oracle;
import com.cisco.app.dbconnector.service.FileSystemInterface;
import com.cisco.app.dbconnector.util.DatabaseUtility;
import com.cisco.app.util.Memory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller for rest calls
 * 
 * @author jiwyatt
 * @since 12/12/2020
 * 
 */
@RestController
@RequestMapping("/rest")
@CrossOrigin(origins = { "${spring.security.oauth2.resourceserver.jwt.issuer-uri}" })
public class WebControllerRest {
	Logger logger = LoggerFactory.getLogger(WebControllerRest.class);

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String redirectUri;

	@Resource(name = "${filesystem.fileSystemInterface}")
	private FileSystemInterface fileSystem;

	@Autowired
	private DatabaseUtility db;
	private BasicAuth basicAuth;
	private Map<String, Object> endpointMap = new ConcurrentHashMap<>();

	private static final String AUTHENTICATFION = "Authentication";
	private static final String EXCEPTION = "Exception";

	public WebControllerRest() {
		super();
		logger.info("public WebControllerRest");
	}

	/**
	 * get the active connector AKA data/Connector.obj
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/connector")
	@ResponseBody
	public Object getConnector(HttpServletRequest request, HttpServletResponse response) {
		long tstart = System.currentTimeMillis();
		this.addHeaders(response);

		Authentication check = (Authentication) request.getSession().getAttribute(AUTHENTICATFION);
		logger.debug("check:{}", check);

		try {
			logger.debug("readConnectorFromFile");
			return fileSystem.readConnectorFromFile();
		} catch (Exception e) {
			logger.error(e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"" + EXCEPTION + "\":\"" + encoded + "\"}";
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
	@GetMapping("/connector/{serverType}")
	@ResponseBody
	public Object getConnectorByServerType(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String serverType) {
		long tstart = System.currentTimeMillis();
		this.addHeaders(response);
		logger.debug("serverType:{}", serverType);
		Authentication check = (Authentication) request.getSession().getAttribute(AUTHENTICATFION);
		logger.debug("check:{}", check);

		try {
			if (DbConnection.SERVER_TYPE_MYSQL.equals(serverType)) {
				return fileSystem.readConnectorFromFile(serverType);
			} else if (DbConnection.SERVER_TYPE_SQL_SERVER.equals(serverType)) {
				return fileSystem.readConnectorFromFile(serverType);
			} else if (DbConnection.SERVER_TYPE_ORACLE.equals(serverType)) {
				return fileSystem.readConnectorFromFile(serverType);
			} else {
				logger.error("NOT a valid SERVER_TYPE {}:", serverType);
				return fileSystem.readConnectorFromFile();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"" + EXCEPTION + "\":\"" + encoded + "\"}";
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
	@PostMapping("/connector")
	@ResponseBody
	public Object postConnector(HttpServletRequest request, HttpServletResponse response, @RequestBody Object body) {
		long tstart = System.currentTimeMillis();
		this.addHeaders(response);
		logger.debug("body:{}", body);
		Authentication check = (Authentication) request.getSession().getAttribute(AUTHENTICATFION);
		logger.debug("check:{}", check);

		try {
			ObjectMapper mapper = new ObjectMapper();
			DbConnection oDbConnection = mapper.convertValue(body, new TypeReference<MySql>() { });
			if (!db.dbConnectionIsValid(oDbConnection)) {
				String encoded = new String( Base64.getEncoder().encode("Invalid Connection Pool setting".getBytes()));
				return "{\"" + EXCEPTION + "\":\"" + encoded + "\"}";
			}
			if (oDbConnection.getType().equals(DbConnection.SERVER_TYPE_SQL_SERVER)) {
				oDbConnection = mapper.convertValue(body, new TypeReference<SqlServer>() { });
			} else if (oDbConnection.getType().equals(DbConnection.SERVER_TYPE_MYSQL)) {
				oDbConnection = mapper.convertValue(body, new TypeReference<MySql>() { });
			} else if (oDbConnection.getType().equals(DbConnection.SERVER_TYPE_ORACLE)) {
				oDbConnection = mapper.convertValue(body, new TypeReference<Oracle>() { });
			} else {
				return "{\"" + EXCEPTION + "\";\"Not a valid server type\"}";
			}
			/**
			 * write Connector to file
			 */
			fileSystem.writeConnectorToFile(oDbConnection);
			reloadRules();

			logger.debug("oDbConnection:{}", oDbConnection);
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
			return "{\"" + EXCEPTION + "\":\"" + encoded + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/postConnector");
		}
	}

	@GetMapping("/endpoints")
	@ResponseBody
	public Object getEndpoints(HttpServletRequest request, HttpServletResponse response) {
		long tstart = System.currentTimeMillis();
		this.addHeaders(response);
		Authentication check = (Authentication) request.getSession().getAttribute(AUTHENTICATFION);
		logger.debug("check:{}", check);

		try {
			return fileSystem.loadEndpointsFromFile();
		} catch (Exception e) {
			logger.error(e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"" + EXCEPTION + "\":\"" + encoded + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/getEndpoints");
		}
	}

	@PostMapping("/endpoint")
	@ResponseBody
	public Object postEndpoint(HttpServletRequest request, HttpServletResponse response, @RequestBody Object body) {
		long tstart = System.currentTimeMillis();
		this.addHeaders(response);
		logger.debug("body:{}", body);
		Authentication check = (Authentication) request.getSession().getAttribute(AUTHENTICATFION);
		logger.debug("check:{}", check);

		try {
			ObjectMapper mapper = new ObjectMapper();
			Endpoint oEndpoint = null;
			oEndpoint = mapper.convertValue(body, new TypeReference<Endpoint>() {
			});
			/**
			 * write Connector to file
			 */
			fileSystem.writeEndpointToFile(oEndpoint);
			String json = db.execute(oEndpoint);
			logger.debug("json: {}", json);
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
				logger.warn("authorization is turned off: {}", e.getMessage());
				basicAuth = new BasicAuth();
				fileSystem.writeBasicAuthToFile(basicAuth);
			}
			if (basicAuth.getIsBasicAuthenticationRequired()) {
				if (basicAuth.getValue() == null || basicAuth.getValue().length() < 1) {
					String sw = "Basic Authentication is turned on but has not been configured properly.";
					String encoded = new String(Base64.getEncoder().encode(sw.getBytes()));
					return "{\"" + EXCEPTION + "\":\"" + encoded + "\"}";
				}
			}
			endpointMap.clear();
			reloadRules();
			return "{\"response\":\"SUCCESS\",\"jsonResponse\":\"" + jsonResponse + "\",\"httpParams\":\"" + httpParams
					+ "\",\"sqlStatement\":\"" + sqlStatement + "\",\"authentication\":\""
					+ basicAuth.getIsBasicAuthenticationRequired() + "\"}";

		} catch (Exception e) {
			logger.error("Exception", e);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"" + EXCEPTION + "\":\"" + encoded + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/postEndpoint");
		}
	}

	@ResponseBody
	@DeleteMapping("/endpoint/{endpointName}")
	public Object deleteEndpoint(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String endpointName) {
		long tstart = System.currentTimeMillis();
		this.addHeaders(response);
		Authentication check = (Authentication) request.getSession().getAttribute(AUTHENTICATFION);
		logger.debug("check:{}", check);

		try {
			logger.debug("endpointName:{}", endpointName);
			List<Endpoint> lists = fileSystem.loadEndpointsFromFile();
			for (Endpoint endpoint : lists) {
				if (endpoint.getName().equals(endpointName)) {
					fileSystem.deleteEndpoint(endpointName);
				}
			}
			// clear the map. it will get reloaded automatically
			endpointMap.clear();
			reloadRules();
			return fileSystem.loadEndpointsFromFile();
		} catch (java.io.FileNotFoundException e) {
			return "{\"" + EXCEPTION + "\":\"Connector not found\"}";
		} catch (Exception e) {
			logger.error("", e);
			return "{\"" + EXCEPTION + "\":\"" + e + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/deleteEndpoint");
		}
	}

	@GetMapping("/basicauth")
	@ResponseBody
	public Object getBasicAuth(HttpServletRequest request, HttpServletResponse response) {
		long tstart = System.currentTimeMillis();
		Authentication check = (Authentication) request.getSession().getAttribute(AUTHENTICATFION);
		logger.debug("check:{}", check);

		try {
			return fileSystem.readBasicAuthFromFile();
		} catch (Exception e) {
			logger.error(e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"" + EXCEPTION + "\":\"" + encoded + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/getBasicAuth");
		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
	@PostMapping("/basicauth")
	@ResponseBody
	public Object postBasicAuth(HttpServletRequest request, HttpServletResponse response, @RequestBody Object body) {
		long tstart = System.currentTimeMillis();
		this.addHeaders(response);
		Authentication check = (Authentication) request.getSession().getAttribute(AUTHENTICATFION);
		logger.debug("postBasicAuth:check:{}", check);
		logger.debug("postBasicAuth:body:{}", body);
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

			reloadRules();

			logger.debug("oBasicAuth:{}", oBasicAuth);
			return "{\"response\":\"Global setting for Basic Authentication for endpoints has been updated\"}";

		} catch (Exception e) {
			logger.error(e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String encoded = new String(Base64.getEncoder().encode(sw.toString().getBytes()));
			return "{\"" + EXCEPTION + "\":\"" + encoded + "\"}";
		} finally {
			logExecutionTime(request, tstart, "/postBasicAuth");
		}
	}

	private void reloadRules() throws Exception {
		// reload the rules & BasicAuth setting
		new RestTemplate().getForObject(redirectUri + "/rest/webexcc/reloadRules?reload=true", String.class);
		
	}

	private long printMemoryCounter = -1;

	/**
	 * log each request for performance print java VM memory every 1000 request
	 * 
	 * @param request
	 * @param tstart
	 * @param method
	 */
	private void logExecutionTime(HttpServletRequest request, long tstart, String method) {
		printMemoryCounter++;
		if (printMemoryCounter % 100 == 0) {
			Memory.logMemory();
		}
		try {
			logger.debug("Done in {} milli seconds {} - {}", (System.currentTimeMillis() - tstart), request.getSession().getId(), method);
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

	/**
	 * 
	 * @param response
	 */
	private void addHeaders(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Methods", "POST, GET,  DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with, content-type");
		response.setHeader("Access-Control-Allow-Origin", redirectUri);
		response.setHeader("Access-Control-Allow-Credentials", "true");
	}

}