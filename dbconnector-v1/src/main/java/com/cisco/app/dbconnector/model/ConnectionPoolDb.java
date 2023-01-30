/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.model;

/**
 * implement this call for new connection pool
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
public interface ConnectionPoolDb {

	String getInitialPoolSize();

	void setInitialPoolSize(String initialPoolSize);

	String getMinPoolSize();

	void setMinPoolSize(String minPoolSize);

	String getAcquireIncrement();

	void setAcquireIncrement(String acquireIncrement);

	String getMaxPoolSize();

	void setMaxPoolSize(String maxPoolSize);

	String getMaxStatements();

	void setMaxStatements(String maxStatements);

	String getUnreturnedConnectionTimeout();

	void setUnreturnedConnectionTimeout(String unreturnedConnectionTimeout);

}