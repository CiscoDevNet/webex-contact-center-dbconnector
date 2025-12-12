package com.cisco.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.oauth2.resourceserver.OAuth2ResourceServerSecurityMarker;

@SpringBootApplication
@ComponentScan("com.cisco")
public class DbconnectorOauth2Application {

	public static void main(String[] args) {
		SpringApplication.run(DbconnectorOauth2Application.class, args);
	}

}
