package com.example.vehicleinspection.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class InspectionOfficer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String badgeNumber;

    private String firstName;
    private String lastName;
    private String department;

    @ElementCollection
    @CollectionTable(name = "inspection_methods", 
                    joinColumns = @JoinColumn(name = "officer_id"))
    private List<String> inspectionMethods = new ArrayList<>();

    @OneToMany(mappedBy = "inspectionOfficer")
    private List<InspectionBooking> inspections = new ArrayList<>();

    private boolean isAvailable;
    private String specialization;
    private int yearsOfExperience;
}