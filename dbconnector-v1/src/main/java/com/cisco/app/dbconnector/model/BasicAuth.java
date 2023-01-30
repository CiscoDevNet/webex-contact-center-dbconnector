/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.model;

import java.io.Serializable;

/**
 * Basic Authentication for endpoints
 * @author jiwyatt
 * @since 12/12/2020
 */
public class BasicAuth implements Serializable {

	private static final long serialVersionUID = -7243048278496580603L;

	private String username = "";
	private String password = "";
	private String value = "";
	private boolean isBasicAuthenticationRequired = false;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean getIsBasicAuthenticationRequired() {
		return isBasicAuthenticationRequired;
	}

	public void setBasicAuthenticationRequired(boolean isBasicAuthenticationRequired) {
		this.isBasicAuthenticationRequired = isBasicAuthenticationRequired;
	}

}
