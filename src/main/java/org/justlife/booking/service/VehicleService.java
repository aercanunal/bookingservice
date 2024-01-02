package org.justlife.booking.service;

import org.justlife.booking.model.VehicleDTO;

import java.util.List;

public interface VehicleService {
    List<VehicleDTO> getAllVehicles();

    VehicleDTO getVehicleById(Long id);

    void saveVehicle(VehicleDTO vehicleDTO);

    void updateVehicle(Long id, VehicleDTO vehicleDTO);

    void deleteVehicle(Long id);
}
