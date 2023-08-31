package io.github.alpertools.licensemanagementsystemvendor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Slf4j
public class KeyService {

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        // create a key pair generator object using static factory method get instance
        // this key pair generator object uses RSA algorithm
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        // initialize the key pair generator object with the key size of 2048 bits (256 bytes)
        keyPairGenerator.initialize(2048);
        // return a key pair
        // one of them will be public key, and the other one is the private key
        return keyPairGenerator.generateKeyPair();
    }

    public static String extractPublicKeyBase64(KeyPair keyPair) {
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        return Base64.getEncoder().encodeToString(publicKeyBytes);
    }
}
