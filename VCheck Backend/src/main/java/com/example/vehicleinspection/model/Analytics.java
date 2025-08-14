package com.example.vehicleinspection.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "inspection_id")
    private InspectionBooking inspection;

    private LocalDateTime inspectionDate;
    private Long inspectionDuration; // in minutes

    private String inspectionType;
    private String vehicleCategory;
    
    @Column(columnDefinition = "TEXT")
    private String inspectionFindings;

    private boolean passedInspection;
    private int defectsFound;
    private String criticalIssues;

    // Dashboard metrics
    private double inspectionScore;
    private String performanceMetrics;
    
    @Column(columnDefinition = "TEXT")
    private String recommendations;

    // Reporting fields
    private String reportStatus;
    private LocalDateTime reportGeneratedAt;
    private String reportId;

    // Statistical data
    private double averageInspectionTime;
    private int totalInspectionsToday;
    private int passRate;
}