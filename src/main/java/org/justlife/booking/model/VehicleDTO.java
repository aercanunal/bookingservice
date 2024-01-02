package org.justlife.booking.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for representing a vehicle")
public class VehicleDTO {

    @Schema(description = "Unique identifier for the vehicle", example = "1")
    private Long id;

    @Schema(description = "Plate number of the vehicle", example = "ABC123")
    private String plateNumber;

    @Schema(description = "Model of the vehicle", example = "Sedan")
    private String model;

    @Schema(description = "List of cleaning professionals associated with the vehicle")
    private List<CleaningProfessionalDTO> professionals;

}