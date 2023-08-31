package io.github.alpertools.licensemanagementsystemcustomer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class License {
    private UserInformation userInformation;
    private ClusterMetadata clusterMetadata;

    @Override
    public String toString() {
        return userInformation.toString() + "\n" + clusterMetadata.toString();
    }
}
