package com.example.vehicleinspection.repository;

import com.example.vehicleinspection.model.InspectionBooking;
import com.example.vehicleinspection.model.InspectionOfficer;
import com.example.vehicleinspection.model.VehicleOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InspectionBookingRepository extends JpaRepository<InspectionBooking, Long> {
    List<InspectionBooking> findByVehicleOwner(VehicleOwner vehicleOwner);
    List<InspectionBooking> findByInspectionOfficer(InspectionOfficer officer);
    List<InspectionBooking> findByStatus(InspectionBooking.InspectionStatus status);
    List<InspectionBooking> findByScheduledDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<InspectionBooking> findByInspectionOfficerAndScheduledDateTimeBetween(
        InspectionOfficer officer, LocalDateTime start, LocalDateTime end);
    List<InspectionBooking> findByVehicleOwnerAndStatus(
        VehicleOwner owner, InspectionBooking.InspectionStatus status);
    long countByStatusAndScheduledDateTimeBetween(
        InspectionBooking.InspectionStatus status, LocalDateTime start, LocalDateTime end);
}