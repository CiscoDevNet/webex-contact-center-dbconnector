/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * WebexCC endpoints
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
public class Endpoint implements Serializable {

	private static final long serialVersionUID = 7175170792578658670L;
	private String name = "";
	private String query = "";
	private String description = "";
	private String endpoint = "";
	private List<NameValue> nameValueList = new ArrayList<>();

	public Endpoint() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String sql) {
		this.query = sql;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String toString() {
		return "Endpoint [endpoint=" + endpoint + ", description=" + description + "]";
	}

	public List<NameValue> getNameValueList() {
		return nameValueList;
	}

	public void setNameValueList(List<NameValue> nameValueList) {
		this.nameValueList = nameValueList;
	}

	public void updateNameValueList(Entry<String, String> entry) {
		for (NameValue nameValue : this.nameValueList) {
			if (nameValue.getName().equals(entry.getKey())) {
				nameValue.setValue(entry.getValue());
				return;
			}
		}
	}

}
