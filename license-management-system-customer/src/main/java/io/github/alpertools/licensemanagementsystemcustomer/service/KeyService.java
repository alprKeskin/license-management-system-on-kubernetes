package io.github.alpertools.licensemanagementsystemcustomer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.*;

@Service
@Slf4j
public class KeyService {

	public static PublicKey generatePublicKey() throws NoSuchAlgorithmException {
		return generateKeyPair().getPublic();
	}

	private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(4096 );
		return keyPairGenerator.generateKeyPair();
	}

}
