/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.model;

import java.io.Serializable;

/**
 * Name and Value pair for GET parameters
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
public class NameValue implements Serializable {


	private static final long serialVersionUID = -6524385327636128250L;
	
	private String name;
	private String value;
	

	public NameValue(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}


	public NameValue() {
		super();
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}

}
