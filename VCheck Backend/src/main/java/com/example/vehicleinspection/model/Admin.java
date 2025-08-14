package com.example.vehicleinspection.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String firstName;
    private String lastName;
    private String email;

    @Column(nullable = false)
    private String role;

    private boolean canManageClients;
    private boolean canManageOfficers;
    private boolean canViewAnalytics;
    private boolean canManageSystem;

    @Column(nullable = false)
    private String department;

    private String contactNumber;
    private LocalDateTime lastLogin;
}