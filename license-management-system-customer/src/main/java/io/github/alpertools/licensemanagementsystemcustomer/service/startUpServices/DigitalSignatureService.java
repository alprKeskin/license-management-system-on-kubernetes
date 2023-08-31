package io.github.alpertools.licensemanagementsystemcustomer.service.startUpServices;

import io.github.alpertools.licensemanagementsystemcustomer.service.SecretsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
@Slf4j
public class DigitalSignatureService {

    @Value("${license.secret.digital-signature.secret-name}")
    private String DIGITAL_SIGNATURE_SECRET_NAME;
    @Value("${license.secret.digital-signature.data.key}")
    private String DIGITAL_SIGNATURE_SECRET_DATA_KEY;

    @Autowired
    private SecretsService secretsService;

    public String getDigitalSignature() {
        return isDigitalSignatureSecretInSecrets() ? getDigitalSignatureFromSecrets() : getDigitalSignatureFromUser();
    }

    public boolean isDigitalSignatureSecretInSecrets() {
        return secretsService.isSecretInSecrets(DIGITAL_SIGNATURE_SECRET_NAME);
    }

    private String getDigitalSignatureFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your digital signature: ");
        return scanner.nextLine();
    }

    private String getDigitalSignatureFromSecrets() {
        return secretsService.getKubernetesSecretDataValue(DIGITAL_SIGNATURE_SECRET_NAME, DIGITAL_SIGNATURE_SECRET_DATA_KEY);
    }

}
