package com.example.vehicleinspection.repository;

import com.example.vehicleinspection.model.Car;
import com.example.vehicleinspection.model.VehicleOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByLicensePlate(String licensePlate);
    List<Car> findByOwner(VehicleOwner owner);
    List<Car> findByLastInspectionDateBefore(LocalDate date);
    List<Car> findByInsuranceExpiryDateBefore(LocalDate date);
    boolean existsByLicensePlate(String licensePlate);
}