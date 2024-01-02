package org.justlife.booking.util;

import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@NoArgsConstructor
public class BookingConstant {
    public static final LocalTime START_TIME = LocalTime.of(8,0,0);
    public static final LocalTime END_TIME = LocalTime.of(22,0,0);

    public static final DayOfWeek OFF_DAY = DayOfWeek.FRIDAY;


    public static final Integer REST_TIME_MINUTE = 30;
}
