package com.example.vehicleinspection.service;

import com.example.vehicleinspection.model.Analytics;
import com.example.vehicleinspection.dto.AnalyticsDTO;
import com.example.vehicleinspection.model.InspectionBooking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AnalyticsService extends BaseService<Analytics, Long> {

    // Basic CRUD operations
    AnalyticsDTO createAnalytics(AnalyticsDTO analyticsDTO);
    AnalyticsDTO updateAnalytics(Long id, AnalyticsDTO analyticsDTO);
    void deleteAnalytics(Long id);

    // Find operations
    List<AnalyticsDTO> findByInspectionDate(LocalDateTime date);
    List<AnalyticsDTO> findByDateRange(LocalDateTime start, LocalDateTime end);
    List<AnalyticsDTO> findByPassedStatus(boolean passed);
    List<AnalyticsDTO> findByInspectionType(String type);
    List<AnalyticsDTO> findByInspectionBooking(Long bookingId);

    // Statistical operations
    double getAverageInspectionDuration(LocalDateTime start, LocalDateTime end);
    long getPassedInspectionsCount(LocalDateTime start, LocalDateTime end);
    long getFailedInspectionsCount(LocalDateTime start, LocalDateTime end);
    double getPassRate(LocalDateTime start, LocalDateTime end);
    Map<String, Long> getInspectionsByVehicleCategory(LocalDateTime start, LocalDateTime end);

    // Performance metrics
    double getAverageInspectionScore(Long officerId);
    Map<String, Double> getOfficerPerformanceMetrics(Long officerId);
    List<String> getTopDefects(LocalDateTime start, LocalDateTime end, int limit);

    // Trend analysis
    Map<String, Long> getMonthlyInspectionTrends(int year);
    Map<String, Double> getSeasonalPatterns(int year);
    double getYearOverYearGrowth(int year);

    // Safety and compliance
    double getOverallSafetyScore(LocalDateTime start, LocalDateTime end);
    double getComplianceRate(LocalDateTime start, LocalDateTime end);
    List<String> getCriticalIssuesByFrequency(LocalDateTime start, LocalDateTime end);

    // Customer satisfaction
    double getAverageCustomerSatisfactionScore(LocalDateTime start, LocalDateTime end);
    Map<String, Long> getCustomerFeedbackDistribution(LocalDateTime start, LocalDateTime end);

    // Report generation
    AnalyticsDTO generateDailyReport(LocalDateTime date);
    AnalyticsDTO generateMonthlyReport(int year, int month);
    AnalyticsDTO generateYearlyReport(int year);
    AnalyticsDTO generateCustomReport(LocalDateTime start, LocalDateTime end, List<String> metrics);

    // Dashboard metrics
    Map<String, Object> getDashboardMetrics(LocalDateTime start, LocalDateTime end);
    Map<String, Object> getPerformanceIndicators(Long officerId);
    List<Map<String, Object>> getInspectionTrendData(LocalDateTime start, LocalDateTime end);
}