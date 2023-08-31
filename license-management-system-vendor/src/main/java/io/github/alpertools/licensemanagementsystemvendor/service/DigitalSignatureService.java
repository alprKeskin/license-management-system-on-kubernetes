package io.github.alpertools.licensemanagementsystemvendor.service;

import io.github.alpertools.licensemanagementsystemvendor.model.License;
import io.github.alpertools.licensemanagementsystemvendor.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.*;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
public class DigitalSignatureService {

    @Value("${license.file.digital.signature}")
    private String DIGITAL_SIGNATURE_SAVE_FILE;

    @Autowired
    private LicenseDatabaseService licenseDatabaseService;


    public String generateDigitalSignature(Long licenseId) throws Exception {
        Optional<License> licenseToBeSigned = licenseDatabaseService.getLicenseById(licenseId);
        // if there is a license with the given id in the system
        if (licenseToBeSigned.isPresent()) {
            // get the raw license
            License license = licenseToBeSigned.get();
            String licenseFingerprint = license.getLicenseFingerprint();
            String digitalSignature = getDigitalSignature(hashEncryptedLicense(licenseFingerprint), license.getKeyPair().getPrivate());
            Utils.saveStringToTxtFile(digitalSignature, DIGITAL_SIGNATURE_SAVE_FILE);
            return digitalSignature;
        }
        // if there is no license with the given id in the system
        return "No license found with the given license id.";
    }

    private String getDigitalSignature(String data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
        // specify the signature algorithm. In our case, "SHA256withRSA".
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signatureBytes = signature.sign();
        // Create a Base64 encoder and encode the specified byte array into a String using the Base64 encoding scheme.
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    private String hashEncryptedLicense(String encryptedLicense) {
        return encryptedLicense;
    }


}
