package com.example.vehicleinspection.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class VehicleOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String driverLicense;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "vehicleOwner", cascade = CascadeType.ALL)
    private List<InspectionBooking> inspectionBookings = new ArrayList<>();
}