package org.justlife.booking.service;

import org.justlife.booking.model.CleaningProfessionalDTO;
import org.justlife.booking.model.CleaningServiceBookingDTO;
import org.justlife.booking.model.VehicleDTO;

public interface CleaningService {
    CleaningProfessionalDTO getProfessionalById(Long id);

    VehicleDTO getVehicleById(Long id);

    CleaningServiceBookingDTO getBookingById(Long id);

    void saveProfessional(CleaningProfessionalDTO professionalDTO);

    void saveVehicle(VehicleDTO vehicleDTO);

    void saveBooking(CleaningServiceBookingDTO bookingDTO);
}
