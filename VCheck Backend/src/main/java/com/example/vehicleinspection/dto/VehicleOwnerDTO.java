package com.example.vehicleinspection.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleOwnerDTO extends BaseDTO {
    
    @NotBlank(message = "Driver license number is required")
    private String driverLicense;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Please provide a valid phone number")
    private String phone;

    private List<Long> vehicleIds;
    private List<Long> inspectionBookingIds;

    // Additional fields for response
    private int totalVehicles;
    private int pendingInspections;
    private String lastInspectionDate;
}