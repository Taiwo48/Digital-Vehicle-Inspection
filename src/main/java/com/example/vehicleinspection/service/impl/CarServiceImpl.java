package com.example.vehicleinspection.service.impl;

import com.example.vehicleinspection.model.Car;
import com.example.vehicleinspection.model.VehicleOwner;
import com.example.vehicleinspection.dto.CarDTO;
import com.example.vehicleinspection.repository.CarRepository;
import com.example.vehicleinspection.repository.VehicleOwnerRepository;
import com.example.vehicleinspection.service.CarService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarServiceImpl extends BaseServiceImpl<Car, Long> implements CarService {

    private final CarRepository carRepository;
    private final VehicleOwnerRepository vehicleOwnerRepository;

    public CarServiceImpl(CarRepository carRepository, VehicleOwnerRepository vehicleOwnerRepository) {
        super(carRepository);
        this.carRepository = carRepository;
        this.vehicleOwnerRepository = vehicleOwnerRepository;
    }

    @Override
    public CarDTO createCar(CarDTO carDTO) {
        VehicleOwner owner = vehicleOwnerRepository.findById(carDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + carDTO.getOwnerId()));

        Car car = new Car();
        BeanUtils.copyProperties(carDTO, car);
        car.setOwner(owner);
        car = carRepository.save(car);
        return convertToDTO(car);
    }

    @Override
    public CarDTO updateCar(Long id, CarDTO carDTO) {
        return carRepository.findById(id)
                .map(car -> {
                    if (!car.getOwner().getId().equals(carDTO.getOwnerId())) {
                        VehicleOwner newOwner = vehicleOwnerRepository.findById(carDTO.getOwnerId())
                                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + carDTO.getOwnerId()));
                        car.setOwner(newOwner);
                    }
                    BeanUtils.copyProperties(carDTO, car, "id", "owner");
                    car = carRepository.save(car);
                    return convertToDTO(car);
                })
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    @Override
    public Optional<CarDTO> findByLicensePlate(String licensePlate) {
        return carRepository.findByLicensePlate(licensePlate)
                .map(this::convertToDTO);
    }

    @Override
    public List<CarDTO> findByOwner(Long ownerId) {
        VehicleOwner owner = vehicleOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + ownerId));
        return carRepository.findByOwner(owner).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDTO> findAllCars() {
        return carRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByLicensePlate(String licensePlate) {
        return carRepository.existsByLicensePlate(licensePlate);
    }

    @Override
    public List<CarDTO> findCarsNeedingInspection() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return carRepository.findByLastInspectionDateBefore(oneYearAgo).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDTO> findCarsWithExpiredInsurance() {
        return carRepository.findByInsuranceExpiryDateBefore(LocalDate.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CarDTO updateInspectionStatus(Long id, String status, LocalDate inspectionDate) {
        return carRepository.findById(id)
                .map(car -> {
                    car.setLastInspectionStatus(status);
                    car.setLastInspectionDate(inspectionDate);
                    car = carRepository.save(car);
                    return convertToDTO(car);
                })
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    @Override
    public CarDTO updateInsurance(Long id, String provider, String policyNumber, LocalDate expiryDate) {
        return carRepository.findById(id)
                .map(car -> {
                    car.setInsuranceProvider(provider);
                    car.setInsurancePolicyNumber(policyNumber);
                    car.setInsuranceExpiryDate(expiryDate);
                    car = carRepository.save(car);
                    return convertToDTO(car);
                })
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    @Override
    public boolean isInspectionDue(Long id) {
        return carRepository.findById(id)
                .map(car -> car.getLastInspectionDate() == null ||
                        car.getLastInspectionDate().isBefore(LocalDate.now().minusYears(1)))
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    @Override
    public boolean isInsuranceValid(Long id) {
        return carRepository.findById(id)
                .map(car -> car.getInsuranceExpiryDate() != null &&
                        car.getInsuranceExpiryDate().isAfter(LocalDate.now()))
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    @Override
    public List<CarDTO> findCarsByMakeAndModel(String make, String model) {
        return carRepository.findAll().stream()
                .filter(car -> car.getMake().equalsIgnoreCase(make) &&
                        car.getModel().equalsIgnoreCase(model))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDTO> findCarsByYear(Integer year) {
        return carRepository.findAll().stream()
                .filter(car -> car.getYear().equals(year))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CarDTO convertToDTO(Car car) {
        CarDTO dto = new CarDTO();
        BeanUtils.copyProperties(car, dto);
        dto.setOwnerId(car.getOwner().getId());
        dto.setOwnerName(car.getOwner().getFirstName() + " " + car.getOwner().getLastName());
        dto.setInsuranceValid(isInsuranceValid(car.getId()));
        dto.setInspectionDue(isInspectionDue(car.getId()));
        return dto;
    }
}