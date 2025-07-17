package com.example.vehicleinspection.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarDTO extends BaseDTO {

    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "License plate must contain only uppercase letters, numbers and hyphens")
    private String licensePlate;

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be after 1900")
    private Integer year;

    @NotBlank(message = "Insurance provider is required")
    private String insuranceProvider;

    @NotBlank(message = "Insurance policy number is required")
    private String insurancePolicyNumber;

    @NotNull(message = "Insurance expiry date is required")
    private LocalDate insuranceExpiryDate;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    // Additional fields for response
    private String ownerName;
    private LocalDate lastInspectionDate;
    private String lastInspectionStatus;
    private boolean insuranceValid;
    private boolean inspectionDue;
}