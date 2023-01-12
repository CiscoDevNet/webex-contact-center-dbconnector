package com.cisco.app.dbconnector;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cisco.app.dbconnector.util.Cypher2021;

@SpringBootTest
class DbConnectorTests {
 

	@Test
	public void encrypt() {
		try {
			File file = File.createTempFile("temp", null);
			new Cypher2021().encrypt(file);
			file.deleteOnExit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void decrypt() {
		try {
			File file = File.createTempFile("temp", null);
			new Cypher2021().decrypt(file);
			file.deleteOnExit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
