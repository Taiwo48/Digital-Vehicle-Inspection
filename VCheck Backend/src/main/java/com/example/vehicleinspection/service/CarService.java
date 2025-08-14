package com.example.vehicleinspection.service;

import com.example.vehicleinspection.model.Car;
import com.example.vehicleinspection.dto.CarDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CarService extends BaseService<Car, Long> {
    
    CarDTO createCar(CarDTO carDTO);
    
    CarDTO updateCar(Long id, CarDTO carDTO);
    
    Optional<CarDTO> findByLicensePlate(String licensePlate);
    
    List<CarDTO> findByOwner(Long ownerId);
    
    List<CarDTO> findAllCars();
    
    void deleteCar(Long id);
    
    boolean existsByLicensePlate(String licensePlate);
    
    List<CarDTO> findCarsNeedingInspection();
    
    List<CarDTO> findCarsWithExpiredInsurance();
    
    CarDTO updateInspectionStatus(Long id, String status, LocalDate inspectionDate);
    
    CarDTO updateInsurance(Long id, String provider, String policyNumber, LocalDate expiryDate);
    
    boolean isInspectionDue(Long id);
    
    boolean isInsuranceValid(Long id);
    
    List<CarDTO> findCarsByMakeAndModel(String make, String model);
    
    List<CarDTO> findCarsByYear(Integer year);
}