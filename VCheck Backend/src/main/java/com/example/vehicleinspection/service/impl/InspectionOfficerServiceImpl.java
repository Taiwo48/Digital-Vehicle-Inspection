package com.example.vehicleinspection.service.impl;

import com.example.vehicleinspection.model.InspectionOfficer;
import com.example.vehicleinspection.model.InspectionBooking;
import com.example.vehicleinspection.dto.InspectionOfficerDTO;
import com.example.vehicleinspection.repository.InspectionOfficerRepository;
import com.example.vehicleinspection.repository.InspectionBookingRepository;
import com.example.vehicleinspection.service.InspectionOfficerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InspectionOfficerServiceImpl extends BaseServiceImpl<InspectionOfficer, Long> implements InspectionOfficerService {

    private final InspectionOfficerRepository officerRepository;
    private final InspectionBookingRepository bookingRepository;

    public InspectionOfficerServiceImpl(InspectionOfficerRepository officerRepository,
                                      InspectionBookingRepository bookingRepository) {
        super(officerRepository);
        this.officerRepository = officerRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public InspectionOfficerDTO createOfficer(InspectionOfficerDTO officerDTO) {
        InspectionOfficer officer = new InspectionOfficer();
        BeanUtils.copyProperties(officerDTO, officer);
        officer.setInspectionMethods(new ArrayList<>(officerDTO.getInspectionMethods()));
        officer = officerRepository.save(officer);
        return convertToDTO(officer);
    }

    @Override
    public InspectionOfficerDTO updateOfficer(Long id, InspectionOfficerDTO officerDTO) {
        return officerRepository.findById(id)
                .map(officer -> {
                    BeanUtils.copyProperties(officerDTO, officer, "id", "inspectionMethods");
                    officer.setInspectionMethods(new ArrayList<>(officerDTO.getInspectionMethods()));
                    officer = officerRepository.save(officer);
                    return convertToDTO(officer);
                })
                .orElseThrow(() -> new RuntimeException("Officer not found with id: " + id));
    }

    @Override
    public Optional<InspectionOfficerDTO> findByBadgeNumber(String badgeNumber) {
        return officerRepository.findByBadgeNumber(badgeNumber)
                .map(this::convertToDTO);
    }

    @Override
    public List<InspectionOfficerDTO> findByDepartment(String department) {
        return officerRepository.findByDepartment(department).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InspectionOfficerDTO> findAllOfficers() {
        return officerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InspectionOfficerDTO> findAvailableOfficers() {
        return officerRepository.findByIsAvailable(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InspectionOfficerDTO> findBySpecialization(String specialization) {
        return officerRepository.findBySpecialization(specialization).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InspectionOfficerDTO> findByExperience(int minYears) {
        return officerRepository.findByYearsOfExperienceGreaterThanEqual(minYears).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InspectionOfficerDTO updateAvailability(Long id, boolean isAvailable) {
        return officerRepository.findById(id)
                .map(officer -> {
                    officer.setAvailable(isAvailable);
                    officer = officerRepository.save(officer);
                    return convertToDTO(officer);
                })
                .orElseThrow(() -> new RuntimeException("Officer not found with id: " + id));
    }

    @Override
    public InspectionOfficerDTO addInspectionMethod(Long id, String method) {
        return officerRepository.findById(id)
                .map(officer -> {
                    if (!officer.getInspectionMethods().contains(method)) {
                        officer.getInspectionMethods().add(method);
                        officer = officerRepository.save(officer);
                    }
                    return convertToDTO(officer);
                })
                .orElseThrow(() -> new RuntimeException("Officer not found with id: " + id));
    }

    @Override
    public InspectionOfficerDTO removeInspectionMethod(Long id, String method) {
        return officerRepository.findById(id)
                .map(officer -> {
                    officer.getInspectionMethods().remove(method);
                    officer = officerRepository.save(officer);
                    return convertToDTO(officer);
                })
                .orElseThrow(() -> new RuntimeException("Officer not found with id: " + id));
    }

    @Override
    public List<InspectionOfficerDTO> findOfficersAvailableForTimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        return officerRepository.findByIsAvailable(true).stream()
                .filter(officer -> isOfficerAvailableForTimeSlot(officer, startTime, endTime))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int getCompletedInspectionsCount(Long officerId) {
        return (int) bookingRepository.findByInspectionOfficer(officerRepository.findById(officerId)
                .orElseThrow(() -> new RuntimeException("Officer not found with id: " + officerId)))
                .stream()
                .filter(booking -> booking.getStatus() == InspectionBooking.InspectionStatus.COMPLETED)
                .count();
    }

    @Override
    public double getAverageInspectionRating(Long officerId) {
        // This would typically come from a rating system in the Analytics entity
        // For now, returning a placeholder value
        return 4.5;
    }

    @Override
    public List<String> getOfficerCertifications(Long officerId) {
        return officerRepository.findById(officerId)
                .map(InspectionOfficer::getInspectionMethods)
                .orElseThrow(() -> new RuntimeException("Officer not found with id: " + officerId));
    }

    private boolean isOfficerAvailableForTimeSlot(InspectionOfficer officer, LocalDateTime startTime, LocalDateTime endTime) {
        return bookingRepository.findByInspectionOfficerAndScheduledDateTimeBetween(officer, startTime, endTime)
                .isEmpty();
    }

    private InspectionOfficerDTO convertToDTO(InspectionOfficer officer) {
        InspectionOfficerDTO dto = new InspectionOfficerDTO();
        BeanUtils.copyProperties(officer, dto);
        
        // Set additional fields
        dto.setTotalInspectionsCompleted(getCompletedInspectionsCount(officer.getId()));
        dto.setAverageInspectionRating(getAverageInspectionRating(officer.getId()));
        dto.setCurrentQueueSize(officer.getInspections().size());
        dto.setCertifications(new ArrayList<>(officer.getInspectionMethods()));
        
        // Count today's inspections
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime tomorrow = today.plusDays(1);
        dto.setInspectionsToday((int) officer.getInspections().stream()
                .filter(booking -> booking.getScheduledDateTime().isAfter(today) &&
                        booking.getScheduledDateTime().isBefore(tomorrow))
                .count());
        
        return dto;
    }
}