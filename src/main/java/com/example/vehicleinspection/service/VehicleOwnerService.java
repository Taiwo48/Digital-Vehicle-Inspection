package com.example.vehicleinspection.service;

import com.example.vehicleinspection.model.VehicleOwner;
import com.example.vehicleinspection.dto.VehicleOwnerDTO;

import java.util.List;
import java.util.Optional;

public interface VehicleOwnerService extends BaseService<VehicleOwner, Long> {
    
    VehicleOwnerDTO createVehicleOwner(VehicleOwnerDTO ownerDTO);
    
    VehicleOwnerDTO updateVehicleOwner(Long id, VehicleOwnerDTO ownerDTO);
    
    Optional<VehicleOwnerDTO> findByDriverLicense(String driverLicense);
    
    Optional<VehicleOwnerDTO> findByEmail(String email);
    
    List<VehicleOwnerDTO> findAllVehicleOwners();
    
    void deleteVehicleOwner(Long id);
    
    boolean existsByDriverLicense(String driverLicense);
    
    List<VehicleOwnerDTO> findOwnersWithPendingInspections();
    
    List<VehicleOwnerDTO> findOwnersWithExpiredInsurance();
    
    VehicleOwnerDTO addVehicleToOwner(Long ownerId, Long vehicleId);
    
    VehicleOwnerDTO removeVehicleFromOwner(Long ownerId, Long vehicleId);
    
    int getVehicleCount(Long ownerId);
    
    int getPendingInspectionsCount(Long ownerId);
}