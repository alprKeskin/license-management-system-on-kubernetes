package io.github.alpertools.licensemanagementsystemcustomer.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInformation {

	private Long id;
	private String licenseKey;
	private LocalDate expirationDate;
	private String customerName;

	@Override
	public String toString() {
		return String.format(
				"ID: %s\n" +
				"License Key: %s\n" +
				"Expiration Date: %s\n" +
				"Customer Name: %s",
				id,
				licenseKey,
				expirationDate,
				customerName
		);
	}

}
