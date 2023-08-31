package io.github.alpertools.licensemanagementsystemcustomer.service;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
@Slf4j
public class SecretsService {

    private final KubernetesClient client;

    @Autowired
    SecretsService() {
        client = new KubernetesClientBuilder().build();
    }

    public boolean doesKubernetesSecretDataExist(String secretName, String secretDataKey) {
        Secret secret = getKubernetesSecret(secretName);
        if (secret == null) return false;
        return (secret.getData().containsKey(secretDataKey));
    }

    public String getKubernetesSecretDataValue(String secretName, String secretDataKey) {
        Secret secret = getKubernetesSecret(secretName);

        if (secret != null) {
            byte[] dataBytes = Base64.getDecoder().decode(secret.getData().get(secretDataKey));
            return new String(dataBytes);
        }
        throw new RuntimeException("Kubernetes Secret cannot be found!");
    }

    public void addKubernetesSecretData(String secretDataKey, String secretDataValue, String secretName, String secretLabel) {
        if (doesKubernetesSecretExist(secretName)) {
            if (doesKubernetesSecretDataExist(secretName, secretDataKey)) throw new RuntimeException("The secret data already exists.");
            appendSecretDataToExistingKubernetesSecret(secretDataKey, secretDataValue, secretName);
        }
        else {
            Secret secret = createSecret(secretName, secretLabel);
            addDataToSecret(secret, secretDataKey, secretDataValue);
            addKubernetesSecret(secret);
        }
    }

    public void deleteKubernetesSecret(String secretName) {
        client.secrets().inNamespace(client.getNamespace()).withName(secretName).delete();
    }

    private Secret getKubernetesSecret(String secretName) {
        return client
                .secrets()
                .inNamespace(client.getNamespace())
                .withName(secretName)
                .get();
    }

    private void addKubernetesSecret(Secret secret) {
        client.secrets()
                .inNamespace(client.getNamespace())
                .create(secret);
    }

    private boolean doesKubernetesSecretExist(String secretName) {
        return (getKubernetesSecret(secretName) != null);
    }

    private Secret createSecret(String secretName, String secretLabel) {
        return new SecretBuilder()
                .withNewMetadata()
                .withName(secretName)
                .addToLabels("app", secretLabel)
                .endMetadata()
                .build();
    }

    private void addDataToSecret(Secret secret, String secretDataKey, String secretDataValue) {
        Map<String, String> secretData = secret.getData();
        secretData.put(secretDataKey, Base64.getEncoder().encodeToString(secretDataValue.getBytes()));
        secret.setData(secretData);
    }

    private void appendSecretDataToExistingKubernetesSecret(String secretDataKey, String secretDataValue, String secretName) {
        Secret secret = getKubernetesSecret(secretName);
        addDataToSecret(secret, secretDataKey, secretDataValue);
        client.secrets()
                .inNamespace(client.getNamespace())
                .replace(secret);
    }


    /*
    public void addSecretData(String secretDataKey, String secretDataValue, String secretName, String secretLabel) {
        Secret secret = new SecretBuilder()
                .withNewMetadata().
                withName(secretName)
                .addToLabels("app", secretLabel)
                .endMetadata()
                .addToData(secretDataKey, Base64.getEncoder().encodeToString(secretDataValue.getBytes()))
                .build();

        Secret existingSecret = client
                .secrets()
                .inNamespace(client.getNamespace())
                .withName("public-key-secret")
                .get();

        if (existingSecret != null) {
            client.secrets()
                    .inNamespace(client.getNamespace())
                    .withName("public-key-secret").replace(secret);
        }
        else {
            client.secrets()
                    .inNamespace(client.getNamespace())
                    .create(secret);
        }
    }
    */



}
