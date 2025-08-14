package com.example.vehicleinspection.service.impl;

import com.example.vehicleinspection.model.Analytics;
import com.example.vehicleinspection.model.InspectionBooking;
import com.example.vehicleinspection.dto.AnalyticsDTO;
import com.example.vehicleinspection.repository.AnalyticsRepository;
import com.example.vehicleinspection.repository.InspectionBookingRepository;
import com.example.vehicleinspection.service.AnalyticsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnalyticsServiceImpl extends BaseServiceImpl<Analytics, Long> implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final InspectionBookingRepository bookingRepository;

    public AnalyticsServiceImpl(AnalyticsRepository analyticsRepository,
                               InspectionBookingRepository bookingRepository) {
        super(analyticsRepository);
        this.analyticsRepository = analyticsRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public AnalyticsDTO createAnalytics(AnalyticsDTO analyticsDTO) {
        Analytics analytics = new Analytics();
        BeanUtils.copyProperties(analyticsDTO, analytics);
        analytics = analyticsRepository.save(analytics);
        return convertToDTO(analytics);
    }

    @Override
    public AnalyticsDTO updateAnalytics(Long id, AnalyticsDTO analyticsDTO) {
        return analyticsRepository.findById(id)
                .map(analytics -> {
                    BeanUtils.copyProperties(analyticsDTO, analytics, "id");
                    analytics = analyticsRepository.save(analytics);
                    return convertToDTO(analytics);
                })
                .orElseThrow(() -> new RuntimeException("Analytics not found"));
    }

    @Override
    public void deleteAnalytics(Long id) {
        analyticsRepository.deleteById(id);
    }

    @Override
    public List<AnalyticsDTO> findByInspectionDate(LocalDateTime date) {
        return analyticsRepository.findByInspectionDate(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnalyticsDTO> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return analyticsRepository.findByInspectionDateBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnalyticsDTO> findByPassedStatus(boolean passed) {
        return analyticsRepository.findByPassed(passed).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnalyticsDTO> findByInspectionType(String type) {
        return analyticsRepository.findByInspectionType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnalyticsDTO> findByInspectionBooking(Long bookingId) {
        return analyticsRepository.findByInspectionBookingId(bookingId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public double getAverageInspectionDuration(LocalDateTime start, LocalDateTime end) {
        return analyticsRepository.calculateAverageInspectionDuration(start, end);
    }

    @Override
    public long getPassedInspectionsCount(LocalDateTime start, LocalDateTime end) {
        return analyticsRepository.countByPassedAndInspectionDateBetween(true, start, end);
    }

    @Override
    public long getFailedInspectionsCount(LocalDateTime start, LocalDateTime end) {
        return analyticsRepository.countByPassedAndInspectionDateBetween(false, start, end);
    }

    @Override
    public double getPassRate(LocalDateTime start, LocalDateTime end) {
        long passed = getPassedInspectionsCount(start, end);
        long total = passed + getFailedInspectionsCount(start, end);
        return total > 0 ? (double) passed / total : 0.0;
    }

    @Override
    public Map<String, Long> getInspectionsByVehicleCategory(LocalDateTime start, LocalDateTime end) {
        return analyticsRepository.getInspectionCountByVehicleCategory(start, end);
    }

    @Override
    public double getAverageInspectionScore(Long officerId) {
        return analyticsRepository.calculateAverageInspectionScore(officerId);
    }

    @Override
    public Map<String, Double> getOfficerPerformanceMetrics(Long officerId) {
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("averageScore", getAverageInspectionScore(officerId));
        metrics.put("passRate", analyticsRepository.calculateOfficerPassRate(officerId));
        metrics.put("averageDuration", analyticsRepository.calculateAverageInspectionDurationByOfficer(officerId));
        return metrics;
    }

    @Override
    public List<String> getTopDefects(LocalDateTime start, LocalDateTime end, int limit) {
        return analyticsRepository.findMostCommonDefects(start, end, limit);
    }

    @Override
    public Map<String, Long> getMonthlyInspectionTrends(int year) {
        Map<String, Long> trends = new HashMap<>();
        for (Month month : Month.values()) {
            String monthName = month.name();
            long count = analyticsRepository.countInspectionsByMonth(year, month.getValue());
            trends.put(monthName, count);
        }
        return trends;
    }

    @Override
    public Map<String, Double> getSeasonalPatterns(int year) {
        Map<String, Double> patterns = new HashMap<>();
        patterns.put("SPRING", calculateSeasonalAverage(year, Arrays.asList(Month.MARCH, Month.APRIL, Month.MAY)));
        patterns.put("SUMMER", calculateSeasonalAverage(year, Arrays.asList(Month.JUNE, Month.JULY, Month.AUGUST)));
        patterns.put("FALL", calculateSeasonalAverage(year, Arrays.asList(Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER)));
        patterns.put("WINTER", calculateSeasonalAverage(year, Arrays.asList(Month.DECEMBER, Month.JANUARY, Month.FEBRUARY)));
        return patterns;
    }

    @Override
    public double getYearOverYearGrowth(int year) {
        long currentYearCount = analyticsRepository.countInspectionsByYear(year);
        long previousYearCount = analyticsRepository.countInspectionsByYear(year - 1);
        return previousYearCount > 0 ? 
            ((double) currentYearCount - previousYearCount) / previousYearCount * 100 : 0.0;
    }

    @Override
    public double getOverallSafetyScore(LocalDateTime start, LocalDateTime end) {
        return analyticsRepository.calculateAverageSafetyScore(start, end);
    }

    @Override
    public double getComplianceRate(LocalDateTime start, LocalDateTime end) {
        return analyticsRepository.calculateComplianceRate(start, end);
    }

    @Override
    public List<String> getCriticalIssuesByFrequency(LocalDateTime start, LocalDateTime end) {
        return analyticsRepository.findMostCommonCriticalIssues(start, end);
    }

    @Override
    public double getAverageCustomerSatisfactionScore(LocalDateTime start, LocalDateTime end) {
        return analyticsRepository.calculateAverageCustomerSatisfactionScore(start, end);
    }

    @Override
    public Map<String, Long> getCustomerFeedbackDistribution(LocalDateTime start, LocalDateTime end) {
        return analyticsRepository.getCustomerFeedbackDistribution(start, end);
    }

    @Override
    public AnalyticsDTO generateDailyReport(LocalDateTime date) {
        AnalyticsDTO report = new AnalyticsDTO();
        LocalDateTime endDate = date.plusDays(1);
        
        report.setInspectionDate(date);
        report.setTotalInspections((int) (getPassedInspectionsCount(date, endDate) + 
                                        getFailedInspectionsCount(date, endDate)));
        report.setPassRate(getPassRate(date, endDate));
        report.setAverageInspectionDuration(getAverageInspectionDuration(date, endDate));
        report.setSafetyScore(getOverallSafetyScore(date, endDate));
        report.setComplianceScore(getComplianceRate(date, endDate));
        report.setCustomerSatisfactionScore(getAverageCustomerSatisfactionScore(date, endDate));
        
        return report;
    }

    @Override
    public AnalyticsDTO generateMonthlyReport(int year, int month) {
        AnalyticsDTO report = new AnalyticsDTO();
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1);

        report.setInspectionDate(startDate);
        report.setTotalInspections((int) (getPassedInspectionsCount(startDate, endDate) + 
                                        getFailedInspectionsCount(startDate, endDate)));
        report.setPassRate(getPassRate(startDate, endDate));
        report.setAverageInspectionDuration(getAverageInspectionDuration(startDate, endDate));
        report.setSafetyScore(getOverallSafetyScore(startDate, endDate));
        report.setComplianceScore(getComplianceRate(startDate, endDate));
        report.setCustomerSatisfactionScore(getAverageCustomerSatisfactionScore(startDate, endDate));

        return report;
    }

    @Override
    public AnalyticsDTO generateYearlyReport(int year) {
        AnalyticsDTO report = new AnalyticsDTO();
        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = startDate.plusYears(1);

        report.setInspectionDate(startDate);
        report.setTotalInspections((int) (getPassedInspectionsCount(startDate, endDate) + 
                                        getFailedInspectionsCount(startDate, endDate)));
        report.setPassRate(getPassRate(startDate, endDate));
        report.setAverageInspectionDuration(getAverageInspectionDuration(startDate, endDate));
        report.setSafetyScore(getOverallSafetyScore(startDate, endDate));
        report.setComplianceScore(getComplianceRate(startDate, endDate));
        report.setCustomerSatisfactionScore(getAverageCustomerSatisfactionScore(startDate, endDate));
        report.setYearOverYearGrowth(getYearOverYearGrowth(year));

        return report;
    }

    @Override
    public AnalyticsDTO generateCustomReport(LocalDateTime start, LocalDateTime end, List<String> metrics) {
        AnalyticsDTO report = new AnalyticsDTO();
        report.setInspectionDate(start);

        for (String metric : metrics) {
            switch (metric.toUpperCase()) {
                case "TOTAL_INSPECTIONS":
                    report.setTotalInspections((int) (getPassedInspectionsCount(start, end) + 
                                                    getFailedInspectionsCount(start, end)));
                    break;
                case "PASS_RATE":
                    report.setPassRate(getPassRate(start, end));
                    break;
                case "AVERAGE_DURATION":
                    report.setAverageInspectionDuration(getAverageInspectionDuration(start, end));
                    break;
                case "SAFETY_SCORE":
                    report.setSafetyScore(getOverallSafetyScore(start, end));
                    break;
                case "COMPLIANCE_SCORE":
                    report.setComplianceScore(getComplianceRate(start, end));
                    break;
                case "CUSTOMER_SATISFACTION":
                    report.setCustomerSatisfactionScore(getAverageCustomerSatisfactionScore(start, end));
                    break;
            }
        }

        return report;
    }

    @Override
    public Map<String, Object> getDashboardMetrics(LocalDateTime start, LocalDateTime end) {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalInspections", getPassedInspectionsCount(start, end) + getFailedInspectionsCount(start, end));
        metrics.put("passRate", getPassRate(start, end));
        metrics.put("averageDuration", getAverageInspectionDuration(start, end));
        metrics.put("safetyScore", getOverallSafetyScore(start, end));
        metrics.put("complianceRate", getComplianceRate(start, end));
        metrics.put("customerSatisfaction", getAverageCustomerSatisfactionScore(start, end));
        metrics.put("topDefects", getTopDefects(start, end, 5));
        metrics.put("vehicleCategories", getInspectionsByVehicleCategory(start, end));
        return metrics;
    }

    @Override
    public Map<String, Object> getPerformanceIndicators(Long officerId) {
        Map<String, Object> indicators = new HashMap<>();
        indicators.put("performanceMetrics", getOfficerPerformanceMetrics(officerId));
        indicators.put("inspectionCount", analyticsRepository.countInspectionsByOfficer(officerId));
        indicators.put("averageScore", getAverageInspectionScore(officerId));
        return indicators;
    }

    @Override
    public List<Map<String, Object>> getInspectionTrendData(LocalDateTime start, LocalDateTime end) {
        return analyticsRepository.getInspectionTrendData(start, end);
    }

    private double calculateSeasonalAverage(int year, List<Month> months) {
        return months.stream()
                .mapToLong(month -> analyticsRepository.countInspectionsByMonth(year, month.getValue()))
                .average()
                .orElse(0.0);
    }

    private AnalyticsDTO convertToDTO(Analytics analytics) {
        AnalyticsDTO dto = new AnalyticsDTO();
        BeanUtils.copyProperties(analytics, dto);
        
        // Set additional calculated fields
        if (analytics.getInspectionDate() != null) {
            LocalDateTime end = analytics.getInspectionDate().plusDays(1);
            dto.setPassRate(getPassRate(analytics.getInspectionDate(), end));
            dto.setSafetyScore(getOverallSafetyScore(analytics.getInspectionDate(), end));
            dto.setComplianceScore(getComplianceRate(analytics.getInspectionDate(), end));
        }
        
        return dto;
    }
}