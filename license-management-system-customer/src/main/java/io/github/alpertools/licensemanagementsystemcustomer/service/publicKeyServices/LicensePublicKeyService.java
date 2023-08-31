package io.github.alpertools.licensemanagementsystemcustomer.service.publicKeyServices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.PublicKey;

@Service
@Slf4j
public class LicensePublicKeyService {

    @Autowired
    private PublicKeyLoaderService publicKeyLoaderService;

    @Autowired
    private PublicKeyWriterService publicKeyWriterService;

    public void setPublicKey(String publicKeyBase64) throws IOException {
        publicKeyWriterService.writePublicKey(publicKeyBase64);
    }

    public PublicKey getPublicKey() throws Exception {
        return publicKeyLoaderService.loadPublicKey();
    }

    public String getPublicKeyBase64() throws IOException {
        return publicKeyLoaderService.readPublicKeyBase64();
    }

}
