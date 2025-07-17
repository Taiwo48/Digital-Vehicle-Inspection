package com.example.vehicleinspection.service.impl;

import com.example.vehicleinspection.model.VehicleOwner;
import com.example.vehicleinspection.model.Car;
import com.example.vehicleinspection.dto.VehicleOwnerDTO;
import com.example.vehicleinspection.repository.VehicleOwnerRepository;
import com.example.vehicleinspection.repository.CarRepository;
import com.example.vehicleinspection.service.VehicleOwnerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleOwnerServiceImpl extends BaseServiceImpl<VehicleOwner, Long> implements VehicleOwnerService {

    private final VehicleOwnerRepository vehicleOwnerRepository;
    private final CarRepository carRepository;

    public VehicleOwnerServiceImpl(VehicleOwnerRepository vehicleOwnerRepository, CarRepository carRepository) {
        super(vehicleOwnerRepository);
        this.vehicleOwnerRepository = vehicleOwnerRepository;
        this.carRepository = carRepository;
    }

    @Override
    public VehicleOwnerDTO createVehicleOwner(VehicleOwnerDTO ownerDTO) {
        VehicleOwner owner = new VehicleOwner();
        BeanUtils.copyProperties(ownerDTO, owner);
        owner = vehicleOwnerRepository.save(owner);
        return convertToDTO(owner);
    }

    @Override
    public VehicleOwnerDTO updateVehicleOwner(Long id, VehicleOwnerDTO ownerDTO) {
        return vehicleOwnerRepository.findById(id)
                .map(owner -> {
                    BeanUtils.copyProperties(ownerDTO, owner, "id");
                    owner = vehicleOwnerRepository.save(owner);
                    return convertToDTO(owner);
                })
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found with id: " + id));
    }

    @Override
    public Optional<VehicleOwnerDTO> findByDriverLicense(String driverLicense) {
        return vehicleOwnerRepository.findByDriverLicense(driverLicense)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<VehicleOwnerDTO> findByEmail(String email) {
        return vehicleOwnerRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    @Override
    public List<VehicleOwnerDTO> findAllVehicleOwners() {
        return vehicleOwnerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByDriverLicense(String driverLicense) {
        return vehicleOwnerRepository.existsByDriverLicense(driverLicense);
    }

    @Override
    public List<VehicleOwnerDTO> findOwnersWithPendingInspections() {
        // Implementation would require a query to find owners with vehicles that need inspection
        return vehicleOwnerRepository.findAll().stream()
                .filter(owner -> owner.getVehicles().stream()
                        .anyMatch(car -> car.getLastInspectionDate() == null ||
                                car.getLastInspectionDate().isBefore(LocalDate.now().minusMonths(12))))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleOwnerDTO> findOwnersWithExpiredInsurance() {
        return vehicleOwnerRepository.findAll().stream()
                .filter(owner -> owner.getVehicles().stream()
                        .anyMatch(car -> car.getInsuranceExpiryDate() != null &&
                                car.getInsuranceExpiryDate().isBefore(LocalDate.now())))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleOwnerDTO addVehicleToOwner(Long ownerId, Long vehicleId) {
        VehicleOwner owner = vehicleOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found with id: " + ownerId));
        Car car = carRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + vehicleId));

        car.setOwner(owner);
        owner.getVehicles().add(car);
        owner = vehicleOwnerRepository.save(owner);
        return convertToDTO(owner);
    }

    @Override
    public VehicleOwnerDTO removeVehicleFromOwner(Long ownerId, Long vehicleId) {
        VehicleOwner owner = vehicleOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle owner not found with id: " + ownerId));
        
        owner.getVehicles().removeIf(car -> car.getId().equals(vehicleId));
        owner = vehicleOwnerRepository.save(owner);
        return convertToDTO(owner);
    }

    @Override
    public int getVehicleCount(Long ownerId) {
        return vehicleOwnerRepository.findById(ownerId)
                .map(owner -> owner.getVehicles().size())
                .orElse(0);
    }

    @Override
    public int getPendingInspectionsCount(Long ownerId) {
        return (int) vehicleOwnerRepository.findById(ownerId)
                .map(owner -> owner.getVehicles().stream()
                        .filter(car -> car.getLastInspectionDate() == null ||
                                car.getLastInspectionDate().isBefore(LocalDate.now().minusMonths(12)))
                        .count())
                .orElse(0L);
    }

    private VehicleOwnerDTO convertToDTO(VehicleOwner owner) {
        VehicleOwnerDTO dto = new VehicleOwnerDTO();
        BeanUtils.copyProperties(owner, dto);
        
        dto.setVehicleIds(owner.getVehicles().stream()
                .map(Car::getId)
                .collect(Collectors.toList()));
        
        dto.setTotalVehicles(owner.getVehicles().size());
        dto.setPendingInspections(getPendingInspectionsCount(owner.getId()));
        
        owner.getVehicles().stream()
                .map(Car::getLastInspectionDate)
                .filter(date -> date != null)
                .max(LocalDate::compareTo)
                .ifPresent(date -> dto.setLastInspectionDate(date.toString()));
        
        return dto;
    }
}