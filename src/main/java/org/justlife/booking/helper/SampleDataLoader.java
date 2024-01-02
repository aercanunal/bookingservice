package org.justlife.booking.helper;

import lombok.RequiredArgsConstructor;
import org.justlife.booking.entity.CleaningProfessional;
import org.justlife.booking.entity.Vehicle;
import org.justlife.booking.repository.CleaningProfessionalRepository;
import org.justlife.booking.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SampleDataLoader implements CommandLineRunner {

    private final CleaningProfessionalRepository cleaningProfessionalRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Temizlik profesyonelleri
        List<CleaningProfessional> cleaningProfessionals = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            CleaningProfessional professional = new CleaningProfessional();
            professional.setName("Professional" + i);
            cleaningProfessionals.add(professional);
        }

        cleaningProfessionalRepository.saveAll(cleaningProfessionals);

        // Araçlar
        List<Vehicle> vehicles = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Vehicle vehicle = new Vehicle();
            vehicle.setPlateNumber("35ABC"+i);
            vehicle.setModel("model"+i);
            vehicleRepository.save(vehicle);
            vehicles.add(vehicle);

            cleaningProfessionals.stream()
                    .skip((i-1)*5)
                    .limit(5)
                    .forEach(cleaningProfessional -> cleaningProfessional.setVehicle(vehicle));

            cleaningProfessionalRepository.saveAll(cleaningProfessionals);
        }

        // Araç ve temizlik profesyonelleri ilişkisi
        int professionalIndex = 0;
        for (Vehicle vehicle : vehicles) {
            for (int i = 0; i < 5; i++) {
                vehicle.getProfessionals().add(cleaningProfessionals.get(professionalIndex));
                professionalIndex++;
            }
        }

        vehicleRepository.saveAll(vehicles);
    }
}
