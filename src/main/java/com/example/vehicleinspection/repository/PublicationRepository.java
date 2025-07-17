package com.example.vehicleinspection.repository;

import com.example.vehicleinspection.model.Publication;
import com.example.vehicleinspection.model.VehicleOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findByVehicleOwner(VehicleOwner vehicleOwner);
    List<Publication> findByType(Publication.NotificationType type);
    List<Publication> findByIsReadFalse();
    List<Publication> findByScheduledForBefore(LocalDateTime dateTime);
    List<Publication> findByVehicleOwnerAndIsReadFalse(VehicleOwner vehicleOwner);
    List<Publication> findByPriorityAndSentAtIsNull(String priority);
    List<Publication> findByTypeAndScheduledForBetween(
        Publication.NotificationType type, LocalDateTime start, LocalDateTime end);
    long countByVehicleOwnerAndIsReadFalse(VehicleOwner vehicleOwner);
}