package org.justlife.booking.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for representing a cleaning professional")
public class CleaningProfessionalDTO {

    @Schema(description = "Unique identifier for the cleaning professional", example = "1")
    private Long id;

    @Schema(description = "Name of the cleaning professional", example = "John Doe")
    private String name;

}
