package com.cisco.app.dbconnector.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class Cypher2021 {

	private static final String key = "${client_secret}";
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
		if (oldFile.exists()) {
			oldFile.delete();
		}
		newFile.renameTo(oldFile);
	}

}
