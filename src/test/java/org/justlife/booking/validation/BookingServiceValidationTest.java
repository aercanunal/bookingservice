package org.justlife.booking.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.justlife.booking.entity.CleaningProfessional;
import org.justlife.booking.entity.Vehicle;
import org.justlife.booking.mapper.CleaningServiceMapper;
import org.justlife.booking.model.AvailableProfessionalsTimeDTO;
import org.justlife.booking.model.BookingRequestDTO;
import org.justlife.booking.model.Duration;
import org.justlife.booking.model.TimeRange;
import org.justlife.booking.model.base.BusinessException;
import org.justlife.booking.repository.CleaningProfessionalRepository;
import org.justlife.booking.repository.CleaningServiceBookingRepository;
import org.justlife.booking.util.BookingCalculationUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceValidationTest {

    @Mock
    private CleaningServiceBookingRepository cleaningServiceBookingRepository;

    @Mock
    private CleaningProfessionalRepository cleaningProfessionalRepository;

    @Mock
    private BookingCalculationUtil bookingCalculationUtil;

    @InjectMocks
    private BookingServiceValidation bookingServiceValidation;

    @Test
    void validateBooking_ValidBooking() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setStartDate(LocalDate.now().with(DayOfWeek.MONDAY));
        bookingRequest.setStartTime(LocalTime.of(10, 0));
        bookingRequest.setDuration(Duration.TWO_HOURS);
        bookingRequest.setProfessionals(Arrays.asList(1L, 2L, 3L));

        CleaningProfessional professional1 = new CleaningProfessional(1L, "John Doe", getVehicle(1L));
        CleaningProfessional professional2 = new CleaningProfessional(2L, "Jane Doe", getVehicle(1L));
        CleaningProfessional professional3 = new CleaningProfessional(3L, "Bob Doe", getVehicle(1L));

        when(cleaningProfessionalRepository.findAllById(any())).thenReturn(Arrays.asList(professional1, professional2, professional3));
        when(bookingCalculationUtil.getAvailableTimeOfProfessional(any(), any())).thenReturn(
                new AvailableProfessionalsTimeDTO(CleaningServiceMapper.INSTANCE.toDTO(professional1),
                        List.of(new TimeRange(LocalTime.of(8, 0), LocalTime.of(10, 30)))),
                new AvailableProfessionalsTimeDTO(CleaningServiceMapper.INSTANCE.toDTO(professional2),
                        List.of(new TimeRange(LocalTime.of(8, 0), LocalTime.of(10, 30)))),
                new AvailableProfessionalsTimeDTO(CleaningServiceMapper.INSTANCE.toDTO(professional3),
                        List.of(new TimeRange(LocalTime.of(8, 0), LocalTime.of(10, 10))))
        );

        assertDoesNotThrow(() -> bookingServiceValidation.validateBooking(bookingRequest));
    }

    @Test
    void validateBooking_FIRDAY() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setStartDate(LocalDate.now().with(DayOfWeek.FRIDAY));
        bookingRequest.setStartTime(LocalTime.of(8, 0));
        bookingRequest.setDuration(Duration.FOUR_HOURS);
        bookingRequest.setProfessionals(Arrays.asList(1L, 2L, 3L));

        assertThrows(BusinessException.class, () -> bookingServiceValidation.validateBooking(bookingRequest));
    }

    @Test
    void validateBooking_NotWorkingHour() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setStartDate(LocalDate.now().with(DayOfWeek.MONDAY));
        bookingRequest.setStartTime(LocalTime.of(7, 0));
        bookingRequest.setDuration(Duration.FOUR_HOURS);
        bookingRequest.setProfessionals(Arrays.asList(1L, 2L, 3L));

        assertThrows(BusinessException.class, () -> bookingServiceValidation.validateBooking(bookingRequest));
    }

    @Test
    void validateBooking_ValidBooking_ProfessionalCount() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setStartDate(LocalDate.now().with(DayOfWeek.MONDAY));
        bookingRequest.setStartTime(LocalTime.of(10, 0));
        bookingRequest.setDuration(Duration.TWO_HOURS);
        bookingRequest.setProfessionals(Arrays.asList(1L, 2L, 3L, 4L));

        assertThrows(BusinessException.class, () -> bookingServiceValidation.validateBooking(bookingRequest));
    }

    @Test
    void validateBooking_ValidBooking_Vehicle() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setStartDate(LocalDate.now().with(DayOfWeek.MONDAY));
        bookingRequest.setStartTime(LocalTime.of(10, 0));
        bookingRequest.setDuration(Duration.TWO_HOURS);
        bookingRequest.setProfessionals(Arrays.asList(1L, 2L, 3L));

        CleaningProfessional professional1 = new CleaningProfessional(1L, "John Doe", getVehicle(1L));
        CleaningProfessional professional2 = new CleaningProfessional(2L, "Jane Doe", getVehicle(2L));
        CleaningProfessional professional3 = new CleaningProfessional(3L, "Bob Doe", getVehicle(1L));

        when(cleaningProfessionalRepository.findAllById(any())).thenReturn(Arrays.asList(professional1, professional2, professional3));

        assertThrows(BusinessException.class, () -> bookingServiceValidation.validateBooking(bookingRequest));
    }

    @Test
    void validateAvailability_InvalidAvailability_AvailableTime() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setStartDate(LocalDate.now().with(DayOfWeek.MONDAY));
        bookingRequest.setStartTime(LocalTime.of(10, 0));
        bookingRequest.setDuration(Duration.TWO_HOURS);
        bookingRequest.setProfessionals(Arrays.asList(1L, 2L, 3L));

        CleaningProfessional professional1 = new CleaningProfessional(1L, "John Doe", getVehicle(1L));
        CleaningProfessional professional2 = new CleaningProfessional(2L, "Jane Doe", getVehicle(1L));
        CleaningProfessional professional3 = new CleaningProfessional(3L, "Bob Doe", getVehicle(1L));

        when(cleaningProfessionalRepository.findAllById(any())).thenReturn(Arrays.asList(professional1, professional2, professional3));
        when(bookingCalculationUtil.getAvailableTimeOfProfessional(any(), any())).thenReturn(
                new AvailableProfessionalsTimeDTO(CleaningServiceMapper.INSTANCE.toDTO(professional1),
                        List.of(new TimeRange(LocalTime.of(13, 0), LocalTime.of(15, 30)))),
                new AvailableProfessionalsTimeDTO(CleaningServiceMapper.INSTANCE.toDTO(professional2),
                        List.of(new TimeRange(LocalTime.of(13, 0), LocalTime.of(15, 30)))),
                new AvailableProfessionalsTimeDTO(CleaningServiceMapper.INSTANCE.toDTO(professional3),
                        List.of(new TimeRange(LocalTime.of(13, 0), LocalTime.of(15, 10))))
        );

        assertThrows(BusinessException.class, () -> bookingServiceValidation.validateBooking(bookingRequest));
    }

    private Vehicle getVehicle(Long id) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        return vehicle;
    }

}
