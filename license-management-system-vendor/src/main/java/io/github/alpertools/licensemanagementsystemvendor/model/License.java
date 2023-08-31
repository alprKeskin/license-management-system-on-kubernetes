package io.github.alpertools.licensemanagementsystemvendor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.KeyPair;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class License {
    @Id
    private Long id;
    @Column(unique = true, nullable = false)
    private String licenseKey;
    @Column(nullable = false)
    private LocalDate expirationDate;
    @Column(nullable = false)
    private String customerName;
    @Lob
    @Column
    private String licenseFingerprint;
    @Lob
    @Column
    private KeyPair keyPair;

    @Override
    public String toString() {
        return String.format(
                "ID: %s\n" +
                "License Key: %s\n" +
                "Expiration Date %s\n" +
                "Customer Name: %s\n" +
                "License Fingerprint: %s",
                id,
                licenseKey,
                expirationDate,
                customerName,
                licenseFingerprint
        );
    }

}
