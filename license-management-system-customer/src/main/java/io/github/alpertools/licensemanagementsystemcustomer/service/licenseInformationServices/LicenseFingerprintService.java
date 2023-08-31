package io.github.alpertools.licensemanagementsystemcustomer.service.licenseInformationServices;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.github.alpertools.licensemanagementsystemcustomer.model.ClusterMetadata;
import io.github.alpertools.licensemanagementsystemcustomer.model.License;
import io.github.alpertools.licensemanagementsystemcustomer.model.UserInformation;
import io.github.alpertools.licensemanagementsystemcustomer.service.KeyService;
import io.github.alpertools.licensemanagementsystemcustomer.service.SecretsService;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Secret;
import io.kubernetes.client.util.ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

@Service
@Slf4j
public class LicenseFingerprintService {

	@Value("${license.secret.public-key.secret-name}")
	private String PUBLIC_KEY_SECRET_NAME;
	@Value("${license.secret.public-key.secret-label}")
	private String PUBLIC_KEY_SECRET_LABEL;
	@Value("${license.secret.public-key.data.key}")
	private String PUBLIC_KEY_SECRET_DATA_KEY;

	@Autowired
	private KubernetesInformationService kubernetesInformationService;
	@Autowired
	private SecretsService secretsService;

	public String generateLicenseFingerprint(UserInformation userInformation) throws Exception {
		License license = getLicense(userInformation);
		String hashedLicense = hashLicense(license);
		String publicKeyBase64 = getPublicKeyBase64ForLicenseFingerprint();
		return encryptData(hashedLicense, publicKeyBase64);
	}

	private String encryptData(String data, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// Generate a consistent key from the input key using hashing (SHA-256)
		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
		byte[] keyBytes = Arrays.copyOf(sha256.digest(key.getBytes()), 16);
		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

		return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
	}

	private License getLicense(UserInformation userInformation) throws ApiException {
		ClusterMetadata clusterMetadata = kubernetesInformationService.pullKubernetesClusterMetadata();
		return new License(userInformation, clusterMetadata);
	}

	private String getPublicKeyBase64ForLicenseFingerprint() throws NoSuchAlgorithmException {
		if (secretsService.doesKubernetesSecretDataExist(PUBLIC_KEY_SECRET_NAME, PUBLIC_KEY_SECRET_DATA_KEY)) {
			log.info("Public Key exists in the kubernetes secret data!");
			return secretsService.getKubernetesSecretDataValue(PUBLIC_KEY_SECRET_NAME, PUBLIC_KEY_SECRET_DATA_KEY);
		}
		else {
			log.info("Public Key does not exist in the kubernetes secret data!");
			String publicKeyBase64 = convertPublicKeyToBase64(KeyService.generatePublicKey());
			secretsService.addKubernetesSecretData(PUBLIC_KEY_SECRET_DATA_KEY, publicKeyBase64, PUBLIC_KEY_SECRET_NAME, PUBLIC_KEY_SECRET_LABEL);
			return publicKeyBase64;
		}
	}

	private String convertPublicKeyToBase64(PublicKey publicKey) {
		byte[] publicKeyBytes = publicKey.getEncoded();
		return Base64.getEncoder().encodeToString(publicKeyBytes);
	}

	private String hashLicense(License license) {
		return license.toString();
	}

}
