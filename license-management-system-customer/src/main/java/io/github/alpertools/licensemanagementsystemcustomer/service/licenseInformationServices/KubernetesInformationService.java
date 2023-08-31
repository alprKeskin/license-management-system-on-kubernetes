package io.github.alpertools.licensemanagementsystemcustomer.service.licenseInformationServices;

import io.github.alpertools.licensemanagementsystemcustomer.model.ClusterMetadata;
import io.github.alpertools.licensemanagementsystemcustomer.service.licenseInformationServices.ClusterMetadataProviderService;
import io.kubernetes.client.openapi.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KubernetesInformationService {

    @Autowired
    private ClusterMetadataProviderService clusterMetadataProviderService;

    public ClusterMetadata pullKubernetesClusterMetadata() throws ApiException {
        return new ClusterMetadata(
                clusterMetadataProviderService.retrieveClusterName(),
                clusterMetadataProviderService.retrieveApiServerUrl(),
                clusterMetadataProviderService.retrieveApiServerCertificateThumbprint(),
                clusterMetadataProviderService.retrieveClusterUID(),
                clusterMetadataProviderService.retrieveClusterCreationTimestamp(),
                clusterMetadataProviderService.retrieveMasterNodesMacAddresses()
        );
    }

}
