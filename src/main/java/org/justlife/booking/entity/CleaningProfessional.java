package org.justlife.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "cleaning_professionals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CleaningProfessional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    /*@ElementCollection
    @CollectionTable(name = "cleaning_professional_available_times", joinColumns = @JoinColumn(name = "cleaning_professional_id"))
    private List<LocalDateTime> availableTimes =  new ArrayList<>();*/
}

