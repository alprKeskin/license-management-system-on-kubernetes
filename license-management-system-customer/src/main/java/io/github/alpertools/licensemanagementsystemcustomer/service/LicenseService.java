package io.github.alpertools.licensemanagementsystemcustomer.service;

import io.github.alpertools.licensemanagementsystemcustomer.model.ClusterMetadata;
import io.github.alpertools.licensemanagementsystemcustomer.model.License;
import io.github.alpertools.licensemanagementsystemcustomer.model.LicenseValidationRequest;
import io.github.alpertools.licensemanagementsystemcustomer.model.UserInformation;
import io.github.alpertools.licensemanagementsystemcustomer.service.licenseInformationServices.KubernetesInformationService;
import io.github.alpertools.licensemanagementsystemcustomer.service.licenseInformationServices.LicenseFingerprintService;
import io.github.alpertools.licensemanagementsystemcustomer.service.publicKeyServices.LicensePublicKeyService;
import io.kubernetes.client.openapi.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Service
@Slf4j
public class LicenseService {

    @Autowired
    private LicensePublicKeyService licensePublicKeyService;

    @Autowired
    private LicenseValidationService licenseValidationService;

    @Autowired
    private KubernetesInformationService kubernetesInformationService;

    @Autowired
    private LicenseFingerprintService licenseFingerprintService;

    public void setPublicKey(String publicKeyBase64) throws IOException {
        licensePublicKeyService.setPublicKey(publicKeyBase64);
    }

    public String getPublicKeyBase64() throws Exception {
        return licensePublicKeyService.getPublicKeyBase64();
    }

    public boolean validateLicense(LicenseValidationRequest licenseValidationRequest) throws Exception {
        License license = createLicenseInformation(licenseValidationRequest);
        return licenseValidationService.validateLicense(license, licenseValidationRequest.getSignature());
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
