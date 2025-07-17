package com.example.vehicleinspection.repository;

import com.example.vehicleinspection.model.InspectionOfficer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InspectionOfficerRepository extends JpaRepository<InspectionOfficer, Long> {
    Optional<InspectionOfficer> findByBadgeNumber(String badgeNumber);
    List<InspectionOfficer> findByDepartment(String department);
    List<InspectionOfficer> findByIsAvailable(boolean isAvailable);
    List<InspectionOfficer> findBySpecialization(String specialization);
    List<InspectionOfficer> findByYearsOfExperienceGreaterThanEqual(int years);
}