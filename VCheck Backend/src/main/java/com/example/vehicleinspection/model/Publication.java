package com.example.vehicleinspection.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_owner_id")
    private VehicleOwner vehicleOwner;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime scheduledFor;
    private LocalDateTime sentAt;

    private String status;
    private String priority;
    private boolean isRead;

    // Notification channels
    private boolean sendEmail;
    private boolean sendSMS;
    private boolean sendPushNotification;

    public enum NotificationType {
        INSPECTION_REMINDER,
        INSPECTION_RESULT,
        MAINTENANCE_ALERT,
        DOCUMENT_EXPIRY,
        SYSTEM_UPDATE,
        GENERAL_ANNOUNCEMENT
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}