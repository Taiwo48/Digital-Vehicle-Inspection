package com.example.vehicleinspection.service;

import com.example.vehicleinspection.model.InspectionOfficer;
import com.example.vehicleinspection.dto.InspectionOfficerDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InspectionOfficerService extends BaseService<InspectionOfficer, Long> {
    
    InspectionOfficerDTO createOfficer(InspectionOfficerDTO officerDTO);
    
    InspectionOfficerDTO updateOfficer(Long id, InspectionOfficerDTO officerDTO);
    
    Optional<InspectionOfficerDTO> findByBadgeNumber(String badgeNumber);
    
    List<InspectionOfficerDTO> findByDepartment(String department);
    
    List<InspectionOfficerDTO> findAllOfficers();
    
    void deleteOfficer(Long id);
    
    List<InspectionOfficerDTO> findAvailableOfficers();
    
    List<InspectionOfficerDTO> findBySpecialization(String specialization);
    
    List<InspectionOfficerDTO> findByExperience(int minYears);
    
    InspectionOfficerDTO updateAvailability(Long id, boolean isAvailable);
    
    InspectionOfficerDTO addInspectionMethod(Long id, String method);
    
    InspectionOfficerDTO removeInspectionMethod(Long id, String method);
    
    List<InspectionOfficerDTO> findOfficersAvailableForTimeSlot(LocalDateTime startTime, LocalDateTime endTime);
    
    int getCompletedInspectionsCount(Long officerId);
    
    double getAverageInspectionRating(Long officerId);
    
    List<String> getOfficerCertifications(Long officerId);
}