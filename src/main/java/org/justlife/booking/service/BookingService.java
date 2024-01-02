package org.justlife.booking.service;

import org.justlife.booking.model.AvailableProfessionalsTimeDTO;
import org.justlife.booking.model.BookingRequestDTO;
import org.justlife.booking.model.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingService {

    List<AvailableProfessionalsTimeDTO> getAvailableList(LocalDate localDate);
    List<AvailableProfessionalsTimeDTO> getAvailableList(LocalDate startDate, LocalTime startTime, Duration duration);

    void createBooking(BookingRequestDTO bookingRequest);

    void updateBooking(BookingRequestDTO bookingRequest);
}
