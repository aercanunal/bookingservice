package org.justlife.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.justlife.booking.model.CleaningProfessionalDTO;
import org.justlife.booking.service.CleaningProfessionalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/cleaning-professionals")
@RequiredArgsConstructor
public class CleaningProfessionalRestController {

    private final CleaningProfessionalService professionalService;

    @GetMapping
    @Operation(summary = "Get all cleaning professionals")
    public List<CleaningProfessionalDTO> getAllCleaningProfessionals() {
        return professionalService.getAllCleaningProfessionals();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a cleaning professional by ID")
    public CleaningProfessionalDTO getProfessionalById(@PathVariable Long id) {
        return professionalService.getProfessionalById(id);
    }

    @PostMapping
    @Operation(summary = "Save a new cleaning professional")
    @ApiResponse(responseCode = "201", description = "Cleaning professional created successfully")
    public ResponseEntity<Void> saveProfessional(@RequestBody CleaningProfessionalDTO professionalDTO) {
        professionalService.saveProfessional(professionalDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a cleaning professional by ID")
    @ApiResponse(responseCode = "200", description = "Cleaning professional updated successfully")
    public ResponseEntity<Void> updateProfessional(@PathVariable Long id, @RequestBody CleaningProfessionalDTO professionalDTO) {
        professionalService.updateProfessional(id, professionalDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a cleaning professional by ID")
    @ApiResponse(responseCode = "204", description = "Cleaning professional deleted successfully")
    public ResponseEntity<Void> deleteProfessional(@PathVariable Long id) {
        professionalService.deleteProfessional(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

