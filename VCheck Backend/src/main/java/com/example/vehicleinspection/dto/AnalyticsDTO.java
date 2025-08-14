package com.example.vehicleinspection.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class AnalyticsDTO extends BaseDTO {

    @NotNull(message = "Inspection ID is required")
    private Long inspectionId;

    private LocalDateTime inspectionDate;
    private Long inspectionDuration;
    private String inspectionType;
    private String vehicleCategory;
    private String inspectionFindings;
    private boolean passedInspection;
    private int defectsFound;
    private String criticalIssues;

    // Dashboard metrics
    private double inspectionScore;
    private String performanceMetrics;
    private String recommendations;

    // Report details
    private String reportStatus;
    private LocalDateTime reportGeneratedAt;
    private String reportId;

    // Statistical data
    private double averageInspectionTime;
    private int totalInspectionsToday;
    private int passRate;

    // Additional analytics fields
    private double safetyScore;
    private double complianceScore;
    private int repeatInspectionCount;
    private String trendAnalysis;
    private String seasonalPatterns;
    private double yearOverYearGrowth;
    private int customerSatisfactionScore;
}