/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.text.StringSubstitutor;
//import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cisco.app.dbconnector.model.DbConnection;
import com.cisco.app.dbconnector.model.Endpoint;
import com.cisco.app.dbconnector.model.NameValue;
import com.cisco.app.dbconnector.util.Convertor;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * this class makes the DB calls and returns the results as JSON
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
@Component
public class DatabaseUtility {
	Logger logger = LoggerFactory.getLogger(DatabaseUtility.class);

	private ComboPooledDataSource dataSource = null;

	@Resource(name="${filesystem.fileSystemInterface}")	
	private FileSystemInterface fileSystem;

	public DatabaseUtility() {
		super();

	}

	public boolean dbConnectionIsValid(DbConnection oDbConnection) {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			cpds.setJdbcUrl(oDbConnection.getConnectionString());
			cpds.setUser(oDbConnection.getUsername());
			cpds.setPassword(oDbConnection.getPassword());
			cpds.setInitialPoolSize(Integer.parseInt(oDbConnection.getConnectionPool().getInitialPoolSize()));
			cpds.setMinPoolSize(Integer.parseInt(oDbConnection.getConnectionPool().getMinPoolSize()));
			cpds.setAcquireIncrement(Integer.parseInt(oDbConnection.getConnectionPool().getAcquireIncrement()));
			cpds.setMaxPoolSize(Integer.parseInt(oDbConnection.getConnectionPool().getMaxPoolSize()));
			cpds.setMaxStatements(Integer.parseInt(oDbConnection.getConnectionPool().getMaxStatements()));
			cpds.setUnreturnedConnectionTimeout(Integer.parseInt(oDbConnection.getConnectionPool().getUnreturnedConnectionTimeout()));
			this.dataSource = cpds;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	private ComboPooledDataSource getDataSource() throws Exception {
		logger.debug("private ComboPooledDataSource getDataSource");
		if (this.dataSource != null) {
			return this.dataSource;
		}
		DbConnection oDbConnection = fileSystem.readConnectorFromFile();
		if (oDbConnection.getType().equals(DbConnection.SERVER_TYPE_MYSQL)) {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			cpds.setJdbcUrl(oDbConnection.getConnectionString());
			cpds.setUser(oDbConnection.getUsername());
			cpds.setPassword(oDbConnection.getPassword());

			cpds.setInitialPoolSize(Integer.parseInt(oDbConnection.getConnectionPool().getInitialPoolSize()));
			cpds.setMinPoolSize(Integer.parseInt(oDbConnection.getConnectionPool().getMinPoolSize()));
			cpds.setAcquireIncrement(Integer.parseInt(oDbConnection.getConnectionPool().getAcquireIncrement()));
			cpds.setMaxPoolSize(Integer.parseInt(oDbConnection.getConnectionPool().getMaxPoolSize()));
			cpds.setMaxStatements(Integer.parseInt(oDbConnection.getConnectionPool().getMaxStatements()));
			cpds.setUnreturnedConnectionTimeout(Integer.parseInt(oDbConnection.getConnectionPool().getUnreturnedConnectionTimeout()));
			this.dataSource = cpds;
			return cpds;

		} else if (oDbConnection.getType().equals(DbConnection.SERVER_TYPE_SQL_SERVER)) {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			cpds.setJdbcUrl(oDbConnection.getConnectionString());
			cpds.setUser(oDbConnection.getUsername());
			cpds.setPassword(oDbConnection.getPassword());

			cpds.setInitialPoolSize(Integer.parseInt(oDbConnection.getConnectionPool().getInitialPoolSize()));
			cpds.setMinPoolSize(Integer.parseInt(oDbConnection.getConnectionPool().getMinPoolSize()));
			cpds.setAcquireIncrement(Integer.parseInt(oDbConnection.getConnectionPool().getAcquireIncrement()));
			cpds.setMaxPoolSize(Integer.parseInt(oDbConnection.getConnectionPool().getMaxPoolSize()));
			cpds.setMaxStatements(Integer.parseInt(oDbConnection.getConnectionPool().getMaxStatements()));
			cpds.setUnreturnedConnectionTimeout(Integer.parseInt(oDbConnection.getConnectionPool().getUnreturnedConnectionTimeout()));
			this.dataSource = cpds;
			return cpds;
		} else {
			throw new Exception("Not a valid DataSource type");
		}
	}

	public String getUrlParams(Endpoint oEndpoint) throws Exception {
		try {
			StringBuffer sb = new StringBuffer("");
			Map<String, String> valuesMap = new HashMap<String, String>();
			List<NameValue> nameValueList = oEndpoint.getNameValueList();
			if (nameValueList == null || nameValueList.size() == 0) {
				return "";
			} else {
				for (NameValue nameValue : nameValueList) {
					/**
					 * name check
					 */
					if (nameValue.getName() == null || nameValue.getName().isEmpty()) {
						// Name can't be blank.
						continue;
					}
					try {
						// Name can't be a number.
						Integer.parseInt(nameValue.getName());
						continue;
					} catch (Exception e) {
						// life is good
					}
					if (Character.isDigit(nameValue.getName().charAt(0))) {
						// Name can't start with a number.
						continue;
					}
					if (nameValue.getName().startsWith("-")) {
						// Name can't start with a dash.
						continue;
					}
					String check = nameValue.getName().replaceAll("[ `~!@#$%^&*()_+=\\-\\[\\]{};':\",.<>/?]", "");
					if (nameValue.getName().length() != check.length()) {
						// Name can't have characters.
						continue;
					}
					/**
					 * value check
					 */
					if (nameValue.getValue() == null || "undefined".equalsIgnoreCase(nameValue.getValue())) {
						nameValue.setValue("");
					}
					valuesMap.put(nameValue.getName(), nameValue.getValue());
					sb.append("&" + nameValue.getName() + "=" + URLEncoder.encode(nameValue.getValue(), StandardCharsets.UTF_8.toString()));
				}
				StringSubstitutor sub = new StringSubstitutor(valuesMap);
				String realSql = sub.replace(sb.toString());
				if (realSql == null || realSql.isEmpty()) {
					return "";
				}
				return "?" + realSql.substring(1);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public String getSqlStatement(Endpoint oEndpoint) throws Exception {
		try {
			Map<String, String> valuesMap = new HashMap<String, String>();
			for (NameValue nameValue : oEndpoint.getNameValueList()) {
				if (nameValue.getName() == null || nameValue.getName().isEmpty()) {
					continue;
				}
				if (nameValue.getValue() == null || nameValue.getValue().isEmpty()) {
					nameValue.setValue("");
				}
				valuesMap.put(nameValue.getName(), nameValue.getValue());
			}
			String templateString = oEndpoint.getQuery();
			StringSubstitutor sub = new StringSubstitutor(valuesMap);
			String realSql = sub.replace(templateString);
			logger.debug("realSql:" + realSql);
			return realSql;

		} catch (Exception e) {
			throw e;
		}
	}

	public String execute(Endpoint oEndpoint) throws Exception {
		this.getDataSource();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			pstmt = connection.prepareStatement(this.getSqlStatement(oEndpoint));
			resultSet = pstmt.executeQuery();
			Object json = new Convertor().convertToJSON(resultSet);
			return json.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}

	public boolean testConnector(DbConnection oConnector) throws Exception {
		long tstart = System.currentTimeMillis();
		logger.info("Connector:{}", oConnector);

		if (oConnector.getType().equals(DbConnection.SERVER_TYPE_MYSQL)) {
			// Creating connection
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(oConnector.getConnectionString());
				// Getting DatabaseMetaData object
				connection.getMetaData();
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} else if (oConnector.getType().equals(DbConnection.SERVER_TYPE_SQL_SERVER)) {
			// Creating connection
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(oConnector.getConnectionString(), oConnector.getUsername(), oConnector.getPassword());
				// Getting DatabaseMetaData object
				connection.getMetaData();
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} else {
			throw new Exception("Not a valid connector type");
		}
		logExecutionTime(tstart, "testConnector");
		return true;
	}

	private void logExecutionTime(long tstart, String method) {
		logger.info( "Done in " + (System.currentTimeMillis() - tstart) + " milli seconds"  + "" + " - " + method + "");
	}

}