/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.service;

import java.util.List;

import com.cisco.app.dbconnector.model.BasicAuth;
import com.cisco.app.dbconnector.model.DbConnection;
import com.cisco.app.dbconnector.model.Endpoint;

/**
 * implement this class for new file systems 
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
public interface FileSystemInterface {

	void writeConnectorToFile(DbConnection oDbConnection) throws Exception;

	DbConnection readConnectorFromFile() throws Exception;

	/**
	 * create empty objects of they are not null when the web site retrieves the
	 * objects
	 * 
	 * @throws Exception
	 */
	void seedDataFiles() throws Exception;

	Object readConnectorFromFile(String serverType) throws Exception;

	void writeEndpointToFile(Endpoint oEndpoint) throws Exception;

	List<Endpoint> loadEndpointsFromFile() throws Exception;

	BasicAuth readBasicAuthFromFile() throws Exception;

	void writeBasicAuthToFile(BasicAuth basicAuth) throws Exception;

	void deleteEndpoint(String endpointName) throws Exception;

}