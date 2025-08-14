package com.example.vehicleinspection.controller;

import com.example.vehicleinspection.dto.InspectionBookingDTO;
import com.example.vehicleinspection.model.InspectionBooking;
import com.example.vehicleinspection.service.InspectionBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class InspectionBookingController {

    private final InspectionBookingService bookingService;

    public InspectionBookingController(InspectionBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<InspectionBookingDTO> createBooking(@Valid @RequestBody InspectionBookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InspectionBookingDTO> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody InspectionBookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.updateBooking(id, bookingDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspectionBookingDTO> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/assign-officer/{officerId}")
    public ResponseEntity<InspectionBookingDTO> assignOfficer(
            @PathVariable Long id,
            @PathVariable Long officerId) {
        return ResponseEntity.ok(bookingService.assignOfficer(id, officerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<InspectionBookingDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam InspectionBooking.InspectionStatus status) {
        return ResponseEntity.ok(bookingService.updateStatus(id, status));
    }

    @GetMapping("/vehicle-owner/{ownerId}")
    public ResponseEntity<List<InspectionBookingDTO>> getByVehicleOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(bookingService.findByVehicleOwner(ownerId));
    }

    @GetMapping("/officer/{officerId}")
    public ResponseEntity<List<InspectionBookingDTO>> getByInspectionOfficer(@PathVariable Long officerId) {
        return ResponseEntity.ok(bookingService.findByInspectionOfficer(officerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<InspectionBookingDTO>> getByStatus(
            @PathVariable InspectionBooking.InspectionStatus status) {
        return ResponseEntity.ok(bookingService.findByStatus(status));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<InspectionBookingDTO>> getByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(bookingService.findByDateRange(start, end));
    }

    @GetMapping("/officer/{officerId}/date-range")
    public ResponseEntity<List<InspectionBookingDTO>> getByOfficerAndDateRange(
            @PathVariable Long officerId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(bookingService.findByOfficerAndDateRange(officerId, start, end));
    }

    @GetMapping("/owner/{ownerId}/status/{status}")
    public ResponseEntity<List<InspectionBookingDTO>> getByOwnerAndStatus(
            @PathVariable Long ownerId,
            @PathVariable InspectionBooking.InspectionStatus status) {
        return ResponseEntity.ok(bookingService.findByOwnerAndStatus(ownerId, status));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<InspectionBookingDTO> completeInspection(
            @PathVariable Long id,
            @RequestParam String result,
            @RequestParam String recommendations) {
        return ResponseEntity.ok(bookingService.completeInspection(id, result, recommendations));
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<InspectionBookingDTO> rescheduleBooking(
            @PathVariable Long id,
            @RequestParam LocalDateTime newDateTime) {
        return ResponseEntity.ok(bookingService.rescheduleBooking(id, newDateTime));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/time-slot-available")
    public ResponseEntity<Boolean> checkTimeSlotAvailability(
            @RequestParam Long officerId,
            @RequestParam LocalDateTime dateTime) {
        return ResponseEntity.ok(bookingService.isTimeSlotAvailable(officerId, dateTime));
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalDateTime>> getAvailableTimeSlots(
            @RequestParam Long officerId,
            @RequestParam LocalDateTime date) {
        return ResponseEntity.ok(bookingService.getAvailableTimeSlots(officerId, date));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Object> getBookingStatistics(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(Map.of(
            "completedInspections", bookingService.getCompletedInspectionsCount(start, end),
            "averageDuration", bookingService.getAverageInspectionDuration()
        ));
    }
}