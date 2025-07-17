package com.example.vehicleinspection.service;

import com.example.vehicleinspection.model.InspectionBooking;
import com.example.vehicleinspection.dto.InspectionBookingDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface InspectionBookingService extends BaseService<InspectionBooking, Long> {
    
    InspectionBookingDTO createBooking(InspectionBookingDTO bookingDTO);
    
    InspectionBookingDTO updateBooking(Long id, InspectionBookingDTO bookingDTO);
    
    InspectionBookingDTO assignOfficer(Long bookingId, Long officerId);
    
    InspectionBookingDTO updateStatus(Long id, InspectionBooking.InspectionStatus status);
    
    List<InspectionBookingDTO> findByVehicleOwner(Long ownerId);
    
    List<InspectionBookingDTO> findByInspectionOfficer(Long officerId);
    
    List<InspectionBookingDTO> findByStatus(InspectionBooking.InspectionStatus status);
    
    List<InspectionBookingDTO> findByDateRange(LocalDateTime start, LocalDateTime end);
    
    List<InspectionBookingDTO> findByOfficerAndDateRange(Long officerId, LocalDateTime start, LocalDateTime end);
    
    List<InspectionBookingDTO> findByOwnerAndStatus(Long ownerId, InspectionBooking.InspectionStatus status);
    
    InspectionBookingDTO completeInspection(Long id, String result, String recommendations);
    
    InspectionBookingDTO rescheduleBooking(Long id, LocalDateTime newDateTime);
    
    void cancelBooking(Long id);
    
    boolean isTimeSlotAvailable(Long officerId, LocalDateTime dateTime);
    
    List<LocalDateTime> getAvailableTimeSlots(Long officerId, LocalDateTime date);
    
    long getCompletedInspectionsCount(LocalDateTime start, LocalDateTime end);
    
    double getAverageInspectionDuration();
}