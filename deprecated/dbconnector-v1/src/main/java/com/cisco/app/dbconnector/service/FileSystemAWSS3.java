/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.cisco.app.dbconnector.model.BasicAuth;
import com.cisco.app.dbconnector.model.DbConnection;
import com.cisco.app.dbconnector.model.Endpoint;
import com.cisco.app.dbconnector.model.MySql;
import com.cisco.app.dbconnector.model.SqlServer;

/**
 * AWS file system storage AKA bucket
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
@Component(value = "fileSystemAWSS3")
public class FileSystemAWSS3 implements FileSystemInterface {
	Logger logger = LoggerFactory.getLogger(FileSystemAWSS3.class);

	@Value("${fileSystemAWSS3.accessKey}")
	protected String accessKey;

	@Value("${fileSystemAWSS3.secretKey}")
	protected String secretKey;

	protected AmazonS3 s3Client = null;

	@Value("${fileSystemAWSS3.bucketName}")
	protected String bucketName = "dbconnector-data";

	@Value("${filesystem.dataDirectory}")
	private String dataDirectory;

	public FileSystemAWSS3() {
		super();
	}

	@Override
	public void writeConnectorToFile(DbConnection oDbConnection) throws Exception {
		FileOutputStream f = null;
		ObjectOutputStream o = null;
		try {
			// this is the active connection
			// create file so it can be sent to S3

			File oFile = new File(this.dataDirectory + "/connector.obj");
			f = new FileOutputStream(oFile);
			o = new ObjectOutputStream(f);
			o.writeObject(oDbConnection);
			o.close();
			f.close();
			// copy file to S3
			this.putFile(oFile.getAbsolutePath());

			// this is the MySQL connection
			// create file so it can be sent to S3
			if (oDbConnection.getType().equals(DbConnection.SERVER_TYPE_MYSQL)) {
				oFile = new File(this.dataDirectory + "/MySql.obj");
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(oDbConnection);
				o.close();
				f.close();
				// copy file to S3
				this.putFile(oFile.getAbsolutePath());
			}
			// this is the SQL Server connection
			// create file so it can be sent to S3
			if (oDbConnection.getType().equals(DbConnection.SERVER_TYPE_SQL_SERVER)) {
				oFile = new File(this.dataDirectory + "/SqlServer.obj");
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(oDbConnection);
				o.close();
				f.close();
				// copy file to S3
				this.putFile(oFile.getAbsolutePath());
			}

		} finally {
			if (o != null) {
				o.close();
			}
			if (f != null) {
				f.close();
			}
		}
	}

	@Override
	public DbConnection readConnectorFromFile() throws Exception {
		// get file from S3
		try {
			byte[] bytes = this.getFile("connector.obj");
			Files.write(Paths.get(this.dataDirectory + "/connector.obj"), bytes);
		} catch (Exception e) {
			this.seedDataFiles();
		}
		/**
		 * read Connector from filesystem
		 */
		ObjectInputStream oi = null;
		DbConnection oDbConnection = null;

		try {
			File oFile = new File(this.dataDirectory + "/connector.obj");
			FileInputStream fi = new FileInputStream(oFile);
			oi = new ObjectInputStream(fi);
			// Read objects
			oDbConnection = (DbConnection) oi.readObject();
		} catch (FileNotFoundException e) {
			// 1st time to run the program and no data files exist
			logger.error(e.getMessage());
			this.seedDataFiles();
			return new MySql();
		} finally {
			if (oi != null) {
				oi.close();
			}
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
	public void seedDataFiles() throws Exception {
		logger.info("using fileSystemInterface: " + this.getClass().getSimpleName());
		logger.info("seedDataFiles...");
		new File(this.dataDirectory).mkdirs();
		FileOutputStream f = null;
		ObjectOutputStream o = null;
		try {
			// this is the active connection
			File oFile = new File(this.dataDirectory + "/connector.obj");
			if (!oFile.exists()) {
				logger.info("CREATING data/connector.obj");
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(new MySql());
				o.close();
				f.close();
				// copy file to S3
				this.putFile(oFile.getAbsolutePath());
			}
			oFile = new File(this.dataDirectory + "/MySql.obj");
			if (!oFile.exists()) {
				logger.info("CREATING data/MySql.obj");
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(new MySql());
				o.close();
				f.close();
				// copy file to S3
				this.putFile(oFile.getAbsolutePath());
			}
			oFile = new File(this.dataDirectory + "/SqlServer.obj");
			if (!oFile.exists()) {
				logger.info("CREATING data/SqlServer.obj");
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(new SqlServer());
				o.close();
				f.close();
				// copy file to S3
				this.putFile(oFile.getAbsolutePath());
			}
			oFile = new File(this.dataDirectory + "/BasicAuth.obj");
			if (!oFile.exists()) {
				logger.info("CREATING data/BasicAuth.obj");
				f = new FileOutputStream(oFile);
				o = new ObjectOutputStream(f);
				o.writeObject(new BasicAuth());
				o.close();
				f.close();
				// copy file to S3
				this.putFile(oFile.getAbsolutePath());
			}
		} finally {
			if (o != null) {
				o.close();
			}
			if (f != null) {
				f.close();
			}
		}
	}

	@Override
	public Object readConnectorFromFile(String serverType) throws Exception {
		// get file from S3
		/**
		 * get from S3 and read Connector from filesystem
		 */
		ObjectInputStream oi = null;

		try {
			if (DbConnection.SERVER_TYPE_MYSQL.equals(serverType)) {
				try {
					byte[] bytes = this.getFile("MySql.obj");
					Files.write(Paths.get(this.dataDirectory + "/MySql.obj"), bytes);
				} catch (Exception e) {
					this.seedDataFiles();
				}
				File oFile = new File(this.dataDirectory + "/MySql.obj");
				FileInputStream fi = new FileInputStream(oFile);
				oi = new ObjectInputStream(fi);
				// Read objects
				return (DbConnection) oi.readObject();
			} else if (DbConnection.SERVER_TYPE_SQL_SERVER.equals(serverType)) {
				try {
					byte[] bytes = this.getFile("SqlServer.obj");
					Files.write(Paths.get(this.dataDirectory + "/SqlServer.obj"), bytes);
				} catch (Exception e) {
					this.seedDataFiles();
				}
				File oFile = new File(this.dataDirectory + "/SqlServer.obj");
				FileInputStream fi = new FileInputStream(oFile);
				oi = new ObjectInputStream(fi);
				// Read objects
				return (DbConnection) oi.readObject();
			} else {
				logger.warn("NOT a valid SERVER_TYPE {}:", serverType);
			}
		} catch (FileNotFoundException e) {
			// 1st time to run the program and no data files exist
			logger.error(e.getMessage());
			this.seedDataFiles();
			return new MySql();
		} finally {
			if (oi != null) {
				oi.close();
			}
		}
		return "{}";
	}

	@Override
	public void writeEndpointToFile(Endpoint oEndpoint) throws Exception {
		/**
		 * create file so it can be sent to S3
		 */
		FileOutputStream f = null;
		ObjectOutputStream o = null;
		try {
			File oFile = new File(this.dataDirectory + "/" + oEndpoint.getName() + ".obj");
			f = new FileOutputStream(oFile);
			o = new ObjectOutputStream(f);
			o.writeObject(oEndpoint);
			o.close();
			f.close();
			// copy file to S3
			this.putFile(oFile.getAbsolutePath());

		} finally {
			if (o != null) {
				o.close();
			}
			if (f != null) {
				f.close();
			}
		}

	}

	private Endpoint readEndpointFromFile(File fileName) throws Exception {
		// get file from S3
		try {
			byte[] bytes = this.getFile(fileName.getName());
			Files.write(Paths.get(this.dataDirectory + "/" + fileName), bytes);
		} catch (Exception e) {
//			this.seedDataFiles();
		}
		/**
		 * read Connector from file
		 */
		ObjectInputStream oi = null;
		// Read objects
		Endpoint oEndpoint = null;
		try {
			FileInputStream fi = new FileInputStream(fileName);
			oi = new ObjectInputStream(fi);
			// Read objects
			oEndpoint = (Endpoint) oi.readObject();

		} finally {
			if (oi != null) {
				oi.close();
			}
		}
		return oEndpoint;
	}

	@Override
	public List<Endpoint> loadEndpointsFromFile() throws Exception {
		// get Endpoints from S3
		List<String> fileList = this.listBucket();
		for (String file : fileList) {
			byte[] bytes = this.getFile(file);
			Files.write(Paths.get(this.dataDirectory + "/" + file), bytes);
		}
		/**
		 * get Endpoints from filesystem
		 */
		List<Endpoint> lists = new ArrayList<Endpoint>();
		File[] files = new File(this.dataDirectory).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				logger.debug("name:" + name);
				try {
					name = name.replaceFirst("\\.obj", "");
					// check to see if the file name is a UUID AKA and endpoint file name
					UUID.fromString(name);
					return true;
				} catch (java.lang.IllegalArgumentException e) {
					logger.debug("Not an endpoint:" + name);
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
	public BasicAuth readBasicAuthFromFile() throws Exception {
		// get file from S3
		try {
			byte[] bytes = this.getFile("BasicAuth.obj");
			Files.write(Paths.get(this.dataDirectory + "/BasicAuth.obj"), bytes);
		} catch (Exception e) {
			this.seedDataFiles();
			return new BasicAuth();
		}
		/**
		 * read BasicAuth from filesystem
		 */
		ObjectInputStream oi = null;
		try {
			File oFile = new File(this.dataDirectory + "/BasicAuth.obj");
			FileInputStream fi = new FileInputStream(oFile);
			oi = new ObjectInputStream(fi);
			// Read objects
			return (BasicAuth) oi.readObject();
		} catch (FileNotFoundException e) {
			// 1st time to run the program and no data files exist
			logger.error(e.getMessage());
			this.seedDataFiles();
			return new BasicAuth();
		} finally {
			if (oi != null) {
				oi.close();
			}
		}
	}

	@Override
	public void writeBasicAuthToFile(BasicAuth basicAuth) throws Exception {
		FileOutputStream f = null;
		ObjectOutputStream o = null;

		try {
			// create file so it can be sent to S3
			File oFile = new File(this.dataDirectory + "/BasicAuth.obj");
			f = new FileOutputStream(oFile);
			o = new ObjectOutputStream(f);
			o.writeObject(basicAuth);
			o.close();
			f.close();
			// copy file to S3
			this.putFile(oFile.getAbsolutePath());
		} finally {
			if (o != null) {
				o.close();
			}
			if (f != null) {
				f.close();
			}
		}

	}

	public List<String> listBucket() {
		List<String> returnThis = new ArrayList<String>();
		try {
			List<S3ObjectSummary> fileList = s3Client.listObjects(bucketName).getObjectSummaries();
			for (S3ObjectSummary file : fileList) {
				logger.info("listBucket:file:" + bucketName + ":" + file.getKey());
				if (file.getKey().endsWith(".obj")) {
					returnThis.add(file.getKey());
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return returnThis;
	}

	public byte[] getFile(String absolutePath) throws Exception {
		logger.debug("bucketName:" + bucketName);
		logger.debug("getFile:" + absolutePath);
		S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, absolutePath));
		logger.debug("getContentType: " + s3object.getObjectMetadata().getContentType());
		logger.debug("getContentDisposition: " + s3object.getObjectMetadata().getContentDisposition());
		logger.debug("getContentEncoding: " + s3object.getObjectMetadata().getContentEncoding());
		InputStream objectData = s3object.getObjectContent();
		byte[] bytes = IOUtils.toByteArray(objectData);
		objectData.close();
		return bytes;
	}

	public void putFile(String absolutePath) throws Exception {
		logger.debug("bucketName:" + bucketName);
		logger.debug("putFile:" + absolutePath);
		byte[] bytes = Files.readAllBytes(Paths.get(absolutePath));

//		createFolder(bucketName, folderName, s3Client);

		InputStream fis = new ByteArrayInputStream(bytes);

		// AmazonS3 s3 = new AmazonS3Client();
		// Region usEast1 = Region.getRegion(Regions.US_EAST_1);
		// s3.setRegion(usEast1);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(bytes.length);

		String fileName = new File(absolutePath).getName();
		s3Client.putObject(bucketName, fileName, fis, metadata);
		s3Client.setObjectAcl(bucketName, fileName, CannedAccessControlList.Private);
	}

	@Override
	public void deleteEndpoint(String endpointName) throws Exception {
		new File(this.dataDirectory + "/" + endpointName + ".obj").delete();
		s3Client.deleteObject(bucketName, endpointName + ".obj");
	}
	
	@PostConstruct
	private void createConnection() {
		s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
	}
}
