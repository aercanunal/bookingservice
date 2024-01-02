package org.justlife.booking.service.impl;

import lombok.RequiredArgsConstructor;
import org.justlife.booking.entity.CleaningServiceBooking;
import org.justlife.booking.mapper.CleaningServiceMapper;
import org.justlife.booking.model.*;
import org.justlife.booking.repository.CleaningServiceBookingRepository;
import org.justlife.booking.service.BookingService;
import org.justlife.booking.service.CleaningProfessionalService;
import org.justlife.booking.util.BookingCalculationUtil;
import org.justlife.booking.validation.BookingServiceValidation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final CleaningProfessionalService cleaningProfessionalService;

    private final CleaningServiceBookingRepository cleaningServiceBookingRepository;
    private final BookingCalculationUtil bookingCalculationUtil;

    private final BookingServiceValidation bookingServiceValidation;

    @Override
    public List<AvailableProfessionalsTimeDTO> getAvailableList(LocalDate startDate) {
        // Tüm temizlik profesyonellerini al
        List<CleaningProfessionalDTO> professionals = cleaningProfessionalService.getAllCleaningProfessionals();

        //startDate tarihinde ki tüm booking kayıtlarını al
        List<CleaningServiceBooking> bookings =cleaningServiceBookingRepository.findAllByStartDate(startDate);

        return bookingCalculationUtil.getAvailableProfessionalsTimeList(bookings, professionals);
    }

    @Override
    public List<AvailableProfessionalsTimeDTO> getAvailableList(LocalDate startDate, LocalTime startTime, Duration duration) {
        List<AvailableProfessionalsTimeDTO> availableList = getAvailableList(startDate);

        TimeRange selectTime = new TimeRange(startTime, startTime.plusHours(duration.getHours()));

        for (AvailableProfessionalsTimeDTO professional : availableList) {
            professional.setAvailableTimes(professional.getAvailableTimes()
                    .stream()
                    .filter(time -> time.isOverlap(selectTime))
                    .toList());
        }
        return availableList.stream().filter(x-> !x.getAvailableTimes().isEmpty()).toList();
    }

    @Override
    public void createBooking(BookingRequestDTO bookingRequest) {
        //validate booking
        bookingServiceValidation.validateBooking(bookingRequest);

        CleaningServiceBooking cleaningServiceBooking = CleaningServiceMapper.INSTANCE.toEntity(bookingRequest);
        cleaningServiceBookingRepository.save(cleaningServiceBooking);
    }

    @Override
    public void updateBooking(BookingRequestDTO bookingRequest) {
        //validate booking
        bookingServiceValidation.validateBooking(bookingRequest);

        CleaningServiceBooking cleaningServiceBooking = CleaningServiceMapper.INSTANCE.toEntity(bookingRequest);

        cleaningServiceBookingRepository.save(cleaningServiceBooking);
    }

}
