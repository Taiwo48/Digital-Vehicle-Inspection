package com.example.vehicleinspection.repository;

import com.example.vehicleinspection.model.VehicleOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleOwnerRepository extends JpaRepository<VehicleOwner, Long> {
    Optional<VehicleOwner> findByDriverLicense(String driverLicense);
    boolean existsByDriverLicense(String driverLicense);
    Optional<VehicleOwner> findByEmail(String email);
}