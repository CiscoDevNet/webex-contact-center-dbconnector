/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.model;

import java.io.Serializable;

/**
 * C3p0 connection pool
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
public class ConnectionPoolC3p0 implements Serializable, ConnectionPoolDb {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4851890835088791994L;
	private String initialPoolSize = "10";
	private String minPoolSize = "10";
	private String acquireIncrement = "1";
	private String maxPoolSize = "20";
	private String maxStatements = "10";
	private String unreturnedConnectionTimeout = "7";
	public ConnectionPoolC3p0() {
		super();
	}

	public static void main(String[] args) {

	}

	@Override
	public String getInitialPoolSize() {
		return initialPoolSize;
	}

	@Override
	public void setInitialPoolSize(String initialPoolSize) {
		this.initialPoolSize = initialPoolSize;
	}

	@Override
	public String getMinPoolSize() {
		return minPoolSize;
	}

	@Override
	public void setMinPoolSize(String minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	@Override
	public String getAcquireIncrement() {
		return acquireIncrement;
	}

	@Override
	public void setAcquireIncrement(String acquireIncrement) {
		this.acquireIncrement = acquireIncrement;
	}

	@Override
	public String getMaxPoolSize() {
		return maxPoolSize;
	}

	@Override
	public void setMaxPoolSize(String maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	@Override
	public String getMaxStatements() {
		return maxStatements;
	}

	@Override
	public void setMaxStatements(String maxStatements) {
		this.maxStatements = maxStatements;
	}

	@Override
	public String getUnreturnedConnectionTimeout() {
		return unreturnedConnectionTimeout;
	}

	@Override
	public void setUnreturnedConnectionTimeout(String unreturnedConnectionTimeout) {
		this.unreturnedConnectionTimeout = unreturnedConnectionTimeout;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ConnectionPoolC3p0 [initialPoolSize=" + initialPoolSize + ", minPoolSize=" + minPoolSize
				+ ", acquireIncrement=" + acquireIncrement + ", maxPoolSize=" + maxPoolSize + ", maxStatements="
				+ maxStatements + ", unreturnedConnectionTimeout=" + unreturnedConnectionTimeout + "]";
	}

}
