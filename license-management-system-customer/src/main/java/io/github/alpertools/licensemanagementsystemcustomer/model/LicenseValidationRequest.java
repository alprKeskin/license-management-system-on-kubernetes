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
public class LicenseValidationRequest {
    private UserInformation userInformation;
    private String signature;

    @Override
    public String toString() {
        return userInformation.toString() + String.format("\nSignature: %s", signature);
    }

}
