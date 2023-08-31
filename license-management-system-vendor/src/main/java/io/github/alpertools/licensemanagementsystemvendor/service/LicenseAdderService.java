package io.github.alpertools.licensemanagementsystemvendor.service;

import io.github.alpertools.licensemanagementsystemvendor.model.License;
import io.github.alpertools.licensemanagementsystemvendor.model.LicenseFingerprintSetterModel;
import io.github.alpertools.licensemanagementsystemvendor.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Slf4j
public class LicenseAdderService {

    @Autowired
    private LicenseDatabaseService licenseDatabaseService;

    @Value("${license.file.key.public}")
    private String PUBLIC_KEY_SAVE_FILE;

    public String addLicense(License license) throws NoSuchAlgorithmException, IOException {
        // if the given license does not exist in the database
        if (!licenseDatabaseService.isPresent(license)) {
            license.setLicenseFingerprint("?");
            license.setKeyPair(KeyService.generateKeyPair());
            licenseDatabaseService.addLicense(license);
            Utils.savePublicKeyToTxtFile(license.getKeyPair().getPublic(), PUBLIC_KEY_SAVE_FILE);
            return String.format("The license has been successfully added.\nPublic Key (Base 64): %s\nWarning: License fingerprint is not assigned!", KeyService.extractPublicKeyBase64(license.getKeyPair()));
        }
        return "The license already exists in the database. No Action!";
    }

    public String setLicenseFingerprint(LicenseFingerprintSetterModel licenseFingerprintSetterModel) {
        Optional<License> optionalLicense = licenseDatabaseService.getLicenseById(licenseFingerprintSetterModel.getId());
        if (optionalLicense.isPresent()) {
            License license = optionalLicense.get();
            license.setLicenseFingerprint(licenseFingerprintSetterModel.getLicenseFingerprint());
            licenseDatabaseService.addLicense(license);
            return "License Fingerprint has been saved successfully.\nUpdated License:\n" + license.toString();
        }
        throw new RuntimeException("No license found with the given id.");
    }



}

