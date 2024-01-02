package org.justlife.booking.mapper;

import org.justlife.booking.entity.CleaningProfessional;
import org.justlife.booking.entity.CleaningServiceBooking;
import org.justlife.booking.entity.Vehicle;
import org.justlife.booking.model.BookingRequestDTO;
import org.justlife.booking.model.CleaningProfessionalDTO;
import org.justlife.booking.model.CleaningServiceBookingDTO;
import org.justlife.booking.model.VehicleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CleaningServiceMapper {
    CleaningServiceMapper INSTANCE = Mappers.getMapper(CleaningServiceMapper.class);

    CleaningProfessionalDTO toDTO(CleaningProfessional professional);

    VehicleDTO toDTO(Vehicle vehicle);

    CleaningServiceBookingDTO toDTO(CleaningServiceBooking booking);

    CleaningProfessional toEntity(CleaningProfessionalDTO professionalDTO);

    Vehicle toEntity(VehicleDTO vehicleDTO);

    CleaningServiceBooking toEntity(CleaningServiceBookingDTO bookingDTO);


    @Mapping(source = "professionals", target = "professionals", qualifiedByName = "toProfessionals")
    CleaningServiceBooking toEntity(BookingRequestDTO bookingRequest);


    @Named(value = "toProfessionals")
    default List<CleaningProfessional> toProfessionals(List<Long> idList) {
        return idList.stream().map(id-> CleaningProfessional.builder().id(id).build()).collect(Collectors.toList());
    }
}
