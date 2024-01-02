package org.justlife.booking.repository;

import org.justlife.booking.entity.CleaningProfessional;
import org.justlife.booking.entity.CleaningServiceBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CleaningServiceBookingRepository extends JpaRepository<CleaningServiceBooking, Long> {
    List<CleaningServiceBooking> findAllByStartDate(LocalDate startDate);

    List<CleaningServiceBooking> findAllByStartDateAndProfessionalsContaining(LocalDate startDate, CleaningProfessional professional);

}
