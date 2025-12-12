package com.cisco.webexcc.dbconnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbConnectorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DbConnectorApplication.class);

	@Value("${spring.security.oauth2.client.registration.webex.redirect-uri}")
	private String redirectUri;

	@Value("${server.port:8080}")
	private String serverPort;

	@Value("${server.servlet.context-path:}")
	private String contextPath;

	public static void main(String[] args) {
		SpringApplication.run(DbConnectorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String baseUrl = "http://localhost:" + serverPort + contextPath;
		logger.info("Application started successfully.");
		logger.info("Redirect URI Pattern: {}", redirectUri);
		logger.info("Expected Base URL: {}", baseUrl);
		logger.info("Expected Full Redirect URI: {}/login/oauth2/code/webex", baseUrl);
	}

}
