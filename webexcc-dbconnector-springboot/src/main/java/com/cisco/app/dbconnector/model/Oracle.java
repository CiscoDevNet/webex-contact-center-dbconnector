/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.model;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Microsoft SQL Server class
 * 
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
public class Oracle implements Serializable, DbConnection {

	private static final long serialVersionUID = 506087301723961948L;
	public static final String FILE_NAME = "Oracle.obj";
	private String type = DbConnection.SERVER_TYPE_ORACLE;
	private String version = "-";
	private String hostname = "192.168.1.11";
	private String port = "1521";
	private String database = "xe";
	private String username = "system";
	private String password = "oracle";
	private String driver = "ojdbc11-21.5.0.0.jar";
	private String connectionString = "jdbc:oracle:thin:@192.168.1.11:1521:xe";

	@JsonDeserialize(as = ConnectionPoolC3p0.class)
	private ConnectionPoolDb connectionPool = new ConnectionPoolC3p0();

	public Oracle() {
		super();
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String getHostname() {
		return hostname;
	}

	@Override
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@Override
	public String getPort() {
		return port;
	}

	@Override
	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public String getDatabase() {
		return database;
	}

	@Override
	public void setDatabase(String database) {
		this.database = database;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getDriver() {
		return driver;
	}

	@Override
	public void setDriver(String driver) {
		this.driver = driver;
	}

	@Override
	public String getConnectionString() {
		return connectionString;
	}

	@Override
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	@Override
	public ConnectionPoolDb getConnectionPool() {
		return connectionPool;
	}

	@Override
	public void setConnectionPool(ConnectionPoolDb connectionPool) {
		this.connectionPool = connectionPool;
	}

	@Override
	public String toString() {
		// @formatter:off
		return "Oracle ["
				+ "type=" + type + ", "
				+ "version=" + version + ", "
				+ "hostname=" + hostname + ", "
				+ "port=" + port + ", "
				+ "database=" + database + ", "
				+ "username=" + username + ", "
				+ "password=" + "*****" + ", "
				+ "driver=" + driver + ", "
				+ "connectionString=" + connectionString + ", "
				+ "connectionPool=" + connectionPool + "]";
		// @formatter:on
	}

}
