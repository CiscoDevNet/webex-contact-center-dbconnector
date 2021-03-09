/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.model;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * MySQL connection
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
public class MySql implements Serializable, DbConnection {


	private static final long serialVersionUID = -7986723323564998747L;
	private String type = DbConnection.SERVER_TYPE_MYSQL;
	private String version = "5.7.23";
	private String hostname = "localhost";
	private String port = "3306";
	private String database = "test";
	private String username = "test";
	private String password = "test";
	private String driver = "mysql-connector-java-8.0.20.jar";
	private String connectionString = "jdbc:mysql://localhost:3306/test?user=test&password=test&serverTimezone=UTC&useLegacyDatetimeCode=false&useSSL=FALSE";

	@JsonDeserialize(as = ConnectionPoolC3p0.class)
	private ConnectionPoolDb connectionPool = new ConnectionPoolC3p0();

	public MySql() {
		super();
	}

	public static void main(String[] args) {

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
		return "MySql [type=" + type + ", version=" + version + ", hostname=" + hostname + ", port=" + port + ", database=" + database + ", username=" + username + ", password=" + password + ", driver=" + driver + ", connectionString=" + connectionString + ", connectionPool=" + connectionPool + "]";
	}

}
