package io.github.alpertools.licensemanagementsystemvendor.service;

import io.github.alpertools.licensemanagementsystemvendor.model.License;
import io.github.alpertools.licensemanagementsystemvendor.repository.LicenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LicenseDatabaseService {
    // this is for the database actions
    @Autowired
    private LicenseRepository licenseRepository;

    public boolean isPresent(License license) {
        return licenseRepository.existsById(license.getId());
    }

    public boolean isPresent(Long licenseId) {return licenseRepository.existsById(licenseId);}

    public Optional<License> getLicenseById(Long licenseId) {
        return licenseRepository.findById(licenseId);
    }

    public void addLicense(License license) {
        licenseRepository.save(license);
    }

    public void printDatabase() {
        List<License> licenses = licenseRepository.findAll();
        for (int i = 0; i < licenses.size(); i++) {
            System.out.println(licenses.get(i).toString());
        }
        System.out.println("-----------------------------------------------------------------");
    }

}
