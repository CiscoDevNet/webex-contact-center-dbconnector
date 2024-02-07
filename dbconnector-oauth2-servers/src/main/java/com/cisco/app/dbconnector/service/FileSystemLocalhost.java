/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cisco.app.dbconnector.model.BasicAuth;
import com.cisco.app.dbconnector.model.DbConnection;
import com.cisco.app.dbconnector.model.Endpoint;
import com.cisco.app.dbconnector.model.MySql;
import com.cisco.app.dbconnector.model.Oracle;
import com.cisco.app.dbconnector.model.SqlServer;
import com.cisco.app.dbconnector.util.Cypher2021;

/**
 * localhost file system storage
 * 
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
@Component(value = "fileSystemLocalhost")
public class FileSystemLocalhost implements FileSystemInterface {
	Logger logger = LoggerFactory.getLogger(FileSystemLocalhost.class);
	@Value("${filesystem.dataDirectory}")
	private String dataDirectory;

	@Autowired
	Cypher2021 cypher2021;

	public FileSystemLocalhost() {
		super();
		logger.info("public FileSystemLocalhost");
	}

	@Override
	public synchronized void writeConnectorToFile(DbConnection oDbConnection) throws Exception {
		FileOutputStream f = null;
		ObjectOutputStream o = null;
		try {
			// this is the active connection
			File oFile = new File(this.dataDirectory + "/" + DbConnection.FILE_NAME);
			oFile.getParentFile().mkdirs();
			f = new FileOutputStream(oFile);
			o = new ObjectOutputStream(f);
			o.writeObject(oDbConnection);
			o.close();
			f.close();
			cypher2021.encrypt(oFile);

			// this is the MySQL connection
			if (oDbConnection.getType().equals(DbConnection.SERVER_TYPE_MYSQL)) {
				oFile = new File(this.dataDirectory + "/" + MySql.FILE_NAME);
				oFile.getParentFile().mkdirs();
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(oDbConnection);
				o.close();
				f.close();
				cypher2021.encrypt(oFile);
			}
			if (oDbConnection.getType().equals(DbConnection.SERVER_TYPE_SQL_SERVER)) {
				oFile = new File(this.dataDirectory + "/" + SqlServer.FILE_NAME);
				oFile.getParentFile().mkdirs();
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(oDbConnection);
				o.close();
				f.close();
				cypher2021.encrypt(oFile);
			}
			if (oDbConnection.getType().equals(DbConnection.SERVER_TYPE_ORACLE)) {
				oFile = new File(this.dataDirectory + "/" + Oracle.FILE_NAME);
				oFile.getParentFile().mkdirs();
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(oDbConnection);
				o.close();
				f.close();
				cypher2021.encrypt(oFile);
			}
			// this is the SQL connection

		} finally {
		}
	}

	@Override
	public synchronized DbConnection readConnectorFromFile() throws Exception {
		/**
		 * read Connector from file
		 */
		ObjectInputStream oi = null;
		DbConnection oDbConnection = null;

		try {
			File oFile = new File(this.dataDirectory + "/" + DbConnection.FILE_NAME);
			cypher2021.decrypt(oFile);
			FileInputStream fi = new FileInputStream(oFile);
			oi = new ObjectInputStream(fi);
			// Read objects
			Object o = oi.readObject();
			oi.close();
			cypher2021.encrypt(oFile);
			oDbConnection = (DbConnection) o;
		} catch (FileNotFoundException e) {
			// 1st time to run the program and no data files exist
			logger.error("OK if 1st time to run the program and no data files exist:" + e.getMessage());
			this.seedDataFiles();
			return new MySql();
		} finally {
		}
		return oDbConnection;
	}

	/**
	 * create empty objects of they are not null when the web site retrieves the
	 * objects
	 * 
	 * @throws Exception
	 */
	@Override
	public synchronized void seedDataFiles() throws Exception {
		logger.debug("using fileSystemInterface: {}", this.getClass().getSimpleName());
		logger.debug("seedDataFiles...");
		new File(this.dataDirectory).mkdirs();
		FileOutputStream f = null;
		ObjectOutputStream o = null;
		try {
			// this is the active connection
			File oFile = new File(this.dataDirectory + "/" + DbConnection.FILE_NAME);
			if (!oFile.exists()) {
				logger.debug("CREATING {}/{}", this.dataDirectory, DbConnection.FILE_NAME);

				oFile.getParentFile().mkdirs();
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(new MySql());
				o.close();
				f.close();
				cypher2021.encrypt(oFile);
			}
			oFile = new File(this.dataDirectory + "/" + MySql.FILE_NAME);
			if (!oFile.exists()) {
				logger.debug("CREATING {}/{}", this.dataDirectory, MySql.FILE_NAME);
				oFile.getParentFile().mkdirs();
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(new MySql());
				o.close();
				f.close();
				cypher2021.encrypt(oFile);
			}
			oFile = new File(this.dataDirectory + "/" + SqlServer.FILE_NAME);
			if (!oFile.exists()) {
				logger.debug("CREATING {}/{}", this.dataDirectory, SqlServer.FILE_NAME);
				oFile.getParentFile().mkdirs();
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(new SqlServer());
				o.close();
				f.close();
				cypher2021.encrypt(oFile);
			}
			oFile = new File(this.dataDirectory + "/" + Oracle.FILE_NAME);
			if (!oFile.exists()) {
				logger.debug("CREATING {}/{}", this.dataDirectory, Oracle.FILE_NAME);
				oFile.getParentFile().mkdirs();
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				Oracle oDbServer = new Oracle();
				o.writeObject(oDbServer);
				o.close();
				f.close();
				cypher2021.encrypt(oFile);
			}
			oFile = new File(this.dataDirectory + "/" + BasicAuth.FILE_NAME);
			if (!oFile.exists()) {
				logger.debug("CREATING {}/{}", this.dataDirectory, BasicAuth.FILE_NAME);
				oFile.getParentFile().mkdirs();
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(new BasicAuth());
				o.close();
				f.close();
				cypher2021.encrypt(oFile);
			}
		} finally {
		}
	}

	@Override
	public synchronized Object readConnectorFromFile(String serverType) throws Exception {
		/**
		 * read Connector from file
		 */
		ObjectInputStream oi = null;
		FileInputStream fi = null;
		try {
			if (DbConnection.SERVER_TYPE_MYSQL.equals(serverType)) {
				File oFile = new File(this.dataDirectory + "/" + MySql.FILE_NAME);
				cypher2021.decrypt(oFile);
				fi = new FileInputStream(oFile);
				oi = new ObjectInputStream(fi);
				Object o = oi.readObject();
				fi.close();
				oi.close();
				cypher2021.encrypt(oFile);
				// Read objects
				return (DbConnection) o;
			} else if (DbConnection.SERVER_TYPE_SQL_SERVER.equals(serverType)) {
				File oFile = new File(this.dataDirectory + "/" + SqlServer.FILE_NAME);
				cypher2021.decrypt(oFile);
				fi = new FileInputStream(oFile);
				oi = new ObjectInputStream(fi);
				Object o = oi.readObject();
				fi.close();
				oi.close();
				cypher2021.encrypt(oFile);
				// Read objects
				return (DbConnection) o;
			} else if (DbConnection.SERVER_TYPE_ORACLE.equals(serverType)) {
				File oFile = new File(this.dataDirectory + "/" + Oracle.FILE_NAME);
				cypher2021.decrypt(oFile);
				fi = new FileInputStream(oFile);
				oi = new ObjectInputStream(fi);
				Object o = oi.readObject();
				fi.close();
				oi.close();
				cypher2021.encrypt(oFile);
				// Read objects
				return (DbConnection) o;
			} else {
				logger.warn("NOT a valid SERVER_TYPE {}:", serverType);
			}
		} catch (FileNotFoundException e) {
			// 1st time to run the program and no data files exist
			logger.error("OK if 1st time to run the program and no data files exist:" + e.getMessage());
			this.seedDataFiles();
			return new MySql();
		} finally {
		}
		return "{}";
	}

	@Override
	public synchronized void writeEndpointToFile(Endpoint oEndpoint) throws Exception {
		/**
		 * write Endpoint to file
		 */
		FileOutputStream f = null;
		ObjectOutputStream o = null;
		try {
			File oFile = new File(this.dataDirectory + "/" + oEndpoint.getName() + ".obj");
			oFile.getParentFile().mkdirs();
			f = new FileOutputStream(oFile);
			o = new ObjectOutputStream(f);
			o.writeObject(oEndpoint);
			o.close();
			f.close();
			cypher2021.encrypt(oFile);
		} finally {
		}

	}

	private synchronized Endpoint readEndpointFromFile(File oFile) throws Exception {
		/**
		 * read Connector from file
		 */
		ObjectInputStream oi = null;
		FileInputStream fi = null;
		// Read objects
		Endpoint oEndpoint = null;

		try {
			cypher2021.decrypt(oFile);
			fi = new FileInputStream(oFile);
			oi = new ObjectInputStream(fi);
			Object o = oi.readObject();
			fi.close();
			oi.close();
			cypher2021.encrypt(oFile);
			// Read objects
			oEndpoint = (Endpoint) o;

		} finally {
		}
		return oEndpoint;
	}

	@Override
	public synchronized List<Endpoint> loadEndpointsFromFile() throws Exception {
		/**
		 * get Endpoints from file system
		 */
		List<Endpoint> lists = new ArrayList<>();
		File[] files = new File(this.dataDirectory).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				logger.debug("loadEndpointsFromFile: name:{}", name);
				try {
					name = name.replaceFirst(".obj", "");
					UUID.fromString(name);
					return true;
				} catch (Exception e) {
					logger.debug("Not an endpoint:{}", name);
					return false;
				}
			}
		});
