package com.example.vehicleinspection.controller;

import com.example.vehicleinspection.dto.PublicationDTO;
import com.example.vehicleinspection.model.Publication;
import com.example.vehicleinspection.service.PublicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publications")
public class PublicationController {

    private final PublicationService publicationService;

    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @PostMapping
    public ResponseEntity<PublicationDTO> createPublication(@Valid @RequestBody PublicationDTO publicationDTO) {
        return ResponseEntity.ok(publicationService.createPublication(publicationDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicationDTO> updatePublication(
            @PathVariable Long id,
            @Valid @RequestBody PublicationDTO publicationDTO) {
        return ResponseEntity.ok(publicationService.updatePublication(id, publicationDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicationDTO> getPublication(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        publicationService.deletePublication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicle-owner/{ownerId}")
    public ResponseEntity<List<PublicationDTO>> getByVehicleOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(publicationService.findByVehicleOwner(ownerId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<PublicationDTO>> getByType(
            @PathVariable Publication.NotificationType type) {
        return ResponseEntity.ok(publicationService.findByType(type));
    }

    @GetMapping("/read-status/{read}")
    public ResponseEntity<List<PublicationDTO>> getByReadStatus(@PathVariable boolean read) {
        return ResponseEntity.ok(publicationService.findByReadStatus(read));
    }

    @GetMapping("/scheduled-date/{date}")
    public ResponseEntity<List<PublicationDTO>> getByScheduledDate(@PathVariable LocalDateTime date) {
        return ResponseEntity.ok(publicationService.findByScheduledDate(date));
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<PublicationDTO>> getByPriority(
            @PathVariable Publication.Priority priority) {
        return ResponseEntity.ok(publicationService.findByPriority(priority));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PublicationDTO>> getByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(publicationService.findByDateRange(start, end));
    }

    @GetMapping("/owner/{ownerId}/type/{type}")
    public ResponseEntity<List<PublicationDTO>> getByOwnerAndType(
            @PathVariable Long ownerId,
            @PathVariable Publication.NotificationType type) {
        return ResponseEntity.ok(publicationService.findByOwnerAndType(ownerId, type));
    }

    @GetMapping("/owner/{ownerId}/read-status/{read}")
    public ResponseEntity<List<PublicationDTO>> getByOwnerAndReadStatus(
            @PathVariable Long ownerId,
            @PathVariable boolean read) {
        return ResponseEntity.ok(publicationService.findByOwnerAndReadStatus(ownerId, read));
    }

    @GetMapping("/type/{type}/priority/{priority}")
    public ResponseEntity<List<PublicationDTO>> getByTypeAndPriority(
            @PathVariable Publication.NotificationType type,
            @PathVariable Publication.Priority priority) {
        return ResponseEntity.ok(publicationService.findByTypeAndPriority(type, priority));
    }

    @PutMapping("/{id}/mark-read")
    public ResponseEntity<PublicationDTO> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.markAsRead(id));
    }

    @PutMapping("/{id}/mark-unread")
    public ResponseEntity<PublicationDTO> markAsUnread(@PathVariable Long id) {
        return ResponseEntity.ok(publicationService.markAsUnread(id));
    }

    @PutMapping("/{id}/delivery-status")
    public ResponseEntity<PublicationDTO> updateDeliveryStatus(
            @PathVariable Long id,
            @RequestParam Publication.DeliveryStatus status) {
        return ResponseEntity.ok(publicationService.updateDeliveryStatus(id, status));
    }

    @PostMapping("/schedule")
    public ResponseEntity<PublicationDTO> schedulePublication(@Valid @RequestBody PublicationDTO publicationDTO) {
        return ResponseEntity.ok(publicationService.schedulePublication(publicationDTO));
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendImmediateNotification(@Valid @RequestBody PublicationDTO publicationDTO) {
        publicationService.sendImmediateNotification(publicationDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/resend")
    public ResponseEntity<Void> resendFailedNotification(@PathVariable Long id) {
        publicationService.resendFailedNotification(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/owner/{ownerId}/mark-all-read")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long ownerId) {
        publicationService.markAllAsRead(ownerId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/expired")
    public ResponseEntity<Void> deleteExpiredPublications(@RequestParam LocalDateTime expiryDate) {
        publicationService.deleteExpiredPublications(expiryDate);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> sendBatchNotifications(@RequestBody List<PublicationDTO> publications) {
        publicationService.sendBatchNotifications(publications);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/owner/{ownerId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long ownerId) {
        return ResponseEntity.ok(publicationService.countUnreadPublications(ownerId));
    }

    @GetMapping("/statistics/type-distribution")
    public ResponseEntity<Map<Publication.NotificationType, Long>> getTypeDistribution(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(publicationService.getPublicationTypeDistribution(start, end));
    }

    @GetMapping("/statistics/delivery-status")
    public ResponseEntity<Map<Publication.DeliveryStatus, Long>> getDeliveryStatusDistribution(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(publicationService.getDeliveryStatusDistribution(start, end));
    }

    @GetMapping("/statistics/delivery-success-rate")
    public ResponseEntity<Double> getDeliverySuccessRate(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(publicationService.getDeliverySuccessRate(start, end));
    }

    @PostMapping("/templates/{templateId}")
    public ResponseEntity<PublicationDTO> createFromTemplate(
            @PathVariable String templateId,
            @RequestBody Map<String, Object> parameters) {
        return ResponseEntity.ok(publicationService.createFromTemplate(templateId, parameters));
    }

    @PutMapping("/templates/{templateId}")
    public ResponseEntity<Void> saveTemplate(
            @PathVariable String templateId,
            @RequestBody String content) {
        publicationService.saveTemplate(templateId, content);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/templates/{templateId}")
    public ResponseEntity<String> getTemplate(@PathVariable String templateId) {
        return ResponseEntity.ok(publicationService.getTemplate(templateId));
    }

    @PutMapping("/owner/{ownerId}/notification-channels")
    public ResponseEntity<Void> configureNotificationChannels(
            @PathVariable Long ownerId,
            @RequestBody Map<String, Boolean> channelPreferences) {
        publicationService.configureNotificationChannels(ownerId, channelPreferences);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/owner/{ownerId}/notification-channels")
    public ResponseEntity<Map<String, Boolean>> getNotificationChannelPreferences(@PathVariable Long ownerId) {
        return ResponseEntity.ok(publicationService.getNotificationChannelPreferences(ownerId));
    }

    @GetMapping("/reports/delivery")
    public ResponseEntity<Map<String, Object>> getDeliveryReport(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(publicationService.generateDeliveryReport(start, end));
    }

    @GetMapping("/owner/{ownerId}/history")
    public ResponseEntity<List<Map<String, Object>>> getNotificationHistory(
            @PathVariable Long ownerId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(publicationService.getNotificationHistory(ownerId, start, end));
    }

    @GetMapping("/owner/{ownerId}/engagement")
    public ResponseEntity<Map<String, Long>> getEngagementMetrics(@PathVariable Long ownerId) {
        return ResponseEntity.ok(publicationService.getEngagementMetrics(ownerId));
    }
}