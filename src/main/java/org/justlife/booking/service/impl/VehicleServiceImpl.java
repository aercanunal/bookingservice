package org.justlife.booking.service.impl;

import lombok.RequiredArgsConstructor;
import org.justlife.booking.entity.Vehicle;
import org.justlife.booking.mapper.CleaningServiceMapper;
import org.justlife.booking.model.VehicleDTO;
import org.justlife.booking.model.base.BusinessException;
import org.justlife.booking.repository.VehicleRepository;
import org.justlife.booking.service.VehicleService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Cacheable(value = "vehicles")
    @Override
    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .map(CleaningServiceMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "vehicles", key = "#id")
    @Override
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Vehicle not found"));
        return CleaningServiceMapper.INSTANCE.toDTO(vehicle);
    }

    @CacheEvict(value = "vehicles", allEntries = true)
    @Override
    public void saveVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = CleaningServiceMapper.INSTANCE.toEntity(vehicleDTO);
        vehicleRepository.save(vehicle);
    }

    @CacheEvict(value = "vehicles", key = "#id")
    @Override
    public void updateVehicle(Long id, VehicleDTO vehicleDTO) {
        vehicleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Vehicle not found"));

        Vehicle entity = CleaningServiceMapper.INSTANCE.toEntity(vehicleDTO);

        vehicleRepository.save(entity);
    }

    @CacheEvict(value = "vehicles", key = "#id")
    @Override
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
