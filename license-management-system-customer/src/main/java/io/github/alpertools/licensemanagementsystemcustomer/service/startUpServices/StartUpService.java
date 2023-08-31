package io.github.alpertools.licensemanagementsystemcustomer.service.startUpServices;

import io.github.alpertools.licensemanagementsystemcustomer.model.LicenseValidationRequest;
import io.github.alpertools.licensemanagementsystemcustomer.model.UserInformation;
import io.github.alpertools.licensemanagementsystemcustomer.service.DeploymentDeactivationService;
import io.github.alpertools.licensemanagementsystemcustomer.service.LicenseService;
import io.github.alpertools.licensemanagementsystemcustomer.service.SecretsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartUpService implements ApplicationRunner {

    @Value("${license.secret.digital-signature.secret-name}")
    private String DIGITAL_SIGNATURE_SECRET_NAME;
    @Value("${license.secret.digital-signature.secret-label}")
    private String DIGITAL_SIGNATURE_SECRET_LABEL;
    @Value("${license.secret.digital-signature.data.key}")
    private String DIGITAL_SIGNATURE_SECRET_DATA_KEY;

    @Autowired
    private UserInformationService userInformationService;
    @Autowired
    private DigitalSignatureService digitalSignatureService;
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private DeploymentDeactivationService deploymentDeactivationService;
    @Autowired
    private SecretsService secretsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("Application started. Running startup tasks...");
        /*
        UserInformation userInformation = userInformationService.getUserInformation();
        String licenseFingerprint = licenseService.generateLicenseFingerprint(userInformation);
        System.out.println("Your license fingerprint: " + licenseFingerprint);
        System.out.println("Send this fingerprint to your license manager foundation.");
        boolean isDigitalSignatureInSecrets = digitalSignatureService.isDigitalSignatureSecretInSecrets();
        String digitalSignature = digitalSignatureService.getDigitalSignature();
        boolean validationResponse = licenseService.validateLicense(new LicenseValidationRequest(userInformation, digitalSignature));
        System.out.println(validationResponse);

        if (isDigitalSignatureInSecrets) {
            if (validationResponse) {
                return;
            }
            else {
                deleteDigitalSignatureSecret();
                deploymentDeactivationService.deleteDeployment();
                return;
            }
        }
        else {
            if (validationResponse) {
                saveDigitalSignatureToSecrets(digitalSignature);
                return;
            }
            else {
                deploymentDeactivationService.deleteDeployment();
                return;
            }
        }
        */
    }

    private void saveDigitalSignatureToSecrets(String digitalSignature) {
        secretsService.addKubernetesSecretData(DIGITAL_SIGNATURE_SECRET_DATA_KEY, digitalSignature, DIGITAL_SIGNATURE_SECRET_NAME, DIGITAL_SIGNATURE_SECRET_LABEL);
    }

    private void deleteDigitalSignatureSecret() {
        secretsService.deleteKubernetesSecret(DIGITAL_SIGNATURE_SECRET_NAME);
    }

}
