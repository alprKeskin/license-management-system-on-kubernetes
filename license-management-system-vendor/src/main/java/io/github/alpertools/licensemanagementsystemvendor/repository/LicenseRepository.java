package io.github.alpertools.licensemanagementsystemvendor.repository;

import io.github.alpertools.licensemanagementsystemvendor.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {
    // finds license record in the database by the license key
    License findByLicenseKey(String licenseKey);
}
