package com.cisco.app.dbconnector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.cisco.app.dbconnector.controller.WebControllerRest;
import com.cisco.app.dbconnector.util.Cypher2021;

@SpringBootTest
@AutoConfigureMockMvc
class DbConnectorTests {
	static Logger logger = LoggerFactory.getLogger(WebControllerRest.class);
//
//	@Autowired
//	MockMvc mockMvc;
//
//	@Test
//	void encrypt() {
//		try {
//			File file = File.createTempFile("temp", null);
//			new Cypher2021().encrypt(file);
//			assertThat(file).exists();
//			file.deleteOnExit();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Test
//	void decrypt() {
//		try {
//			File file = File.createTempFile("temp", null);
//			new Cypher2021().decrypt(file);
//			assertThat(file).exists();
//			file.deleteOnExit();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	void doesItWork() {
//		Object[] o = new Object[0];
//		try {
//			MvcResult oMvcResult = mockMvc.perform(get("/doesItWork", o)).andExpect(status().isOk()).andReturn();
//			System.out.println(oMvcResult.getResponse().getContentAsString(Charset.defaultCharset()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	
//	@Test
//	void getConnector() {
//		Object[] o = new Object[0];
//		try {
//			MvcResult oMvcResult = mockMvc.perform(get("/rest/connector", o)).andExpect(status().isOk()).andReturn();
//			logger.info("\nrest/connector:\n{}", oMvcResult.getResponse().getContentAsString(Charset.defaultCharset()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	@Test
//	void getConnectorByServerType() {
//		Object[] o = new Object[0];
//		try {
//			MvcResult oMvcResult = mockMvc.perform(get("/rest/connector/MySQL", o)).andExpect(status().isOk()).andReturn();
//			logger.info("\nrest/connector/MySQL:\n{}", oMvcResult.getResponse().getContentAsString(Charset.defaultCharset()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	void getEndpoints() {
//		Object[] o = new Object[0];
//		try {
//			MvcResult oMvcResult = mockMvc.perform(get("/rest/endpoints", o)).andExpect(status().isOk()).andReturn();
//			logger.info("\nrest/endpoints:\n{}", oMvcResult.getResponse().getContentAsString(Charset.defaultCharset()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	@Test
//	void getBasicAuth() {
//		Object[] o = new Object[0];
//		try {
//			MvcResult oMvcResult = mockMvc.perform(get("/rest/basicauth", o)).andExpect(status().isOk()).andReturn();
//			logger.info("\nrest/basicauth:\n{}", oMvcResult.getResponse().getContentAsString(Charset.defaultCharset()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	  @Test
//	  void registrationWorksThroughAllLayers() throws Exception {
//	    UserResource user = new UserResource("Zaphod", "zaphod@galaxy.net");
//
//	    mockMvc.perform(post("/forums/{forumId}/register", 42L)
//	            .contentType("application/json")
//	            .param("sendWelcomeMail", "true")
//	            .content(objectMapper.writeValueAsString(user)))
//	            .andExpect(status().isOk());
//
//	    UserEntity userEntity = userRepository.findByName("Zaphod");
//	    assertThat(userEntity.getEmail()).isEqualTo("zaphod@galaxy.net");
//	  }

}
