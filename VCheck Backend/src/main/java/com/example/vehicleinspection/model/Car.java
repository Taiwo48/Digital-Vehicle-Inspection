package com.example.vehicleinspection.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String licensePlate;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    private Integer year;

    @Column(nullable = false)
    private String insuranceProvider;

    private String insurancePolicyNumber;
    private LocalDate insuranceExpiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private VehicleOwner owner;

    private LocalDate lastInspectionDate;
    private String lastInspectionStatus;
}