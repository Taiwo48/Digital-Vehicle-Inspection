package com.example.vehicleinspection.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminDTO extends BaseDTO {

    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,20}$", message = "Username must be 3-20 characters long and contain only letters, numbers, dots, underscores, or hyphens")
    private String username;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Department is required")
    private String department;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Please provide a valid contact number")
    private String contactNumber;

    private boolean canManageClients;
    private boolean canManageOfficers;
    private boolean canViewAnalytics;
    private boolean canManageSystem;

    // Additional fields for response
    private LocalDateTime lastLogin;
    private int activeSessionCount;
    private String lastActionPerformed;
    private int managedClientsCount;
    private int managedOfficersCount;
}