package com.example.vehicleinspection.service;

import com.example.vehicleinspection.model.Publication;
import com.example.vehicleinspection.dto.PublicationDTO;
import com.example.vehicleinspection.model.VehicleOwner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PublicationService extends BaseService<Publication, Long> {

    // Basic CRUD operations
    PublicationDTO createPublication(PublicationDTO publicationDTO);
    PublicationDTO updatePublication(Long id, PublicationDTO publicationDTO);
    void deletePublication(Long id);

    // Find operations
    List<PublicationDTO> findByVehicleOwner(Long ownerId);
    List<PublicationDTO> findByType(Publication.NotificationType type);
    List<PublicationDTO> findByReadStatus(boolean read);
    List<PublicationDTO> findByScheduledDate(LocalDateTime date);
    List<PublicationDTO> findByPriority(Publication.Priority priority);
    List<PublicationDTO> findByDateRange(LocalDateTime start, LocalDateTime end);

    // Combined search operations
    List<PublicationDTO> findByOwnerAndType(Long ownerId, Publication.NotificationType type);
    List<PublicationDTO> findByOwnerAndReadStatus(Long ownerId, boolean read);
    List<PublicationDTO> findByTypeAndPriority(Publication.NotificationType type, Publication.Priority priority);

    // Status operations
    PublicationDTO markAsRead(Long id);
    PublicationDTO markAsUnread(Long id);
    PublicationDTO updateDeliveryStatus(Long id, Publication.DeliveryStatus status);

    // Notification operations
    PublicationDTO schedulePublication(PublicationDTO publicationDTO);
    void sendImmediateNotification(PublicationDTO publicationDTO);
    void resendFailedNotification(Long id);

    // Batch operations
    void markAllAsRead(Long ownerId);
    void deleteExpiredPublications(LocalDateTime expiryDate);
    void sendBatchNotifications(List<PublicationDTO> publications);

    // Statistics and metrics
    long countUnreadPublications(Long ownerId);
    Map<Publication.NotificationType, Long> getPublicationTypeDistribution(LocalDateTime start, LocalDateTime end);
    Map<Publication.DeliveryStatus, Long> getDeliveryStatusDistribution(LocalDateTime start, LocalDateTime end);
    double getDeliverySuccessRate(LocalDateTime start, LocalDateTime end);

    // Template operations
    PublicationDTO createFromTemplate(String templateId, Map<String, Object> parameters);
    void saveTemplate(String templateId, String content);
    String getTemplate(String templateId);

    // Channel-specific operations
    void configureNotificationChannels(Long ownerId, Map<String, Boolean> channelPreferences);
    Map<String, Boolean> getNotificationChannelPreferences(Long ownerId);
    void updateNotificationChannels(PublicationDTO publicationDTO, Map<String, Boolean> enabledChannels);

    // Reporting
    Map<String, Object> generateDeliveryReport(LocalDateTime start, LocalDateTime end);
    List<Map<String, Object>> getNotificationHistory(Long ownerId, LocalDateTime start, LocalDateTime end);
    Map<String, Long> getEngagementMetrics(Long ownerId);
}