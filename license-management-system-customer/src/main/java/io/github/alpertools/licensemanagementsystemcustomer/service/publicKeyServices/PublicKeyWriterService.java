package io.github.alpertools.licensemanagementsystemcustomer.service.publicKeyServices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class PublicKeyWriterService {

    @Value("${license.key.file.public}")
    private String PUBLIC_KEY_SAVE_FILE;

    public void writePublicKey(String publicKeyBase64) throws IOException {
        saveStringToTxtFile(publicKeyBase64, PUBLIC_KEY_SAVE_FILE);
    }

    private void saveStringToTxtFile(String msg, String fileName) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            fileOutputStream.write(msg.getBytes());
        }
    }

}
