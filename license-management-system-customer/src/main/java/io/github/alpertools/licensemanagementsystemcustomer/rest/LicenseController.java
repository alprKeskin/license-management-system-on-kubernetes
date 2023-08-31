package io.github.alpertools.licensemanagementsystemcustomer.rest;

import io.github.alpertools.licensemanagementsystemcustomer.model.LicenseValidationRequest;
import io.github.alpertools.licensemanagementsystemcustomer.model.UserInformation;
import io.github.alpertools.licensemanagementsystemcustomer.service.LicenseService;
import io.github.alpertools.licensemanagementsystemcustomer.service.SecretsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/licensing")
@Slf4j
@CrossOrigin
public class LicenseController {
    private final LicenseService licenseService;
    @Autowired
    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @Autowired
    private SecretsService secretsService;

    @GetMapping("/server-control")
    public ResponseEntity<String> checkServerStatus() {
        log.info("Server Control Endpoint Requested");
        return ResponseEntity.ok("The license management system is up!");
    }

    /***
     * Sets the given public key for the license.
     * @param publicKeyBase64: Public key in the format of Base64
     * @return: Information message
     * @throws IOException: Exception thrown during writing to text file
     */
    @PostMapping("/set-public-key")
    public ResponseEntity<String> setPublicKey(@RequestBody String publicKeyBase64) throws IOException {
        log.info("/licensing/set-public-key endpoint has been requested.");
        licenseService.setPublicKey(publicKeyBase64);
        return ResponseEntity.ok("Public Key has been set for the license.");
    }

    @GetMapping("/get-public-key")
    public ResponseEntity<String> getPublicKey() throws Exception {
        log.info("/licensing/get-public-key endpoint has been requested.");
        return ResponseEntity.ok(licenseService.getPublicKeyBase64());
    }

    /***
     * This endpoint will be running in the software license management system.
     * Gets a license validation request having:
     * => License Data
     * => Digital Signature
     * Uses public key to decode the digital signature (which is encoded by hashed license data and private key in the vendor.)
     * After decoding the digital signature, it attains the hashed license data.
     * It compares the hashed license data it extracted from the digital signature and the license data provided by the customer in the license validation request.
     * If they match, returns true. (License is validated)
     * Else, returns false. (License is not validated)
     * @param request:
     *               => Long id: ID of the license
     *               => String licenseKey: License Key of the customer
     *               => LocalDate expirationDate: Expiration Date of the license
     *               => String customerName: Name of the customer
     *               => String signature: Signature provided by the vendor
     * @return: If the license is validated, returns true. Otherwise, returns false.
     * @throws Exception: Not important.
     */
    @PostMapping("/validate-license")
    public ResponseEntity<String> validateLicense(@RequestBody LicenseValidationRequest request) throws Exception {
        boolean isValidated = licenseService.validateLicense(request);
        return ResponseEntity.ok(isValidated ? "The license is valid." : "The license is invalid.");
    }

    @PostMapping("/get-license-fingerprint")
    public ResponseEntity<String> generateLicenseFingerprint(@RequestBody UserInformation userInformation) throws Exception {
        return ResponseEntity.ok(licenseService.generateLicenseFingerprint(userInformation));
    }

    // TEST
    @GetMapping("/delete-secret")
    public void deleteSecret() {
        secretsService.deleteKubernetesSecret("digital-signature-secret");
        secretsService.deleteKubernetesSecret("user-information-secret");
    }

    @GetMapping("/delete-public-key-secret")
    public void deletePublicKeySecret() {
        secretsService.deleteKubernetesSecret("public-key-secret");
    }

}
