package org.justlife.booking.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// CleaningServiceBookingDTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for representing a cleaning service booking")
public class CleaningServiceBookingDTO {

    @Schema(description = "Unique identifier for the cleaning service booking", example = "1")
    private Long id;

    @Schema(description = "Start date of the booking", example = "2024-01-01")
    @NotNull
    private LocalDate startDate;

    @Schema(description = "Start time of the booking", example = "10:00")
    @NotNull
    private LocalTime startTime;

    @Schema(description = "Duration of the cleaning service booking", example = "TWO_HOURS")
    private Duration duration;

    @Schema(description = "List of cleaning professionals involved in the service booking")
    private List<CleaningProfessionalDTO> professionals;

    // Getter and Setter methods...

}