package io.github.alpertools.licensemanagementsystemcustomer.service;

import io.github.alpertools.licensemanagementsystemcustomer.model.ClusterMetadata;
import io.github.alpertools.licensemanagementsystemcustomer.model.License;
import io.github.alpertools.licensemanagementsystemcustomer.model.LicenseValidationRequest;
import io.github.alpertools.licensemanagementsystemcustomer.model.UserInformation;
import io.github.alpertools.licensemanagementsystemcustomer.service.licenseInformationServices.KubernetesInformationService;
import io.github.alpertools.licensemanagementsystemcustomer.service.licenseInformationServices.LicenseFingerprintService;
import io.github.alpertools.licensemanagementsystemcustomer.service.publicKeyServices.LicensePublicKeyService;
import io.github.alpertools.licensemanagementsystemcustomer.service.startUpServices.DigitalSignatureService;
import io.kubernetes.client.openapi.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Service
@Slf4j
public class LicenseService {

    @Value("${license.secret.digital-signature.secret-name}")
    private String DIGITAL_SIGNATURE_SECRET_NAME;
    @Value("${license.secret.digital-signature.secret-label}")
    private String DIGITAL_SIGNATURE_SECRET_LABEL;
    @Value("${license.secret.digital-signature.data.key}")
    private String DIGITAL_SIGNATURE_SECRET_DATA_KEY;

    @Autowired
    private LicensePublicKeyService licensePublicKeyService;
    @Autowired
    private LicenseValidationService licenseValidationService;
    @Autowired
    private KubernetesInformationService kubernetesInformationService;
    @Autowired
    private LicenseFingerprintService licenseFingerprintService;
    @Autowired
    private DigitalSignatureService digitalSignatureService;
    @Autowired
    private SecretsService secretsService;
    @Autowired
    private DeploymentDeactivationService deploymentDeactivationService;

    public void setPublicKey(String publicKeyBase64) throws IOException {
        licensePublicKeyService.setPublicKey(publicKeyBase64);
    }

    public String getPublicKeyBase64() throws Exception {
        return licensePublicKeyService.getPublicKeyBase64();
    }

    public boolean validateLicense(LicenseValidationRequest licenseValidationRequest) throws Exception {
        License license = createLicenseInformation(licenseValidationRequest);
        boolean isDigitalSignatureInSecrets = digitalSignatureService.isDigitalSignatureSecretInSecrets();
        String digitalSignature = isDigitalSignatureInSecrets ? digitalSignatureService.getDigitalSignature() : licenseValidationRequest.getSignature();
        boolean isValidated = licenseValidationService.validateLicense(license, digitalSignature);
        if (isDigitalSignatureInSecrets) {
            if (isValidated) {
                log.info("No Action");
            }
            else {
                deleteDigitalSignatureSecret();
                deploymentDeactivationService.deleteDeployment();
            }
        }
        else {
            if (isValidated) {
                saveDigitalSignatureToSecrets(digitalSignature);
            }
            else {
                deploymentDeactivationService.deleteDeployment();
            }
        }
        return isValidated;
    }

    private void saveDigitalSignatureToSecrets(String digitalSignature) {
        secretsService.addKubernetesSecretData(DIGITAL_SIGNATURE_SECRET_DATA_KEY, digitalSignature, DIGITAL_SIGNATURE_SECRET_NAME, DIGITAL_SIGNATURE_SECRET_LABEL);
    }

    private void deleteDigitalSignatureSecret() {
        secretsService.deleteKubernetesSecret(DIGITAL_SIGNATURE_SECRET_NAME);
    }

    public String generateLicenseFingerprint(UserInformation userInformation) throws Exception {
        return licenseFingerprintService.generateLicenseFingerprint(userInformation);
    }

    private License createLicenseInformation(LicenseValidationRequest licenseValidationRequest) throws ApiException {
        UserInformation userInformation = licenseValidationRequest.getUserInformation();
        ClusterMetadata clusterMetadata = kubernetesInformationService.pullKubernetesClusterMetadata();
        return new License(userInformation, clusterMetadata);
    }


}