//		Arrays.sort(files);
		for (File file : files) {
			lists.add(readEndpointFromFile(file));
		}
		return lists;
	}

	@Override
	public synchronized BasicAuth readBasicAuthFromFile() throws Exception {
		/**
		 * read BasicAuth from file
		 */
		ObjectInputStream oi = null;
		try {
			File oFile = new File(this.dataDirectory + "/" + BasicAuth.FILE_NAME);
			cypher2021.decrypt(oFile);
			FileInputStream fi = new FileInputStream(oFile);
			oi = new ObjectInputStream(fi);
			Object o = oi.readObject();
			fi.close();
			oi.close();
			cypher2021.encrypt(oFile);
			// Read objects
			return (BasicAuth) o;
		} catch (FileNotFoundException e) {
			// 1st time to run the program and no data files exist
			logger.error("OK if 1st time to run the program and no data files exist:" + e.getMessage());
			this.seedDataFiles();
			return new BasicAuth();
		} finally {
		}
	}

	@Override
	public synchronized void writeBasicAuthToFile(BasicAuth basicAuth) throws Exception {
		FileOutputStream f = null;
		ObjectOutputStream o = null;

		try {
			File oFile = new File(this.dataDirectory + "/" + BasicAuth.FILE_NAME);
			oFile.getParentFile().mkdirs();
			f = new FileOutputStream(oFile);
			o = new ObjectOutputStream(f);
			o.writeObject(basicAuth);
			o.close();
			f.close();
			cypher2021.encrypt(oFile);
		} finally {
		}

	}

	@Override
	public synchronized void deleteEndpoint(String endpointName) throws Exception {
		new File(this.dataDirectory + "/" + endpointName + ".obj").delete();
	}
}
