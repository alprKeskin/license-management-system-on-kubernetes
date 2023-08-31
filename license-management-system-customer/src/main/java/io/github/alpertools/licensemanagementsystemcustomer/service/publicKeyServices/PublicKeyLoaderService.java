package io.github.alpertools.licensemanagementsystemcustomer.service.publicKeyServices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@Slf4j
public class PublicKeyLoaderService {

    @Value("${license.key.file.public}")
    private String PUBLIC_KEY_SAVE_FILE;

    public PublicKey loadPublicKey() throws Exception {
        String publicKeyBase64 = readFileContent(PUBLIC_KEY_SAVE_FILE);
        return convertPublicKeyBase64ToPublicKey(publicKeyBase64);
    }

    public String readPublicKeyBase64() throws IOException {
        return readFileContent(PUBLIC_KEY_SAVE_FILE);
    }

    private String readFileContent(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = br.readLine()) != null) {
                line = line.replace("\r", "").replace("\n", "");
                content.append(line);
            }
        }
        return content.toString();
    }

    private PublicKey convertPublicKeyBase64ToPublicKey(String publicKeyBase64) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64); // error
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

}
