package org.justlife.booking.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.justlife.booking.entity.CleaningServiceBooking;
import org.justlife.booking.mapper.CleaningServiceMapper;
import org.justlife.booking.model.AvailableProfessionalsTimeDTO;
import org.justlife.booking.model.CleaningProfessionalDTO;
import org.justlife.booking.model.Duration;
import org.justlife.booking.model.TimeRange;
import org.justlife.booking.repository.CleaningServiceBookingRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BookingCalculationUtilTest {

    @Mock
    private CleaningServiceBookingRepository bookingRepository;

    @InjectMocks
    private BookingCalculationUtil bookingCalculationUtil;

    @Test
    void findNonOverlappingTimeRanges_whenExistingTimeRangesIsEmpty_shouldReturnFullDayRange() {
        List<TimeRange> existingTimeRanges = new ArrayList<>();


        List<TimeRange> result = bookingCalculationUtil.findNonOverlappingTimeRanges(existingTimeRanges);

        assertEquals(1, result.size());
        assertEquals(LocalTime.of(8, 0), result.get(0).getStartTime());
        assertEquals(LocalTime.of(22, 0), result.get(0).getEndTime());
    }

    @Test
    void findNonOverlappingTimeRanges_whenExistingTimeRangesNotOverlap_shouldReturnCombinedRanges() {
        List<TimeRange> existingTimeRanges = new ArrayList<>();
        existingTimeRanges.add(new TimeRange(LocalTime.of(8, 0), LocalTime.of(10, 0)));
        existingTimeRanges.add(new TimeRange(LocalTime.of(12, 0), LocalTime.of(14, 0)));

        List<TimeRange> result = bookingCalculationUtil.findNonOverlappingTimeRanges(existingTimeRanges);

        assertEquals(2, result.size());
        assertEquals(LocalTime.of(10, 0), result.get(0).getStartTime());
        assertEquals(LocalTime.of(12, 0), result.get(0).getEndTime());
        assertEquals(LocalTime.of(14, 0), result.get(1).getStartTime());
        assertEquals(LocalTime.of(22, 0), result.get(1).getEndTime());

    }

    @Test
    void findNonOverlappingTimeRanges_whenExistingTimeRangesOverlap_shouldReturnCombinedRanges() {
        // Arrange
        List<TimeRange> existingTimeRanges = new ArrayList<>();
        existingTimeRanges.add(new TimeRange(LocalTime.of(8, 0), LocalTime.of(12, 0)));
        existingTimeRanges.add(new TimeRange(LocalTime.of(12, 0), LocalTime.of(14, 0)));

        List<TimeRange> result = bookingCalculationUtil.findNonOverlappingTimeRanges(existingTimeRanges);

        assertEquals(1, result.size());
        assertEquals(LocalTime.of(14, 0), result.get(0).getStartTime());
        assertEquals(LocalTime.of(22, 0), result.get(0).getEndTime());
    }

    @Test
    void getAvailableProfessionalsTimeList() {
        // Arrange
        List<CleaningServiceBooking> bookings = new ArrayList<>();
        List<CleaningProfessionalDTO> professionals = new ArrayList<>();
        professionals.add(new CleaningProfessionalDTO(1L,"Test"));

        // Act
        List<AvailableProfessionalsTimeDTO> result = bookingCalculationUtil.getAvailableProfessionalsTimeList(bookings, professionals);

        // Assert
        assertEquals(1, result.size());
        assertEquals(LocalTime.of(8, 0), result.get(0).getAvailableTimes().get(0).getStartTime());
        assertEquals(LocalTime.of(22, 0), result.get(0).getAvailableTimes().get(0).getEndTime());
    }

    @Test
    void getAvailableTimeOfProfessional() {
        // Arrange
        List<CleaningProfessionalDTO> professionals = new ArrayList<>();
        CleaningProfessionalDTO professional = new CleaningProfessionalDTO(1L, "Test");
        professionals.add(professional);

        List<CleaningServiceBooking> bookings = new ArrayList<>();
        CleaningServiceBooking booking = new CleaningServiceBooking();
        booking.setStartTime(LocalTime.of(8, 0));
        booking.setDuration(Duration.TWO_HOURS);
        booking.setStartDate(LocalDate.now());
        booking.setId(1L);
        booking.setProfessionals(List.of(CleaningServiceMapper.INSTANCE.toEntity(professional)));
        bookings.add(booking);

        // Act
        List<AvailableProfessionalsTimeDTO> result = bookingCalculationUtil.getAvailableProfessionalsTimeList(bookings, professionals);

        // Assert
        assertEquals(1, result.size());
        assertEquals(LocalTime.of(10, 30), result.get(0).getAvailableTimes().get(0).getStartTime());
        assertEquals(LocalTime.of(22, 0), result.get(0).getAvailableTimes().get(0).getEndTime());
    }

    @Test
    void createTimeRange() {
        LocalTime startTime = LocalTime.of(8, 0);
        Duration duration = Duration.TWO_HOURS;

        TimeRange result = bookingCalculationUtil.createTimeRange(startTime, duration);

        assertEquals(LocalTime.of(8, 0), result.getStartTime());
        assertEquals(LocalTime.of(10, 30), result.getEndTime());
    }
}
