package com.cisco.app.dbconnector.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Cypher2021 {
	Logger logger = LoggerFactory.getLogger(Cypher2021.class);

	@Value("${client_id}")
	private  String key ;
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";

	public void encrypt(File inputFile) throws Exception {
		File encryptedFile = new File(inputFile.getAbsolutePath() + ".encrypted");
		encryptToNewFile(inputFile, encryptedFile);
		renameToOldFilename(inputFile, encryptedFile);
	}

	public void decrypt(File inputFile) throws Exception {
		File decryptedFile = new File(inputFile.getAbsolutePath() + ".decrypted");
		decryptToNewFile(inputFile, decryptedFile);
		renameToOldFilename(inputFile, decryptedFile);
	}

	private void decryptToNewFile(File input, File output) throws Exception {
		try (FileInputStream inputStream = new FileInputStream(input);
				FileOutputStream outputStream = new FileOutputStream(output)) {
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			byte[] buff = new byte[1024];
			for (int readBytes = inputStream.read(buff); readBytes > -1; readBytes = inputStream.read(buff)) {
				outputStream.write(cipher.update(buff, 0, readBytes));
			}
			outputStream.write(cipher.doFinal());
		} catch (Exception e) {
			throw e;
		}
	}

	private void encryptToNewFile(File inputFile, File outputFile) throws Exception {
		try (FileInputStream inputStream = new FileInputStream(inputFile);
				FileOutputStream outputStream = new FileOutputStream(outputFile)) {
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] inputBytes = new byte[4096];
			for (int n = inputStream.read(inputBytes); n > 0; n = inputStream.read(inputBytes)) {
				byte[] outputBytes = cipher.update(inputBytes, 0, n);
				outputStream.write(outputBytes);
			}
			byte[] outputBytes = cipher.doFinal();
			outputStream.write(outputBytes);
		} catch (Exception e) {
			throw e;
		}
	}

	private void renameToOldFilename(File oldFile, File newFile) {
		try {
			oldFile.delete();
			Files.copy(newFile.toPath(), oldFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			newFile.delete();
		} catch (Exception e) {
			logger.error("Exception", e);	
		} finally {
		}

	}
	

	@PostConstruct
	private void fixSecretKeyLength() {
		try {
			while (key.length() < 32) {
				key += "0";
			}
		} catch (Exception e) {
		}
		try {
			key = key.substring(0, 32);
		} catch (Exception e) {
			logger.error("Exception", e);	
		}
	}

}