package com.example.vehicleinspection.service.impl;

import com.example.vehicleinspection.model.InspectionBooking;
import com.example.vehicleinspection.model.VehicleOwner;
import com.example.vehicleinspection.model.Car;
import com.example.vehicleinspection.model.InspectionOfficer;
import com.example.vehicleinspection.dto.InspectionBookingDTO;
import com.example.vehicleinspection.repository.*;
import com.example.vehicleinspection.service.InspectionBookingService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InspectionBookingServiceImpl extends BaseServiceImpl<InspectionBooking, Long> implements InspectionBookingService {

    private final InspectionBookingRepository bookingRepository;
    private final VehicleOwnerRepository ownerRepository;
    private final CarRepository carRepository;
    private final InspectionOfficerRepository officerRepository;

    public InspectionBookingServiceImpl(InspectionBookingRepository bookingRepository,
                                       VehicleOwnerRepository ownerRepository,
                                       CarRepository carRepository,
                                       InspectionOfficerRepository officerRepository) {
        super(bookingRepository);
        this.bookingRepository = bookingRepository;
        this.ownerRepository = ownerRepository;
        this.carRepository = carRepository;
        this.officerRepository = officerRepository;
    }

    @Override
    public InspectionBookingDTO createBooking(InspectionBookingDTO bookingDTO) {
        VehicleOwner owner = ownerRepository.findById(bookingDTO.getVehicleOwnerId())
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
        Car car = carRepository.findById(bookingDTO.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        InspectionBooking booking = new InspectionBooking();
        BeanUtils.copyProperties(bookingDTO, booking);
        booking.setVehicleOwner(owner);
        booking.setCar(car);
        booking.setStatus(InspectionBooking.InspectionStatus.SCHEDULED);

        if (bookingDTO.getInspectionOfficerId() != null) {
            InspectionOfficer officer = officerRepository.findById(bookingDTO.getInspectionOfficerId())
                    .orElseThrow(() -> new RuntimeException("Officer not found"));
            if (!isTimeSlotAvailable(officer.getId(), bookingDTO.getScheduledDateTime())) {
                throw new RuntimeException("Time slot is not available for the selected officer");
            }
            booking.setInspectionOfficer(officer);
        }

        booking = bookingRepository.save(booking);
        return convertToDTO(booking);
    }

    @Override
    public InspectionBookingDTO updateBooking(Long id, InspectionBookingDTO bookingDTO) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    if (bookingDTO.getVehicleOwnerId() != null) {
                        VehicleOwner owner = ownerRepository.findById(bookingDTO.getVehicleOwnerId())
                                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
                        booking.setVehicleOwner(owner);
                    }
                    if (bookingDTO.getCarId() != null) {
                        Car car = carRepository.findById(bookingDTO.getCarId())
                                .orElseThrow(() -> new RuntimeException("Car not found"));
                        booking.setCar(car);
                    }
                    BeanUtils.copyProperties(bookingDTO, booking, "id", "vehicleOwner", "car", "inspectionOfficer");
                    booking = bookingRepository.save(booking);
                    return convertToDTO(booking);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public InspectionBookingDTO assignOfficer(Long bookingId, Long officerId) {
        InspectionBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        InspectionOfficer officer = officerRepository.findById(officerId)
                .orElseThrow(() -> new RuntimeException("Officer not found"));

        if (!isTimeSlotAvailable(officerId, booking.getScheduledDateTime())) {
            throw new RuntimeException("Time slot is not available for the selected officer");
        }

        booking.setInspectionOfficer(officer);
        booking = bookingRepository.save(booking);
        return convertToDTO(booking);
    }

    @Override
    public InspectionBookingDTO updateStatus(Long id, InspectionBooking.InspectionStatus status) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(status);
                    if (status == InspectionBooking.InspectionStatus.COMPLETED) {
                        booking.setCompletedDateTime(LocalDateTime.now());
                    }
                    booking = bookingRepository.save(booking);
                    return convertToDTO(booking);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<InspectionBookingDTO> findByVehicleOwner(Long ownerId) {
        VehicleOwner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
        return bookingRepository.findByVehicleOwner(owner).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InspectionBookingDTO> findByInspectionOfficer(Long officerId) {
        InspectionOfficer officer = officerRepository.findById(officerId)
                .orElseThrow(() -> new RuntimeException("Officer not found"));
        return bookingRepository.findByInspectionOfficer(officer).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InspectionBookingDTO> findByStatus(InspectionBooking.InspectionStatus status) {
        return bookingRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InspectionBookingDTO> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findByScheduledDateTimeBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InspectionBookingDTO> findByOfficerAndDateRange(Long officerId, LocalDateTime start, LocalDateTime end) {
        InspectionOfficer officer = officerRepository.findById(officerId)
                .orElseThrow(() -> new RuntimeException("Officer not found"));
        return bookingRepository.findByInspectionOfficerAndScheduledDateTimeBetween(officer, start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InspectionBookingDTO> findByOwnerAndStatus(Long ownerId, InspectionBooking.InspectionStatus status) {
        VehicleOwner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found"));
        return bookingRepository.findByVehicleOwnerAndStatus(owner, status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InspectionBookingDTO completeInspection(Long id, String result, String recommendations) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(InspectionBooking.InspectionStatus.COMPLETED);
                    booking.setCompletedDateTime(LocalDateTime.now());
                    booking.setResult(result);
                    booking.setRecommendations(recommendations);
                    booking = bookingRepository.save(booking);
                    return convertToDTO(booking);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public InspectionBookingDTO rescheduleBooking(Long id, LocalDateTime newDateTime) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    if (booking.getInspectionOfficer() != null &&
                            !isTimeSlotAvailable(booking.getInspectionOfficer().getId(), newDateTime)) {
                        throw new RuntimeException("New time slot is not available for the assigned officer");
                    }
                    booking.setScheduledDateTime(newDateTime);
                    booking.setStatus(InspectionBooking.InspectionStatus.RESCHEDULED);
                    booking = bookingRepository.save(booking);
                    return convertToDTO(booking);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public void cancelBooking(Long id) {
        bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(InspectionBooking.InspectionStatus.CANCELLED);
                    return bookingRepository.save(booking);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public boolean isTimeSlotAvailable(Long officerId, LocalDateTime dateTime) {
        InspectionOfficer officer = officerRepository.findById(officerId)
                .orElseThrow(() -> new RuntimeException("Officer not found"));
        LocalDateTime slotStart = dateTime.minusHours(1);
        LocalDateTime slotEnd = dateTime.plusHours(1);
        return bookingRepository.findByInspectionOfficerAndScheduledDateTimeBetween(officer, slotStart, slotEnd).isEmpty();
    }

    @Override
    public List<LocalDateTime> getAvailableTimeSlots(Long officerId, LocalDateTime date) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0); // Start at 9 AM
        LocalTime endTime = LocalTime.of(17, 0);  // End at 5 PM

        while (startTime.isBefore(endTime)) {
            LocalDateTime timeSlot = date.with(startTime);
            if (isTimeSlotAvailable(officerId, timeSlot)) {
                availableSlots.add(timeSlot);
            }
            startTime = startTime.plusHours(1);
        }
        return availableSlots;
    }

    @Override
    public long getCompletedInspectionsCount(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.countByStatusAndScheduledDateTimeBetween(
                InspectionBooking.InspectionStatus.COMPLETED, start, end);
    }

    @Override
    public double getAverageInspectionDuration() {
        // This would typically calculate the average duration between scheduled and completed times
        // For now, returning a placeholder value in hours
        return 1.5;
    }

    private InspectionBookingDTO convertToDTO(InspectionBooking booking) {
        InspectionBookingDTO dto = new InspectionBookingDTO();
        BeanUtils.copyProperties(booking, dto);

        dto.setVehicleOwnerId(booking.getVehicleOwner().getId());
        dto.setCarId(booking.getCar().getId());
        if (booking.getInspectionOfficer() != null) {
            dto.setInspectionOfficerId(booking.getInspectionOfficer().getId());
        }

        // Set additional fields
        dto.setVehicleOwnerName(booking.getVehicleOwner().getFirstName() + " " + 
                               booking.getVehicleOwner().getLastName());
        dto.setCarLicensePlate(booking.getCar().getLicensePlate());
        if (booking.getInspectionOfficer() != null) {
            dto.setOfficerName(booking.getInspectionOfficer().getFirstName() + " " + 
                              booking.getInspectionOfficer().getLastName());
            dto.setOfficerBadgeNumber(booking.getInspectionOfficer().getBadgeNumber());
        }

        dto.setEstimatedDuration(90L); // 90 minutes estimated duration
        dto.setRescheduled(booking.getStatus() == InspectionBooking.InspectionStatus.RESCHEDULED);
        
        return dto;
    }
}