package com.example.vehicleinspection.controller;

import com.example.vehicleinspection.dto.AnalyticsDTO;
import com.example.vehicleinspection.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping
    public ResponseEntity<AnalyticsDTO> createAnalytics(@Valid @RequestBody AnalyticsDTO analyticsDTO) {
        return ResponseEntity.ok(analyticsService.createAnalytics(analyticsDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnalyticsDTO> updateAnalytics(
            @PathVariable Long id,
            @Valid @RequestBody AnalyticsDTO analyticsDTO) {
        return ResponseEntity.ok(analyticsService.updateAnalytics(id, analyticsDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalyticsDTO> getAnalytics(@PathVariable Long id) {
        return ResponseEntity.ok(analyticsService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalytics(@PathVariable Long id) {
        analyticsService.deleteAnalytics(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<AnalyticsDTO>> getByInspectionDate(@PathVariable LocalDateTime date) {
        return ResponseEntity.ok(analyticsService.findByInspectionDate(date));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AnalyticsDTO>> getByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.findByDateRange(start, end));
    }

    @GetMapping("/passed/{passed}")
    public ResponseEntity<List<AnalyticsDTO>> getByPassedStatus(@PathVariable boolean passed) {
        return ResponseEntity.ok(analyticsService.findByPassedStatus(passed));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<AnalyticsDTO>> getByInspectionType(@PathVariable String type) {
        return ResponseEntity.ok(analyticsService.findByInspectionType(type));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<AnalyticsDTO>> getByInspectionBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(analyticsService.findByInspectionBooking(bookingId));
    }

    @GetMapping("/statistics/inspection-duration")
    public ResponseEntity<Double> getAverageInspectionDuration(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getAverageInspectionDuration(start, end));
    }

    @GetMapping("/statistics/pass-rate")
    public ResponseEntity<Double> getPassRate(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getPassRate(start, end));
    }

    @GetMapping("/statistics/vehicle-categories")
    public ResponseEntity<Map<String, Long>> getInspectionsByVehicleCategory(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getInspectionsByVehicleCategory(start, end));
    }

    @GetMapping("/officer/{officerId}/performance")
    public ResponseEntity<Map<String, Double>> getOfficerPerformanceMetrics(@PathVariable Long officerId) {
        return ResponseEntity.ok(analyticsService.getOfficerPerformanceMetrics(officerId));
    }

    @GetMapping("/defects/top")
    public ResponseEntity<List<String>> getTopDefects(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getTopDefects(start, end, limit));
    }

    @GetMapping("/trends/monthly/{year}")
    public ResponseEntity<Map<String, Long>> getMonthlyInspectionTrends(@PathVariable int year) {
        return ResponseEntity.ok(analyticsService.getMonthlyInspectionTrends(year));
    }

    @GetMapping("/trends/seasonal/{year}")
    public ResponseEntity<Map<String, Double>> getSeasonalPatterns(@PathVariable int year) {
        return ResponseEntity.ok(analyticsService.getSeasonalPatterns(year));
    }

    @GetMapping("/growth/{year}")
    public ResponseEntity<Double> getYearOverYearGrowth(@PathVariable int year) {
        return ResponseEntity.ok(analyticsService.getYearOverYearGrowth(year));
    }

    @GetMapping("/safety-score")
    public ResponseEntity<Double> getOverallSafetyScore(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getOverallSafetyScore(start, end));
    }

    @GetMapping("/compliance-rate")
    public ResponseEntity<Double> getComplianceRate(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getComplianceRate(start, end));
    }

    @GetMapping("/critical-issues")
    public ResponseEntity<List<String>> getCriticalIssues(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getCriticalIssuesByFrequency(start, end));
    }

    @GetMapping("/customer-satisfaction")
    public ResponseEntity<Double> getCustomerSatisfactionScore(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getAverageCustomerSatisfactionScore(start, end));
    }

    @GetMapping("/reports/daily/{date}")
    public ResponseEntity<AnalyticsDTO> getDailyReport(@PathVariable LocalDateTime date) {
        return ResponseEntity.ok(analyticsService.generateDailyReport(date));
    }

    @GetMapping("/reports/monthly/{year}/{month}")
    public ResponseEntity<AnalyticsDTO> getMonthlyReport(
            @PathVariable int year,
            @PathVariable int month) {
        return ResponseEntity.ok(analyticsService.generateMonthlyReport(year, month));
    }

    @GetMapping("/reports/yearly/{year}")
    public ResponseEntity<AnalyticsDTO> getYearlyReport(@PathVariable int year) {
        return ResponseEntity.ok(analyticsService.generateYearlyReport(year));
    }

    @PostMapping("/reports/custom")
    public ResponseEntity<AnalyticsDTO> getCustomReport(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestBody List<String> metrics) {
        return ResponseEntity.ok(analyticsService.generateCustomReport(start, end, metrics));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardMetrics(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getDashboardMetrics(start, end));
    }

    @GetMapping("/officer/{officerId}/indicators")
    public ResponseEntity<Map<String, Object>> getPerformanceIndicators(@PathVariable Long officerId) {
        return ResponseEntity.ok(analyticsService.getPerformanceIndicators(officerId));
    }

    @GetMapping("/trends")
    public ResponseEntity<List<Map<String, Object>>> getInspectionTrendData(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(analyticsService.getInspectionTrendData(start, end));
    }
}