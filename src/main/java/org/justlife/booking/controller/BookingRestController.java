package org.justlife.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.justlife.booking.model.AvailableProfessionalsTimeDTO;
import org.justlife.booking.model.BookingRequestDTO;
import org.justlife.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingRestController {

    private final BookingService bookingService;

    @Operation(summary = "Get Available Professionals Time List",
            description = "Get a list of available time slots for cleaning professionals on the specified date.")
    @GetMapping("/available-list")
    public List<AvailableProfessionalsTimeDTO> getAvailableList(@RequestParam LocalDate startDate) {
        // Verilen tarihe göre çakışmayan temizlik profesyonellerinin müsait zamanlarını al
        return bookingService.getAvailableList(startDate);
    }

    @Operation(summary = "Create Booking",
            description = "Create a new cleaning service booking with the provided information.")
    @PostMapping("/createBooking")
    public ResponseEntity<String> createBooking(@RequestBody @Valid BookingRequestDTO bookingRequest) {
        bookingService.createBooking(bookingRequest);

        return ResponseEntity.ok("Booking created successfully.");
    }

    @Operation(summary = "Update Booking",
            description = "Update an existing cleaning service booking with the provided information.")
    @PutMapping("/updateBooking")
    public ResponseEntity<String> updateBooking(@RequestBody @Valid BookingRequestDTO bookingRequest) {
        bookingService.updateBooking(bookingRequest);

        return ResponseEntity.ok("Booking update successfully.");
    }
}
