package org.justlife.booking.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.justlife.booking.entity.Vehicle;
import org.justlife.booking.model.VehicleDTO;
import org.justlife.booking.repository.VehicleRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllVehicles_shouldReturnListOfVehicles() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setPlateNumber("ABC123");

        when(vehicleRepository.findAll()).thenReturn(Collections.singletonList(vehicle));

        // Act
        List<VehicleDTO> result = vehicleService.getAllVehicles();

        // Assert
        assertEquals(1, result.size());
        assertEquals("ABC123", result.get(0).getPlateNumber());
    }

    @Test
    void getVehicleById_shouldReturnVehicleById() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setPlateNumber("ABC123");

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        // Act
        VehicleDTO result = vehicleService.getVehicleById(1L);

        // Assert
        assertEquals("ABC123", result.getPlateNumber());
    }

    @Test
    void getVehicleById_shouldThrowExceptionWhenVehicleNotFound() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> vehicleService.getVehicleById(1L));
    }

    @Test
    void saveVehicle_shouldSaveVehicle() {
        // Arrange
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(1L);
        vehicleDTO.setPlateNumber("ABC123");

        // Act
        vehicleService.saveVehicle(vehicleDTO);

        // Assert
        verify(vehicleRepository, times(1)).save(any());
    }

    @Test
    void updateVehicle_shouldUpdateVehicle() {
        // Arrange
        Long vehicleId = 1L;
        VehicleDTO updatedVehicleDTO = new VehicleDTO();
        updatedVehicleDTO.setId(vehicleId);
        updatedVehicleDTO.setPlateNumber("UpdatedPlate");

        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setId(vehicleId);
        existingVehicle.setPlateNumber("OriginalPlate");

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));

        // Act
        vehicleService.updateVehicle(vehicleId, updatedVehicleDTO);

        // Assert
        verify(vehicleRepository, times(1)).save(any());
        assertEquals("UpdatedPlate", existingVehicle.getPlateNumber());
    }

    @Test
    void updateVehicle_shouldThrowExceptionWhenVehicleNotFound() {
        // Arrange
        Long vehicleId = 1L;
        VehicleDTO updatedVehicleDTO = new VehicleDTO();
        updatedVehicleDTO.setId(vehicleId);
        updatedVehicleDTO.setPlateNumber("UpdatedPlate");

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> vehicleService.updateVehicle(vehicleId, updatedVehicleDTO));
    }

    @Test
    void deleteVehicle_shouldDeleteVehicle() {
        // Arrange
        Long vehicleId = 1L;

        // Act
        vehicleService.deleteVehicle(vehicleId);

        // Assert
        verify(vehicleRepository, times(1)).deleteById(vehicleId);
    }

}
