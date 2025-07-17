package com.example.vehicleinspection.controller;

import com.example.vehicleinspection.dto.InspectionOfficerDTO;
import com.example.vehicleinspection.service.InspectionOfficerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/officers")
public class InspectionOfficerController {

    private final InspectionOfficerService officerService;

    public InspectionOfficerController(InspectionOfficerService officerService) {
        this.officerService = officerService;
    }

    @PostMapping
    public ResponseEntity<InspectionOfficerDTO> createOfficer(@Valid @RequestBody InspectionOfficerDTO officerDTO) {
        return ResponseEntity.ok(officerService.createOfficer(officerDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InspectionOfficerDTO> updateOfficer(
            @PathVariable Long id,
            @Valid @RequestBody InspectionOfficerDTO officerDTO) {
        return ResponseEntity.ok(officerService.updateOfficer(id, officerDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspectionOfficerDTO> getOfficer(@PathVariable Long id) {
        return ResponseEntity.ok(officerService.findById(id));
    }

    @GetMapping("/badge/{badgeNumber}")
    public ResponseEntity<InspectionOfficerDTO> getByBadgeNumber(@PathVariable String badgeNumber) {
        return ResponseEntity.ok(officerService.findByBadgeNumber(badgeNumber));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfficer(@PathVariable Long id) {
        officerService.deleteOfficer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<InspectionOfficerDTO>> getByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(officerService.findByDepartment(department));
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<InspectionOfficerDTO>> getBySpecialization(@PathVariable String specialization) {
        return ResponseEntity.ok(officerService.findBySpecialization(specialization));
    }

    @GetMapping("/experience")
    public ResponseEntity<List<InspectionOfficerDTO>> getByExperience(@RequestParam int minYears) {
        return ResponseEntity.ok(officerService.findByExperience(minYears));
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<InspectionOfficerDTO> updateAvailability(
            @PathVariable Long id,
            @RequestBody Map<String, List<LocalDateTime>> availability) {
        return ResponseEntity.ok(officerService.updateAvailability(id, availability));
    }

    @PutMapping("/{id}/inspection-methods")
    public ResponseEntity<InspectionOfficerDTO> updateInspectionMethods(
            @PathVariable Long id,
            @RequestBody List<String> methods) {
        return ResponseEntity.ok(officerService.updateInspectionMethods(id, methods));
    }

    @GetMapping("/available")
    public ResponseEntity<List<InspectionOfficerDTO>> getAvailableOfficers(
            @RequestParam LocalDateTime timeSlot) {
        return ResponseEntity.ok(officerService.findAvailableOfficers(timeSlot));
    }

    @GetMapping("/{id}/completed-inspections")
    public ResponseEntity<Integer> getCompletedInspectionsCount(@PathVariable Long id) {
        return ResponseEntity.ok(officerService.getCompletedInspectionsCount(id));
    }

    @GetMapping("/{id}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long id) {
        return ResponseEntity.ok(officerService.getAverageRating(id));
    }

    @GetMapping("/{id}/certifications")
    public ResponseEntity<List<String>> getCertifications(@PathVariable Long id) {
        return ResponseEntity.ok(officerService.getCertifications(id));
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<Map<String, Object>> getOfficerStatistics(@PathVariable Long id) {
        Map<String, Object> statistics = Map.of(
            "completedInspections", officerService.getCompletedInspectionsCount(id),
            "averageRating", officerService.getAverageRating(id),
            "certifications", officerService.getCertifications(id)
        );
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/search")
    public ResponseEntity<List<InspectionOfficerDTO>> searchOfficers(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) Integer minExperience) {
        if (department != null) {
            return ResponseEntity.ok(officerService.findByDepartment(department));
        } else if (specialization != null) {
            return ResponseEntity.ok(officerService.findBySpecialization(specialization));
        } else if (minExperience != null) {
            return ResponseEntity.ok(officerService.findByExperience(minExperience));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}