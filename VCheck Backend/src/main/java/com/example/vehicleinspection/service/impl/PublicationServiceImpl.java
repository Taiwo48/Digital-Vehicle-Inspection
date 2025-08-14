package com.example.vehicleinspection.service.impl;

import com.example.vehicleinspection.model.Publication;
import com.example.vehicleinspection.model.VehicleOwner;
import com.example.vehicleinspection.dto.PublicationDTO;
import com.example.vehicleinspection.repository.PublicationRepository;
import com.example.vehicleinspection.repository.VehicleOwnerRepository;
import com.example.vehicleinspection.service.PublicationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PublicationServiceImpl extends BaseServiceImpl<Publication, Long> implements PublicationService {

    private final PublicationRepository publicationRepository;
    private final VehicleOwnerRepository vehicleOwnerRepository;
    private final Map<String, String> templateStore = new HashMap<>();

    public PublicationServiceImpl(PublicationRepository publicationRepository,
                                 VehicleOwnerRepository vehicleOwnerRepository) {
        super(publicationRepository);
        this.publicationRepository = publicationRepository;
        this.vehicleOwnerRepository = vehicleOwnerRepository;
    }

    @Override
    public PublicationDTO createPublication(PublicationDTO publicationDTO) {
        VehicleOwner owner = vehicleOwnerRepository.findById(publicationDTO.getVehicleOwnerId())
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));

        Publication publication = new Publication();
        BeanUtils.copyProperties(publicationDTO, publication);
        publication.setVehicleOwner(owner);
        publication.setCreationDate(LocalDateTime.now());
        publication.setDeliveryStatus(Publication.DeliveryStatus.PENDING);

        publication = publicationRepository.save(publication);
        return convertToDTO(publication);
    }

    @Override
    public PublicationDTO updatePublication(Long id, PublicationDTO publicationDTO) {
        return publicationRepository.findById(id)
                .map(publication -> {
                    if (publicationDTO.getVehicleOwnerId() != null && 
                        !publicationDTO.getVehicleOwnerId().equals(publication.getVehicleOwner().getId())) {
                        VehicleOwner newOwner = vehicleOwnerRepository.findById(publicationDTO.getVehicleOwnerId())
                                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
                        publication.setVehicleOwner(newOwner);
                    }
                    BeanUtils.copyProperties(publicationDTO, publication, "id", "vehicleOwner", "creationDate");
                    publication = publicationRepository.save(publication);
                    return convertToDTO(publication);
                })
                .orElseThrow(() -> new RuntimeException("Publication not found"));
    }

    @Override
    public void deletePublication(Long id) {
        publicationRepository.deleteById(id);
    }

    @Override
    public List<PublicationDTO> findByVehicleOwner(Long ownerId) {
        VehicleOwner owner = vehicleOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
        return publicationRepository.findByVehicleOwner(owner).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicationDTO> findByType(Publication.NotificationType type) {
        return publicationRepository.findByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicationDTO> findByReadStatus(boolean read) {
        return publicationRepository.findByRead(read).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicationDTO> findByScheduledDate(LocalDateTime date) {
        return publicationRepository.findByScheduledDate(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicationDTO> findByPriority(Publication.Priority priority) {
        return publicationRepository.findByPriority(priority).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicationDTO> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return publicationRepository.findByScheduledDateBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicationDTO> findByOwnerAndType(Long ownerId, Publication.NotificationType type) {
        VehicleOwner owner = vehicleOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
        return publicationRepository.findByVehicleOwnerAndType(owner, type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicationDTO> findByOwnerAndReadStatus(Long ownerId, boolean read) {
        VehicleOwner owner = vehicleOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
        return publicationRepository.findByVehicleOwnerAndRead(owner, read).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicationDTO> findByTypeAndPriority(Publication.NotificationType type, Publication.Priority priority) {
        return publicationRepository.findByTypeAndPriority(type, priority).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PublicationDTO markAsRead(Long id) {
        return publicationRepository.findById(id)
                .map(publication -> {
                    publication.setRead(true);
                    publication.setReadDate(LocalDateTime.now());
                    publication = publicationRepository.save(publication);
                    return convertToDTO(publication);
                })
                .orElseThrow(() -> new RuntimeException("Publication not found"));
    }

    @Override
    public PublicationDTO markAsUnread(Long id) {
        return publicationRepository.findById(id)
                .map(publication -> {
                    publication.setRead(false);
                    publication.setReadDate(null);
                    publication = publicationRepository.save(publication);
                    return convertToDTO(publication);
                })
                .orElseThrow(() -> new RuntimeException("Publication not found"));
    }

    @Override
    public PublicationDTO updateDeliveryStatus(Long id, Publication.DeliveryStatus status) {
        return publicationRepository.findById(id)
                .map(publication -> {
                    publication.setDeliveryStatus(status);
                    if (status == Publication.DeliveryStatus.DELIVERED) {
                        publication.setDeliveryDate(LocalDateTime.now());
                    }
                    publication = publicationRepository.save(publication);
                    return convertToDTO(publication);
                })
                .orElseThrow(() -> new RuntimeException("Publication not found"));
    }

    @Override
    public PublicationDTO schedulePublication(PublicationDTO publicationDTO) {
        publicationDTO.setScheduled(true);
        return createPublication(publicationDTO);
    }

    @Override
    public void sendImmediateNotification(PublicationDTO publicationDTO) {
        publicationDTO.setScheduled(false);
        publicationDTO.setScheduledDate(LocalDateTime.now());
        PublicationDTO savedPublication = createPublication(publicationDTO);
        // Implement actual notification sending logic here
        updateDeliveryStatus(savedPublication.getId(), Publication.DeliveryStatus.DELIVERED);
    }

    @Override
    public void resendFailedNotification(Long id) {
        publicationRepository.findById(id)
                .filter(publication -> publication.getDeliveryStatus() == Publication.DeliveryStatus.FAILED)
                .ifPresent(publication -> {
                    // Implement actual notification resending logic here
                    updateDeliveryStatus(publication.getId(), Publication.DeliveryStatus.PENDING);
                });
    }

    @Override
    public void markAllAsRead(Long ownerId) {
        VehicleOwner owner = vehicleOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
        List<Publication> unreadPublications = publicationRepository.findByVehicleOwnerAndRead(owner, false);
        unreadPublications.forEach(publication -> {
            publication.setRead(true);
            publication.setReadDate(LocalDateTime.now());
        });
        publicationRepository.saveAll(unreadPublications);
    }

    @Override
    public void deleteExpiredPublications(LocalDateTime expiryDate) {
        publicationRepository.deleteByScheduledDateBefore(expiryDate);
    }

    @Override
    public void sendBatchNotifications(List<PublicationDTO> publications) {
        publications.forEach(this::sendImmediateNotification);
    }

    @Override
    public long countUnreadPublications(Long ownerId) {
        VehicleOwner owner = vehicleOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
        return publicationRepository.countByVehicleOwnerAndRead(owner, false);
    }

    @Override
    public Map<Publication.NotificationType, Long> getPublicationTypeDistribution(LocalDateTime start, LocalDateTime end) {
        return publicationRepository.getPublicationTypeDistribution(start, end);
    }

    @Override
    public Map<Publication.DeliveryStatus, Long> getDeliveryStatusDistribution(LocalDateTime start, LocalDateTime end) {
        return publicationRepository.getDeliveryStatusDistribution(start, end);
    }

    @Override
    public double getDeliverySuccessRate(LocalDateTime start, LocalDateTime end) {
        long totalDeliveries = publicationRepository.countByScheduledDateBetween(start, end);
        long successfulDeliveries = publicationRepository.countByDeliveryStatusAndScheduledDateBetween(
                Publication.DeliveryStatus.DELIVERED, start, end);
        return totalDeliveries > 0 ? (double) successfulDeliveries / totalDeliveries : 0.0;
    }

    @Override
    public PublicationDTO createFromTemplate(String templateId, Map<String, Object> parameters) {
        String template = getTemplate(templateId);
        if (template == null) {
            throw new RuntimeException("Template not found");
        }

        // Simple template parameter replacement
        String content = template;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setContent(content);
        return publicationDTO;
    }

    @Override
    public void saveTemplate(String templateId, String content) {
        templateStore.put(templateId, content);
    }

    @Override
    public String getTemplate(String templateId) {
        return templateStore.get(templateId);
    }

    @Override
    public void configureNotificationChannels(Long ownerId, Map<String, Boolean> channelPreferences) {
        VehicleOwner owner = vehicleOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
        // Implement channel preferences storage logic here
    }

    @Override
    public Map<String, Boolean> getNotificationChannelPreferences(Long ownerId) {
        vehicleOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
        // Implement channel preferences retrieval logic here
        return new HashMap<>(); // Placeholder implementation
    }

    @Override
    public void updateNotificationChannels(PublicationDTO publicationDTO, Map<String, Boolean> enabledChannels) {
        publicationDTO.setEmailEnabled(enabledChannels.getOrDefault("email", false));
        publicationDTO.setSmsEnabled(enabledChannels.getOrDefault("sms", false));
        publicationDTO.setPushEnabled(enabledChannels.getOrDefault("push", false));
    }

    @Override
    public Map<String, Object> generateDeliveryReport(LocalDateTime start, LocalDateTime end) {
        Map<String, Object> report = new HashMap<>();
        report.put("totalPublications", publicationRepository.countByScheduledDateBetween(start, end));
        report.put("deliveryStatusDistribution", getDeliveryStatusDistribution(start, end));
        report.put("successRate", getDeliverySuccessRate(start, end));
        report.put("typeDistribution", getPublicationTypeDistribution(start, end));
        return report;
    }

    @Override
    public List<Map<String, Object>> getNotificationHistory(Long ownerId, LocalDateTime start, LocalDateTime end) {
        VehicleOwner owner = vehicleOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
        return publicationRepository.findByVehicleOwnerAndScheduledDateBetween(owner, start, end).stream()
                .map(publication -> {
                    Map<String, Object> history = new HashMap<>();
                    history.put("id", publication.getId());
                    history.put("type", publication.getType());
                    history.put("scheduledDate", publication.getScheduledDate());
                    history.put("deliveryStatus", publication.getDeliveryStatus());
                    history.put("deliveryDate", publication.getDeliveryDate());
                    history.put("read", publication.isRead());
                    history.put("readDate", publication.getReadDate());
                    return history;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getEngagementMetrics(Long ownerId) {
        VehicleOwner owner = vehicleOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
        
        Map<String, Long> metrics = new HashMap<>();
        metrics.put("totalNotifications", publicationRepository.countByVehicleOwner(owner));
        metrics.put("readNotifications", publicationRepository.countByVehicleOwnerAndRead(owner, true));
        metrics.put("unreadNotifications", publicationRepository.countByVehicleOwnerAndRead(owner, false));
        metrics.put("deliveredNotifications", publicationRepository.countByVehicleOwnerAndDeliveryStatus(
                owner, Publication.DeliveryStatus.DELIVERED));
        
        return metrics;
    }

    private PublicationDTO convertToDTO(Publication publication) {
        PublicationDTO dto = new PublicationDTO();
        BeanUtils.copyProperties(publication, dto);
        dto.setVehicleOwnerId(publication.getVehicleOwner().getId());

        // Set additional fields
        dto.setRecipientName(publication.getVehicleOwner().getFirstName() + " " + 
                            publication.getVehicleOwner().getLastName());
        dto.setRecipientEmail(publication.getVehicleOwner().getEmail());
        dto.setRecipientPhone(publication.getVehicleOwner().getPhone());
        
        if (publication.getDeliveryStatus() == Publication.DeliveryStatus.FAILED) {
            dto.setDeliveryAttempts(publicationRepository.countDeliveryAttempts(publication.getId()));
        }

        return dto;
    }
}