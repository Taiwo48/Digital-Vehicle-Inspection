package com.example.vehicleinspection.dto;

import com.example.vehicleinspection.model.InspectionBooking.InspectionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class InspectionBookingDTO extends BaseDTO {

    @NotNull(message = "Vehicle owner ID is required")
    private Long vehicleOwnerId;

    @NotNull(message = "Car ID is required")
    private Long carId;

    private Long inspectionOfficerId;

    @NotNull(message = "Scheduled date time is required")
    @Future(message = "Scheduled date time must be in the future")
    private LocalDateTime scheduledDateTime;

    private LocalDateTime completedDateTime;

    @NotNull(message = "Inspection type is required")
    private String inspectionType;

    private InspectionStatus status;
    private String notes;
    private String result;
    private String recommendations;

    // Additional fields for response
    private String vehicleOwnerName;
    private String carLicensePlate;
    private String officerName;
    private String officerBadgeNumber;
    private Long estimatedDuration;
    private boolean isRescheduled;
    private int rescheduleCount;
    private String previousInspectionResult;
    private LocalDateTime originalScheduledDateTime;
}