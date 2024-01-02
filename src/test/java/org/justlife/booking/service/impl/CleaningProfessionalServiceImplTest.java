package org.justlife.booking.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.justlife.booking.entity.CleaningProfessional;
import org.justlife.booking.model.CleaningProfessionalDTO;
import org.justlife.booking.repository.CleaningProfessionalRepository;
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

class CleaningProfessionalServiceImplTest {

    @Mock
    private CleaningProfessionalRepository professionalRepository;

    @InjectMocks
    private CleaningProfessionalServiceImpl professionalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCleaningProfessionals_shouldReturnListOfProfessionals() {
        // Arrange
        CleaningProfessional professional = new CleaningProfessional();
        professional.setId(1L);
        professional.setName("John Doe");

        when(professionalRepository.findAll()).thenReturn(Collections.singletonList(professional));

        // Act
        List<CleaningProfessionalDTO> result = professionalService.getAllCleaningProfessionals();

        // Assert
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void getProfessionalById_shouldReturnProfessionalById() {
        // Arrange
        CleaningProfessional professional = new CleaningProfessional();
        professional.setId(1L);
        professional.setName("John Doe");

        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));

        // Act
        CleaningProfessionalDTO result = professionalService.getProfessionalById(1L);

        // Assert
        assertEquals("John Doe", result.getName());
    }

    @Test
    void getProfessionalById_shouldThrowExceptionWhenProfessionalNotFound() {
        // Arrange
        when(professionalRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> professionalService.getProfessionalById(1L));
    }

    @Test
    void saveProfessional_shouldSaveProfessional() {
        // Arrange
        CleaningProfessionalDTO professionalDTO = new CleaningProfessionalDTO();
        professionalDTO.setId(1L);
        professionalDTO.setName("John Doe");

        // Act
        professionalService.saveProfessional(professionalDTO);

        // Assert
        verify(professionalRepository, times(1)).save(any());
    }

    @Test
    void updateProfessional_shouldUpdateProfessional() {
        // Arrange
        Long professionalId = 1L;
        CleaningProfessionalDTO updatedProfessionalDTO = new CleaningProfessionalDTO();
        updatedProfessionalDTO.setId(professionalId);
        updatedProfessionalDTO.setName("Updated Name");

        CleaningProfessional existingProfessional = new CleaningProfessional();
        existingProfessional.setId(professionalId);
        existingProfessional.setName("Original Name");

        when(professionalRepository.findById(professionalId)).thenReturn(Optional.of(existingProfessional));

        // Act
        professionalService.updateProfessional(professionalId, updatedProfessionalDTO);

        // Assert
        verify(professionalRepository, times(1)).save(any());
        assertEquals("Updated Name", existingProfessional.getName());
    }

    @Test
    void updateProfessional_shouldThrowExceptionWhenProfessionalNotFound() {
        // Arrange
        Long professionalId = 1L;
        CleaningProfessionalDTO updatedProfessionalDTO = new CleaningProfessionalDTO();
        updatedProfessionalDTO.setId(professionalId);
        updatedProfessionalDTO.setName("Updated Name");

        when(professionalRepository.findById(professionalId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> professionalService.updateProfessional(professionalId, updatedProfessionalDTO));
    }

    @Test
    void deleteProfessional_shouldDeleteProfessional() {
        // Arrange
        Long professionalId = 1L;

        // Act
        professionalService.deleteProfessional(professionalId);

        // Assert
        verify(professionalRepository, times(1)).deleteById(professionalId);
    }

}
