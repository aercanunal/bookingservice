package org.justlife.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeRange implements Comparable<TimeRange>{
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public int compareTo(TimeRange o) {
        return startTime.compareTo(o.startTime);
    }

    public boolean isOverlap(TimeRange other) {
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }
}
