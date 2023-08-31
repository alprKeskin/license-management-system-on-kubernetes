package io.github.alpertools.licensemanagementsystemcustomer.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClusterMetadata {
    private String clusterName;
    private String apiServerUrl;
    private String apiServerCertificateThumbprint;
    private String clusterUID;
    private String clusterCreationTimestamp;
    private String masterNodeMacAddress;

    @Override
    public String toString() {
        return String.format("Cluster Name: %s\n" +
                "API Server URL: %s\n" +
                "API Server Certificate Thumbprint: %s\n" +
                "Cluster UID: %s\n" +
                "Cluster Creation Timestamp: %s\n" +
                "Master Node MAC Address: %s",
                clusterName,
                apiServerUrl,
                apiServerCertificateThumbprint,
                clusterUID,
                clusterCreationTimestamp,
                masterNodeMacAddress
        );
    }
}
