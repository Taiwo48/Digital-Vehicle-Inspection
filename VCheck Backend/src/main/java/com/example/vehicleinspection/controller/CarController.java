package com.example.vehicleinspection.controller;

import com.example.vehicleinspection.dto.CarDTO;
import com.example.vehicleinspection.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<CarDTO> createCar(@Valid @RequestBody CarDTO carDTO) {
        return ResponseEntity.ok(carService.createCar(carDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDTO> updateCar(
            @PathVariable Long id,
            @Valid @RequestBody CarDTO carDTO) {
        return ResponseEntity.ok(carService.updateCar(id, carDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCar(@PathVariable Long id) {
        return ResponseEntity.ok(carService.findById(id));
    }

    @GetMapping("/license-plate/{licensePlate}")
    public ResponseEntity<CarDTO> getByLicensePlate(@PathVariable String licensePlate) {
        return ResponseEntity.ok(carService.findByLicensePlate(licensePlate));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<CarDTO>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(carService.findByOwner(ownerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/needs-inspection")
    public ResponseEntity<List<CarDTO>> getCarsNeedingInspection() {
        return ResponseEntity.ok(carService.findCarsNeedingInspection());
    }

    @GetMapping("/expired-insurance")
    public ResponseEntity<List<CarDTO>> getCarsWithExpiredInsurance() {
        return ResponseEntity.ok(carService.findCarsWithExpiredInsurance());
    }

    @PutMapping("/{id}/inspection-status")
    public ResponseEntity<CarDTO> updateInspectionStatus(
            @PathVariable Long id,
            @RequestParam boolean passed) {
        return ResponseEntity.ok(carService.updateInspectionStatus(id, passed));
    }

    @PutMapping("/{id}/insurance")
    public ResponseEntity<CarDTO> updateInsuranceDetails(
            @PathVariable Long id,
            @RequestBody CarDTO carDTO) {
        return ResponseEntity.ok(carService.updateInsuranceDetails(id, 
                carDTO.getInsuranceProvider(),
                carDTO.getInsuranceExpiryDate()));
    }

    @GetMapping("/{id}/inspection-due")
    public ResponseEntity<Boolean> checkInspectionDueStatus(@PathVariable Long id) {
        return ResponseEntity.ok(carService.isInspectionDue(id));
    }

    @GetMapping("/{id}/insurance-valid")
    public ResponseEntity<Boolean> checkInsuranceValidity(@PathVariable Long id) {
        return ResponseEntity.ok(carService.isInsuranceValid(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CarDTO>> searchCars(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer year) {
        if (make != null && model != null) {
            return ResponseEntity.ok(carService.findByMakeAndModel(make, model));
        } else if (year != null) {
            return ResponseEntity.ok(carService.findByYear(year));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}