package io.github.alpertools.licensemanagementsystemcustomer.service;

import io.github.alpertools.licensemanagementsystemcustomer.model.License;
import io.github.alpertools.licensemanagementsystemcustomer.service.licenseInformationServices.LicenseFingerprintService;
import io.github.alpertools.licensemanagementsystemcustomer.service.publicKeyServices.LicensePublicKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.*;
import java.time.LocalDate;
import java.util.Base64;

@Service
@Slf4j
public class LicenseValidationService {

    @Value("${license.secret.digital-signature.secret-name}")
    private String DIGITAL_SIGNATURE_SECRET_NAME;
    @Value("${license.secret.digital-signature.secret-label}")
    private String DIGITAL_SIGNATURE_SECRET_LABEL;
    @Value("${license.secret.digital-signature.data.key}")
    private String DIGITAL_SIGNATURE_SECRET_DATA_KEY;

    @Autowired
    private LicensePublicKeyService licensePublicKeyService;
    @Autowired
    private LicenseFingerprintService licenseFingerprintService;
    @Autowired
    private SecretsService secretsService;

    private void saveDigitalSignatureToSecrets(String digitalSignature) {
        secretsService.addKubernetesSecretData(DIGITAL_SIGNATURE_SECRET_DATA_KEY, digitalSignature, DIGITAL_SIGNATURE_SECRET_NAME, DIGITAL_SIGNATURE_SECRET_LABEL);
    }

    public boolean validateLicense(License license, String signature) throws Exception {
        return ((verifyDigitalSignature(license, signature)) && (!isExpired(license.getUserInformation().getExpirationDate())));
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
