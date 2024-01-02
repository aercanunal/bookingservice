package org.justlife.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.justlife.booking.model.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cleaning_service_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CleaningServiceBooking implements Comparable<CleaningServiceBooking> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(unique = false)
    private Duration duration;

    @ManyToMany
    @JoinTable(
            name = "booking_cleaning_professionals",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "cleaning_professional_id")
    )
    private List<CleaningProfessional> professionals = new ArrayList<>();

    @Override
    public int compareTo(CleaningServiceBooking other) {
        int dateComparison = this.startDate.compareTo(other.startDate);
        return dateComparison != 0 ? dateComparison : this.startTime.compareTo(other.startTime);
    }

}
