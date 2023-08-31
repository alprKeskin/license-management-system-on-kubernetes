package io.github.alpertools.licensemanagementsystemvendor.rest;

import io.github.alpertools.licensemanagementsystemvendor.model.License;
import io.github.alpertools.licensemanagementsystemvendor.model.LicenseFingerprintSetterModel;
import io.github.alpertools.licensemanagementsystemvendor.service.LicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/licensing")
@Slf4j
public class LicenseController {

    private final LicenseService licenseService;
    @Autowired
    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/server-control")
    public ResponseEntity<String> checkServerStatus() {
        return ResponseEntity.ok("The license management system is up!");
    }

    /***
     * If the license already exists in the database, it returns warning with no action.
     * Otherwise, assigns a key pair for the given license, and adds the license to the database.
     * @param license: License to be assigned a key pair, and then added to the database.
     * @return: If the license already exists in the database, it returns warning with no action.
     * Otherwise, returns true.
     * @throws Exception: Not important.
     */
    @PostMapping("/add-license")
    public ResponseEntity<String> addLicense(@RequestBody License license) throws Exception {
        log.info(String.format("License:\n%s\n%s\n%s\n%s",license.getId(), license.getLicenseKey(), license.getExpirationDate(), license.getCustomerName()));
        return ResponseEntity.ok(licenseService.addLicense(license));
    }

    @PostMapping("/set-license-fingerprint")
    public ResponseEntity<String> setLicenseFingerprint(@RequestBody LicenseFingerprintSetterModel licenseFingerprintSetterModel) {
        return ResponseEntity.ok(licenseService.setLicenseFingerprint(licenseFingerprintSetterModel));
    }

    /***
     * Gets the license from the database (if no record found, throws exception).
     * Hashes the license.
     * Creates a signature using the hashed license data and private key.
     * Private and Public key of the license is already in the database. No more key pair generation.
     * This signature can be decoded, and the hashed license data can only be found by using the public key.
     * @param licenseId: License id of the license to be signed
     * @return: Digital Signature
     * @throws Exception: Not important.
     */
    @PostMapping("/generate-signature")
    public ResponseEntity<String> generateSignature(@RequestBody Long licenseId) throws Exception {
        return ResponseEntity.ok(licenseService.generateDigitalSignature(licenseId));
    }

    /***
     * Gets public key of the license with the given license id.
     * Public key is in the database.
     * @param licenseId: License id of the license that is requested to get public key.
     * @return: Public key of the given license.
     * @throws Exception: Not important.
     */
    @PostMapping("/get-public-key")
    public ResponseEntity<String> getPublicKey(@RequestBody Long licenseId) throws Exception {
        return ResponseEntity.ok(licenseService.getPublicKeyBase64(licenseId));
    }


}
