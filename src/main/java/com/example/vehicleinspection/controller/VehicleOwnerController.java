package com.example.vehicleinspection.controller;

import com.example.vehicleinspection.dto.VehicleOwnerDTO;
import com.example.vehicleinspection.service.VehicleOwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle-owners")
public class VehicleOwnerController {

    private final VehicleOwnerService vehicleOwnerService;

    public VehicleOwnerController(VehicleOwnerService vehicleOwnerService) {
        this.vehicleOwnerService = vehicleOwnerService;
    }

    @PostMapping
    public ResponseEntity<VehicleOwnerDTO> createVehicleOwner(@Valid @RequestBody VehicleOwnerDTO vehicleOwnerDTO) {
        return ResponseEntity.ok(vehicleOwnerService.createVehicleOwner(vehicleOwnerDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleOwnerDTO> updateVehicleOwner(
            @PathVariable Long id,
            @Valid @RequestBody VehicleOwnerDTO vehicleOwnerDTO) {
        return ResponseEntity.ok(vehicleOwnerService.updateVehicleOwner(id, vehicleOwnerDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleOwnerDTO> getVehicleOwner(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleOwnerService.findById(id));
    }

    @GetMapping("/license/{driversLicense}")
    public ResponseEntity<VehicleOwnerDTO> getByDriversLicense(@PathVariable String driversLicense) {
        return ResponseEntity.ok(vehicleOwnerService.findByDriversLicense(driversLicense));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<VehicleOwnerDTO> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(vehicleOwnerService.findByEmail(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleOwner(@PathVariable Long id) {
        vehicleOwnerService.deleteVehicleOwner(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pending-inspections")
    public ResponseEntity<List<VehicleOwnerDTO>> getOwnersWithPendingInspections() {
        return ResponseEntity.ok(vehicleOwnerService.findOwnersWithPendingInspections());
    }

    @GetMapping("/expired-insurance")
    public ResponseEntity<List<VehicleOwnerDTO>> getOwnersWithExpiredInsurance() {
        return ResponseEntity.ok(vehicleOwnerService.findOwnersWithExpiredInsurance());
    }

    @PostMapping("/{id}/vehicles/{vehicleId}")
    public ResponseEntity<VehicleOwnerDTO> addVehicle(
            @PathVariable Long id,
            @PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleOwnerService.addVehicle(id, vehicleId));
    }

    @DeleteMapping("/{id}/vehicles/{vehicleId}")
    public ResponseEntity<VehicleOwnerDTO> removeVehicle(
            @PathVariable Long id,
            @PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleOwnerService.removeVehicle(id, vehicleId));
    }

    @GetMapping("/{id}/vehicle-count")
    public ResponseEntity<Integer> getVehicleCount(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleOwnerService.getVehicleCount(id));
    }

    @GetMapping("/{id}/pending-inspection-count")
    public ResponseEntity<Integer> getPendingInspectionCount(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleOwnerService.getPendingInspectionCount(id));
    }
}