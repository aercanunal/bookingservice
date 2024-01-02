package org.justlife.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableProfessionalsTimeDTO {

    private CleaningProfessionalDTO cleaningProfessional;
    private List<TimeRange> availableTimes = new ArrayList<>();
}
