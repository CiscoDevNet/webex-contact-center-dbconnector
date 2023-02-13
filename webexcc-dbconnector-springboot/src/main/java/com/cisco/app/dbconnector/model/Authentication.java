package com.cisco.app.dbconnector.model;

import java.io.Serializable;

//import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
//import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Authentication oAuthentication = om.readValue(myJsonString), Authentication.class); */
public class Authentication implements Serializable {
	private static final long serialVersionUID = 3177456313868498447L;
	public String access_token;
	public String expires_in;
	public String refresh_token;
	public String refresh_token_expires_in;
	public String token_type;
	public String orginzationId;
	public String scope;
}
