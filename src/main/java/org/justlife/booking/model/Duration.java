package org.justlife.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Duration {
    TWO_HOURS(2), FOUR_HOURS(4);

    private final Integer hours;

}
