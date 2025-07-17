package com.example.vehicleinspection.dto;

import com.example.vehicleinspection.model.Publication.NotificationType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class PublicationDTO extends BaseDTO {

    private Long vehicleOwnerId;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime scheduledFor;
    private LocalDateTime sentAt;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Priority is required")
    private String priority;

    private boolean isRead;

    // Notification channels
    private boolean sendEmail;
    private boolean sendSMS;
    private boolean sendPushNotification;

    // Additional fields for response
    private String recipientName;
    private String recipientEmail;
    private String recipientPhone;
    private int deliveryAttempts;
    private String deliveryStatus;
    private LocalDateTime lastDeliveryAttempt;
    private String notificationTemplate;
    private boolean requiresAcknowledgment;
    private LocalDateTime acknowledgedAt;
}