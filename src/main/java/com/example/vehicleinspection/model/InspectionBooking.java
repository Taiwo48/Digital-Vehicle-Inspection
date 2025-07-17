package com.example.vehicleinspection.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class InspectionBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_owner_id", nullable = false)
    private VehicleOwner vehicleOwner;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "inspection_officer_id")
    private InspectionOfficer inspectionOfficer;

    private LocalDateTime scheduledDateTime;
    private LocalDateTime completedDateTime;

    @Enumerated(EnumType.STRING)
    private InspectionStatus status;

    private String inspectionType;
    private String notes;
    private String result;
    private String recommendations;

    public enum InspectionStatus {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        RESCHEDULED
    }
}