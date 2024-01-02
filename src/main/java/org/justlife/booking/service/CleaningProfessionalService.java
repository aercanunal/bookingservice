package org.justlife.booking.service;

import org.justlife.booking.model.CleaningProfessionalDTO;

import java.util.List;

public interface CleaningProfessionalService {
    List<CleaningProfessionalDTO> getAllCleaningProfessionals();

    CleaningProfessionalDTO getProfessionalById(Long id);

    void saveProfessional(CleaningProfessionalDTO professionalDTO);

    void updateProfessional(Long id, CleaningProfessionalDTO professionalDTO);

    void deleteProfessional(Long id);
}
