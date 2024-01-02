package org.justlife.booking.validation;

import lombok.RequiredArgsConstructor;
import org.justlife.booking.entity.CleaningProfessional;
import org.justlife.booking.entity.CleaningServiceBooking;
import org.justlife.booking.entity.Vehicle;
import org.justlife.booking.model.AvailableProfessionalsTimeDTO;
import org.justlife.booking.model.BookingRequestDTO;
import org.justlife.booking.model.Duration;
import org.justlife.booking.model.TimeRange;
import org.justlife.booking.model.base.BusinessException;
import org.justlife.booking.repository.CleaningProfessionalRepository;
import org.justlife.booking.repository.CleaningServiceBookingRepository;
import org.justlife.booking.util.BookingCalculationUtil;
import org.justlife.booking.util.BookingConstant;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingServiceValidation {

    private final CleaningServiceBookingRepository cleaningServiceBookingRepository;
    private final CleaningProfessionalRepository cleaningProfessionalRepository;
    private final BookingCalculationUtil bookingCalculationUtil;

    public void validateBooking(BookingRequestDTO bookingRequest) {
        validateWorkingDay(bookingRequest);
        validateWorkingHours(bookingRequest);
        validateProfessionalCount(bookingRequest);

        List<CleaningProfessional> cleaningProfessionals = cleaningProfessionalRepository.findAllById(bookingRequest.getProfessionals());
        validateProfessionalsInSameVehicle(cleaningProfessionals);

        validateAvailability(bookingRequest, cleaningProfessionals);
    }

    private void validateAvailability(BookingRequestDTO bookingRequest,
                                      List<CleaningProfessional> cleaningProfessionals) {
        TimeRange timeRange = getBookingTimeRangeWithRestTime(bookingRequest.getStartTime(), bookingRequest.getDuration());
        for (CleaningProfessional professional : cleaningProfessionals) {
            List<CleaningServiceBooking> bookings = getProfessionalsBookingList(bookingRequest, professional);

            AvailableProfessionalsTimeDTO availableTime = bookingCalculationUtil.getAvailableTimeOfProfessional(professional, bookings);

            if (availableTime.getAvailableTimes().stream().noneMatch(x -> x.isOverlap(timeRange))) {
                throw new BusinessException("Cleaners must be available at requested start time");
            }
        }
    }

    private List<CleaningServiceBooking> getProfessionalsBookingList(BookingRequestDTO bookingRequest, CleaningProfessional professional) {
        return cleaningServiceBookingRepository
                .findAllByStartDateAndProfessionalsContaining(bookingRequest.getStartDate(), professional)
                .stream()
                .filter(x-> Objects.isNull(bookingRequest.getId()) || !Objects.equals(bookingRequest.getId(),x.getId()))
                .toList();
    }

    private void validateProfessionalsInSameVehicle(List<CleaningProfessional> cleaningProfessionals) {
        List<Vehicle> vehicles = getVehiclesForCleaningProfessionals(cleaningProfessionals);
        if (vehicles.size() != 1) {
            throw new BusinessException("Multiple cleaners must belong to the same vehicle.");
        }
    }

    private List<Vehicle> getVehiclesForCleaningProfessionals(List<CleaningProfessional> cleaningProfessionals) {
        return cleaningProfessionals.stream()
                .map(CleaningProfessional::getVehicle)
                .distinct()
                .collect(Collectors.toList());
    }


    private void validateProfessionalCount(BookingRequestDTO bookingRequest) {
        if (bookingRequest.getProfessionals().size() > 3) {
            throw new BusinessException("Each booking should be assigned to one or multiple cleaner professionals (1,2 or 3)");
        }
    }

    private void validateWorkingHours(BookingRequestDTO bookingRequest) {
        TimeRange timeRange = getBookingTimeRangeWithRestTime(bookingRequest.getStartTime(), bookingRequest.getDuration());
        if (timeRange.getStartTime().isBefore(BookingConstant.START_TIME) || timeRange.getEndTime().isAfter(BookingConstant.END_TIME)) {
            throw new BusinessException("Each booking should be assigned to one or multiple cleaner professionals (1,2 or 3)");
        }
    }

    private TimeRange getBookingTimeRangeWithRestTime(LocalTime startTime, Duration duration) {
        LocalTime endTime =startTime
                .plusHours(duration.getHours())
                .plusMinutes(BookingConstant.REST_TIME_MINUTE);
        return new TimeRange(startTime, endTime);
    }

    private void validateWorkingDay(BookingRequestDTO bookingRequest) {
        if (BookingConstant.OFF_DAY.equals(bookingRequest.getStartDate().getDayOfWeek())) {
            throw new BusinessException("Not allowed to work on Fridays");
        }
    }
}
