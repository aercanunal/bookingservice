package org.justlife.booking.service.impl;

import lombok.RequiredArgsConstructor;
import org.justlife.booking.entity.CleaningProfessional;
import org.justlife.booking.mapper.CleaningServiceMapper;
import org.justlife.booking.model.CleaningProfessionalDTO;
import org.justlife.booking.repository.CleaningProfessionalRepository;
import org.justlife.booking.service.CleaningProfessionalService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CleaningProfessionalServiceImpl implements CleaningProfessionalService {

    private final CleaningProfessionalRepository professionalRepository;

    @Cacheable(value = "cleaningProfessionals")
    @Override
    public List<CleaningProfessionalDTO> getAllCleaningProfessionals() {
        List<CleaningProfessional> professionals = professionalRepository.findAll();
        return professionals.stream()
                .map(CleaningServiceMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "cleaningProfessionals", key = "#id")
    @Override
    public CleaningProfessionalDTO getProfessionalById(Long id) {
        CleaningProfessional professional = professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cleaning professional not found"));
        return CleaningServiceMapper.INSTANCE.toDTO(professional);
    }

    @CacheEvict(value = "cleaningProfessionals", allEntries = true)
    @Override
    public void saveProfessional(CleaningProfessionalDTO professionalDTO) {
        CleaningProfessional professional = CleaningServiceMapper.INSTANCE.toEntity(professionalDTO);
        professionalRepository.save(professional);
    }

    @CacheEvict(value = "cleaningProfessionals", key = "#id")
    @Override
    public void updateProfessional(Long id, CleaningProfessionalDTO professionalDTO) {
        CleaningProfessional existingProfessional = professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cleaning professional not found"));

        professionalRepository.save(existingProfessional);
    }

    @CacheEvict(value = "cleaningProfessionals", key = "#id")
    @Override
    public void deleteProfessional(Long id) {
        professionalRepository.deleteById(id);
    }
}
