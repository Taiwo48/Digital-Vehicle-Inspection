package com.example.vehicleinspection.repository;

import com.example.vehicleinspection.model.Analytics;
import com.example.vehicleinspection.model.InspectionBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    List<Analytics> findByInspectionDate(LocalDateTime date);
    List<Analytics> findByInspectionDateBetween(LocalDateTime start, LocalDateTime end);
    List<Analytics> findByPassedInspection(boolean passed);
    List<Analytics> findByInspectionType(String type);
    
    @Query("SELECT AVG(a.inspectionDuration) FROM Analytics a WHERE a.inspectionDate BETWEEN ?1 AND ?2")
    Double getAverageInspectionDuration(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT COUNT(a) FROM Analytics a WHERE a.passedInspection = true AND a.inspectionDate BETWEEN ?1 AND ?2")
    Long getPassedInspectionsCount(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT a.vehicleCategory, COUNT(a) FROM Analytics a WHERE a.inspectionDate BETWEEN ?1 AND ?2 GROUP BY a.vehicleCategory")
    List<Object[]> getInspectionsByVehicleCategory(LocalDateTime start, LocalDateTime end);
    
    Optional<Analytics> findByInspection(InspectionBooking inspection);
}