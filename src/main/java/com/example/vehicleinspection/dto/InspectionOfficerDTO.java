package com.example.vehicleinspection.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class InspectionOfficerDTO extends BaseDTO {

    @NotBlank(message = "Badge number is required")
    private String badgeNumber;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Department is required")
    private String department;

    private List<String> inspectionMethods;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Years of experience cannot be negative")
    private int yearsOfExperience;

    private boolean isAvailable;

    // Additional fields for response
    private int totalInspectionsCompleted;
    private double averageInspectionRating;
    private int currentQueueSize;
    private String nextAvailableSlot;
    private List<String> certifications;
    private int inspectionsToday;
}