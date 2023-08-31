package io.github.alpertools.licensemanagementsystemcustomer.service.licenseInformationServices;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeAddress;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ClusterMetadataProviderService {

    @Value("${license.label.node.master}")
    private String MASTER_NODE_LABEL;

    private final KubernetesClient kubernetesClient;

    @Autowired
    ClusterMetadataProviderService() {
        kubernetesClient = new KubernetesClientBuilder().build();
    }

    // output: Default (SUCCESS)
    public String retrieveClusterName() {
        return kubernetesClient.getNamespace();
    }

    // output: https://10.96.0.1:443/ (SUCCESS)
    // In the presence of master nodes more than one, it fails.
    // We can use a combination of master urls of all master nodes.
    // But this time, when a master node is down; or when a new master node is added to the cluster,
    // It will be a problem.
    // RESULT: UNFEASIBLE
    // BUT WE HAVE AN IDEA (OR)
    public String retrieveApiServerUrl() {
        Config config = kubernetesClient.getConfiguration();
        return config.getMasterUrl();
    }

    // output: fa3ee7fb35b6b44fa2b166738607b4aa1fae7157618a206d193470720b9bf263 (SUCCESS)
    public String retrieveApiServerCertificateThumbprint() {
        String apiCertificateThumbprint = "Nothing";
        try {
            // Load API server certificate from Kubernetes configuration (replace with your file path)
            // This is container file system (not the node file system)
            String certificateFilePath = "/var/run/secrets/kubernetes.io/serviceaccount/ca.crt";
            FileInputStream fis = new FileInputStream(certificateFilePath);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(fis);

            // Calculate thumbprint (SHA-256 hash) of the certificate
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] thumbprintBytes = digest.digest(certificate.getEncoded());

            // Convert thumbprint to hexadecimal representation
            StringBuilder thumbprintHex = new StringBuilder();
            for (byte b : thumbprintBytes) {
                thumbprintHex.append(String.format("%02x", b));
            }

            apiCertificateThumbprint = thumbprintHex.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCertificateThumbprint;
    }

    // output: minikube (SUCCESSFUL)
    public String retrieveNodeNames() {
        List<String> nodeNames = new ArrayList<String>();
        List<Node> nodes = getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            nodeNames.add(nodes.get(i).getMetadata().getName());
        }
        return nodeNames.toString();
    }

    // output: dca22c0c-ebed-4332-ba6e-6dc15f95252f (SUCCESSFUL)
    public String retrieveClusterUID() {
        return getFirstNode().getMetadata().getUid();
    }

    // output: 2023-07-26T11:05:50Z (SUCCESSFUL)
    public String retrieveClusterCreationTimestamp() {
        return getFirstNode().getMetadata().getCreationTimestamp();
    }

    public String retrieveMasterNodesMacAddresses() {
        List<String> macAddresses = new ArrayList<String>();
        List<Node> masterNodeList = getMasterNodes();
        for (int i = 0; i < masterNodeList.size(); i++) {
            Node currentNode = masterNodeList.get(i);
            for (NodeAddress address : currentNode.getStatus().getAddresses()) {
                if ("InternalIP".equals(address.getType())) {
                    macAddresses.add(address.getAddress());
                }
            }
        }
        return macAddresses.toString();
    }

    private List<Node> getMasterNodes() {
        List<Node> masterNodeList = new ArrayList<Node>();
        List<Node> nodeList = getNodes();
        for (int i = 0; i < nodeList.size(); i++) {
            if (isMasterNode(nodeList.get(i))) {
                masterNodeList.add(nodeList.get(i));
            }
        }
        return masterNodeList;
    }

    private boolean isMasterNode(Node node) {
        return node.getMetadata().getLabels().containsKey(MASTER_NODE_LABEL);
    }

    private Node getFirstNode() {
        return getNodes().get(0);
    }

    private List<Node> getNodes() {
        NodeList nodeList = kubernetesClient.nodes().list();
        if (nodeList.getItems() != null && !nodeList.getItems().isEmpty()) {
            return nodeList.getItems();
        }
        throw new RuntimeException("No nodes found in the cluster!");
    }

}
