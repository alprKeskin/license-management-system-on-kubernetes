package io.github.alpertools.licensemanagementsystemvendor.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Base64;

public class Utils {

	public static void savePublicKeyToTxtFile(PublicKey publicKey, String fileName) throws IOException {
		byte[] publicKeyBytes = publicKey.getEncoded();
		String base64PublicKey = Base64.getEncoder().encodeToString(publicKeyBytes);
		saveStringToTxtFile(base64PublicKey, fileName);
	}

	public static void saveStringToTxtFile(String msg, String fileName) throws IOException {
		try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
			fileOutputStream.write(msg.getBytes());
		}
	}

}
