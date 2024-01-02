package org.justlife.booking.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.justlife.booking.entity.CleaningServiceBooking;
import org.justlife.booking.mapper.CleaningServiceMapper;
import org.justlife.booking.model.AvailableProfessionalsTimeDTO;
import org.justlife.booking.model.BookingRequestDTO;
import org.justlife.booking.model.CleaningProfessionalDTO;
import org.justlife.booking.model.Duration;
import org.justlife.booking.repository.CleaningServiceBookingRepository;
import org.justlife.booking.service.CleaningProfessionalService;
import org.justlife.booking.util.BookingCalculationUtil;
import org.justlife.booking.validation.BookingServiceValidation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {

    @Mock
    private CleaningProfessionalService cleaningProfessionalService;

    @Mock
    private CleaningServiceBookingRepository cleaningServiceBookingRepository;

    @Mock
    private BookingCalculationUtil bookingCalculationUtil;

    @Mock
    private BookingServiceValidation bookingServiceValidation;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBooking_ValidBookingRequest_BookingSavedSuccessfully() {
        // Arrange
        BookingRequestDTO bookingRequest = createSampleBookingRequest();
        CleaningServiceBooking cleaningServiceBooking = CleaningServiceMapper.INSTANCE.toEntity(bookingRequest);

        // Mocking
        when(cleaningServiceBookingRepository.save(any(CleaningServiceBooking.class))).thenReturn(cleaningServiceBooking);

        // Act
        bookingService.createBooking(bookingRequest);

        // Assert
        verify(bookingServiceValidation).validateBooking(bookingRequest);
        verify(cleaningServiceBookingRepository).save(cleaningServiceBooking);
    }

    @Test
    void updateBooking_ValidBookingRequest_BookingUpdatedSuccessfully() {
        // Arrange
        BookingRequestDTO bookingRequest = createSampleBookingRequest();
        CleaningServiceBooking cleaningServiceBooking = CleaningServiceMapper.INSTANCE.toEntity(bookingRequest);

        // Mocking
        when(cleaningServiceBookingRepository.save(any(CleaningServiceBooking.class))).thenReturn(cleaningServiceBooking);

        // Act
        bookingService.updateBooking(bookingRequest);

        // Assert
        verify(bookingServiceValidation).validateBooking(bookingRequest);
        verify(cleaningServiceBookingRepository).save(cleaningServiceBooking);
    }

    @Test
    void getAvailableList_ValidStartDate_AvailableListReturned() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        List<CleaningProfessionalDTO> professionals = List.of(new CleaningProfessionalDTO());
        List<CleaningServiceBooking> bookings = List.of(new CleaningServiceBooking());
        List<AvailableProfessionalsTimeDTO> availableProfessionalsTimeList = List.of(new AvailableProfessionalsTimeDTO());

        // Mocking
        when(cleaningProfessionalService.getAllCleaningProfessionals()).thenReturn(professionals);
        when(cleaningServiceBookingRepository.findAllByStartDate(startDate)).thenReturn(bookings);
        when(bookingCalculationUtil.getAvailableProfessionalsTimeList(bookings, professionals)).thenReturn(availableProfessionalsTimeList);

        // Act
        List<AvailableProfessionalsTimeDTO> result = bookingService.getAvailableList(startDate);

        // Assert
        verify(cleaningProfessionalService).getAllCleaningProfessionals();
        verify(cleaningServiceBookingRepository).findAllByStartDate(startDate);
        verify(bookingCalculationUtil).getAvailableProfessionalsTimeList(bookings, professionals);
        assertThat(result).isEqualTo(availableProfessionalsTimeList);
    }

    @Test
    void getAvailableListWithTime_ValidParameters_AvailableListWithTimeReturned() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalTime startTime = LocalTime.of(8,0);
        Duration duration = Duration.TWO_HOURS;
        List<CleaningProfessionalDTO> professionals = List.of(new CleaningProfessionalDTO());
        List<CleaningServiceBooking> bookings = List.of(new CleaningServiceBooking());
        List<AvailableProfessionalsTimeDTO> availableProfessionalsTimeList = List.of(new AvailableProfessionalsTimeDTO());

        // Mocking
        when(cleaningProfessionalService.getAllCleaningProfessionals()).thenReturn(professionals);
        when(cleaningServiceBookingRepository.findAllByStartDate(startDate)).thenReturn(bookings);
        when(bookingCalculationUtil.getAvailableProfessionalsTimeList(bookings, professionals)).thenReturn(availableProfessionalsTimeList);

        // Act
        List<AvailableProfessionalsTimeDTO> result = bookingService.getAvailableList(startDate, startTime, duration);

        // Assert
        verify(cleaningProfessionalService).getAllCleaningProfessionals();
        verify(cleaningServiceBookingRepository).findAllByStartDate(startDate);
        verify(bookingCalculationUtil).getAvailableProfessionalsTimeList(bookings, professionals);
    }

    private BookingRequestDTO createSampleBookingRequest() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setId(1L);
        bookingRequest.setStartDate(LocalDate.now());
        bookingRequest.setStartTime(LocalTime.of(8,0));
        bookingRequest.setDuration(Duration.TWO_HOURS);
        bookingRequest.setProfessionals(List.of(1L));

        return bookingRequest;
    }
}
