package org.justlife.booking.util;

import org.justlife.booking.entity.CleaningProfessional;
import org.justlife.booking.entity.CleaningServiceBooking;
import org.justlife.booking.mapper.CleaningServiceMapper;
import org.justlife.booking.model.AvailableProfessionalsTimeDTO;
import org.justlife.booking.model.CleaningProfessionalDTO;
import org.justlife.booking.model.Duration;
import org.justlife.booking.model.TimeRange;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BookingCalculationUtil {

    public List<TimeRange> findNonOverlappingTimeRanges(List<TimeRange> existingTimeRanges) {
        List<TimeRange> nonOverlappingTimeRanges = new ArrayList<>();

        if (existingTimeRanges == null || existingTimeRanges.isEmpty()) {
            // Eğer var olan zaman aralığı liste boşsa, günün tamamını ekleyebilirsiniz.
            nonOverlappingTimeRanges.add(new TimeRange(BookingConstant.START_TIME, BookingConstant.END_TIME));
            return nonOverlappingTimeRanges;
        }

        // İlk aralığı kontrol et
        if (existingTimeRanges.get(0).getStartTime().isAfter(BookingConstant.START_TIME)) {
            nonOverlappingTimeRanges.add(new TimeRange(BookingConstant.START_TIME, existingTimeRanges.get(0).getStartTime()));
        }

        // Orta aralıkları kontrol et
        for (int i = 1; i < existingTimeRanges.size(); i++) {
            TimeRange previousTimeRange = existingTimeRanges.get(i - 1);
            TimeRange currentTimeRange = existingTimeRanges.get(i);

            if (currentTimeRange.getStartTime().isAfter(previousTimeRange.getEndTime())) {
                nonOverlappingTimeRanges.add(new TimeRange(previousTimeRange.getEndTime(), currentTimeRange.getStartTime()));
            }
        }

        // Son aralığı kontrol et
        TimeRange lastTimeRange = existingTimeRanges.get(existingTimeRanges.size() - 1);
        if (lastTimeRange.getEndTime().isBefore(BookingConstant.END_TIME)) {
            nonOverlappingTimeRanges.add(new TimeRange(lastTimeRange.getEndTime(), BookingConstant.END_TIME));
        }

        return nonOverlappingTimeRanges;
    }

    public List<AvailableProfessionalsTimeDTO> getAvailableProfessionalsTimeList(List<CleaningServiceBooking> bookings,
                                                                                  List<CleaningProfessionalDTO> professionals) {
        //her bir temizlik personeli için booking'lere hızlı erişmek için bir map oluştur
        Map<Long, List<CleaningServiceBooking>> professionalsBookingMap = getProfessionalsBookingMap(bookings);

        List<AvailableProfessionalsTimeDTO> resultList = new ArrayList<>();
        for (CleaningProfessionalDTO professional : professionals) {
            AvailableProfessionalsTimeDTO availableTime = new AvailableProfessionalsTimeDTO();
            availableTime.setCleaningProfessional(professional);

            //Kullanılan zaman aralıkları
            List<TimeRange> timeRangeList = getBookingTimeRangeList(professional, professionalsBookingMap);

            //Diğer zaman aralıkları
            List<TimeRange> nonOverlappingTimeRanges = findNonOverlappingTimeRanges(timeRangeList);
            availableTime.setAvailableTimes(nonOverlappingTimeRanges);
            resultList.add(availableTime);
        }

        return resultList;
    }

    public AvailableProfessionalsTimeDTO getAvailableTimeOfProfessional(CleaningProfessional professional, List<CleaningServiceBooking> bookings) {
        CleaningProfessionalDTO professionalDTO = CleaningServiceMapper.INSTANCE.toDTO(professional);
        List<AvailableProfessionalsTimeDTO> professionalsTimeList = getAvailableProfessionalsTimeList(bookings, List.of(professionalDTO));
        return professionalsTimeList.stream().findFirst().orElse(null);
    }

    private List<TimeRange> getBookingTimeRangeList(CleaningProfessionalDTO professional, Map<Long, List<CleaningServiceBooking>> professionalsBookingMap) {
        return professionalsBookingMap
                .getOrDefault(professional.getId(), new ArrayList<>())
                .stream()
                .map(x-> createTimeRange(x.getStartTime(),x.getDuration()))
                .sorted().toList();
    }

    public Map<Long, List<CleaningServiceBooking>> getProfessionalsBookingMap(List<CleaningServiceBooking> bookings) {
        Map<Long, List<CleaningServiceBooking>> professionalBookingMap = new HashMap<>();
        for (CleaningServiceBooking booking : bookings) {
            for (CleaningProfessional professional : booking.getProfessionals()) {
                if (professionalBookingMap.containsKey(professional.getId())) {
                    professionalBookingMap.get(professional.getId()).add(booking);
                } else {
                    List<CleaningServiceBooking> list = new ArrayList<>();
                    list.add(booking);
                    professionalBookingMap.put(professional.getId(), list);
                }
            }
        }

        return professionalBookingMap;
    }

    public TimeRange createTimeRange(LocalTime startTime, Duration duration) {
        LocalTime endTime =startTime
                .plusHours(duration.getHours())
                .plusMinutes(BookingConstant.REST_TIME_MINUTE);
        return new TimeRange(startTime, endTime);
    }
}
