package io.github.alpertools.licensemanagementsystemcustomer.service;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeploymentDeactivationService {

    @Value("${license.deployment.name}")
    private String DEPLOYMENT_NAME;
    @Value("${license.secret.digital-signature.secret-name}")
    private String DIGITAL_SIGNATURE_SECRET_NAME;
    @Value("${license.secret.digital-signature.secret-label}")
    private String DIGITAL_SIGNATURE_SECRET_LABEL;
    @Value("${license.secret.digital-signature.data.key}")
    private String DIGITAL_SIGNATURE_SECRET_DATA_KEY;

    @Autowired
    private SecretsService secretsService;

    private final KubernetesClient kubernetesClient;

    @Autowired
    DeploymentDeactivationService() {
        kubernetesClient = new KubernetesClientBuilder().build();
    }

    public void deleteDeployment() {
        try {
            String namespace = retrievePodNamespace();
            kubernetesClient.apps().deployments().inNamespace(namespace).withName(DEPLOYMENT_NAME).delete();
            kubernetesClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String retrievePodNamespace() {
        String podName = retrievePodName();
        // Fetch the pod information
        Pod pod = kubernetesClient.pods().withName(podName).get();
        if (pod != null) return pod.getMetadata().getNamespace();
        return null;
    }

    private String retrievePodName() {
        try {
            return System.getenv("HOSTNAME");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
