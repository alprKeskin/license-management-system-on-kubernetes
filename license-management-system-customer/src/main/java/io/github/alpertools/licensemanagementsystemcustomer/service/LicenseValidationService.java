package io.github.alpertools.licensemanagementsystemcustomer.service;

import io.github.alpertools.licensemanagementsystemcustomer.model.License;
import io.github.alpertools.licensemanagementsystemcustomer.service.licenseInformationServices.LicenseFingerprintService;
import io.github.alpertools.licensemanagementsystemcustomer.service.publicKeyServices.LicensePublicKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.*;
import java.time.LocalDate;
import java.util.Base64;

@Service
@Slf4j
public class LicenseValidationService {

    @Autowired
    private LicensePublicKeyService licensePublicKeyService;
    @Autowired
    private LicenseFingerprintService licenseFingerprintService;

    public boolean validateLicense(License license, String signature) throws Exception {
        return ( (verifyDigitalSignature(license, signature)) && (!isExpired(license.getUserInformation().getExpirationDate())));
    }

    private boolean verifyDigitalSignature(License license, String signature) throws Exception {
        String licenseFingerprint = licenseFingerprintService.generateLicenseFingerprint(license.getUserInformation());
        PublicKey publicKey = licensePublicKeyService.getPublicKey();
        return verifyData(licenseFingerprint, signature, publicKey);
    }

    private boolean verifyData(String data, String signature, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return sig.verify(signatureBytes);
    }

    private boolean isExpired(LocalDate expirationDate) {
        return expirationDate.isBefore(LocalDate.now());
    }

    private String hashLicense(License license) {
        return license.toString();
    }

}
