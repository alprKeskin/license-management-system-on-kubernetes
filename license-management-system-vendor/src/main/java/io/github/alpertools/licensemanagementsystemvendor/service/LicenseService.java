package io.github.alpertools.licensemanagementsystemvendor.service;

import io.github.alpertools.licensemanagementsystemvendor.model.License;
import io.github.alpertools.licensemanagementsystemvendor.model.LicenseFingerprintSetterModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.*;
import java.util.Optional;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Service
@Slf4j
public class LicenseService {

    @Autowired
    private LicenseAdderService licenseAdderService;
    @Autowired
    private LicenseDatabaseService licenseDatabaseService;
    @Autowired
    private DigitalSignatureService digitalSignatureService;


    public String addLicense(License license) throws NoSuchAlgorithmException, IOException {
        return licenseAdderService.addLicense(license);
    }

    public String setLicenseFingerprint(LicenseFingerprintSetterModel licenseFingerprintSetterModel) {
        return licenseAdderService.setLicenseFingerprint(licenseFingerprintSetterModel);
    }

    public String generateDigitalSignature(Long licenseId) throws Exception {
        return digitalSignatureService.generateDigitalSignature(licenseId);
    }

    public String getPublicKeyBase64(Long licenseId) {
        Optional<License> license = licenseDatabaseService.getLicenseById(licenseId);
        if (license.isPresent()) {
            return KeyService.extractPublicKeyBase64(license.get().getKeyPair());
        }
        return "No license found with the given license id.";
    }

}
