package org.justlife.booking.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for creating a booking request")
public class BookingRequestDTO {

    @Schema(description = "Booking ID")
    private Long id;

    @Schema(description = "Start date of the booking", example = "2024-01-01")
    @NotNull
    private LocalDate startDate;

    @Schema(description = "Start time of the booking", example = "10:00")
    @NotNull
    private LocalTime startTime;

    @Schema(description = "Duration of the booking", example = "TWO_HOURS", enumAsRef = true)
    @NotNull
    private Duration duration;

    @Schema(description = "List of professional IDs", example = "[1, 2, 3]")
    @NotNull(message = "ProfessionalIds cannot be null")
    @NotEmpty(message = "ProfessionalIds cannot be empty")
    private List<Long> professionals;

    // Getter and Setter methods...
}