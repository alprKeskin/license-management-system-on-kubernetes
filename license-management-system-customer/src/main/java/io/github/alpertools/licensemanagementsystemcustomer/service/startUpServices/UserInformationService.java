package io.github.alpertools.licensemanagementsystemcustomer.service.startUpServices;

import io.github.alpertools.licensemanagementsystemcustomer.model.UserInformation;
import io.github.alpertools.licensemanagementsystemcustomer.service.SecretsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Scanner;

@Service
@Slf4j
public class UserInformationService {

    @Value("${license.secret.user-information.secret-name}")
    private String USER_INFORMATION_SECRET_NAME;
    @Value("${license.secret.user-information.secret-label}")
    private String USER_INFORMATION_SECRET_LABEL;
    @Value("${license.secret.user-information.data.key.id}")
    private String USER_INFORMATION_SECRET_DATA_KEY_ID;
    @Value("${license.secret.user-information.data.key.license-key}")
    private String USER_INFORMATION_SECRET_DATA_KEY_LICENSE_KEY;
    @Value("${license.secret.user-information.data.key.expiration-date}")
    private String USER_INFORMATION_SECRET_DATA_KEY_EXPIRATION_DATE;
    @Value("${license.secret.user-information.data.key.customer-name}")
    private String USER_INFORMATION_SECRET_DATA_KEY_CUSTOMER_NAME;

    @Autowired
    private SecretsService secretsService;

    public UserInformation getUserInformation() {
        UserInformation userInformation = new UserInformation();
        if (isUserInformationSecretInSecrets()) return getUserInformationFromSecrets();
        else return getUserInformationFromUser();
    }

    private boolean isUserInformationSecretInSecrets() {
        return secretsService.isSecretInSecrets(USER_INFORMATION_SECRET_NAME);
    }

    private UserInformation getUserInformationFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your id: ");
        Long id = scanner.nextLong();
        System.out.print("Enter your license key: ");
        String licenseKey = scanner.nextLine();
        System.out.print("Enter your license expiration date: ");
        LocalDate expirationDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter your customer name: ");
        String customerName = scanner.nextLine();
        return new UserInformation(id, licenseKey, expirationDate, customerName);
    }

    private UserInformation getUserInformationFromSecrets() {
        Long id = Long.valueOf(secretsService.getKubernetesSecretDataValue(USER_INFORMATION_SECRET_NAME, USER_INFORMATION_SECRET_DATA_KEY_ID));
        String licenseKey = secretsService.getKubernetesSecretDataValue(USER_INFORMATION_SECRET_NAME, USER_INFORMATION_SECRET_DATA_KEY_LICENSE_KEY);
        LocalDate expirationDate = LocalDate.parse(secretsService.getKubernetesSecretDataValue(USER_INFORMATION_SECRET_NAME, USER_INFORMATION_SECRET_DATA_KEY_EXPIRATION_DATE));
        String customerName = secretsService.getKubernetesSecretDataValue(USER_INFORMATION_SECRET_NAME, USER_INFORMATION_SECRET_DATA_KEY_CUSTOMER_NAME);
        return new UserInformation(id, licenseKey, expirationDate, customerName);
    }

}
