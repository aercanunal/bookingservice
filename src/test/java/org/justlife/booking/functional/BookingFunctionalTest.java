package org.justlife.booking.functional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.justlife.booking.model.AvailableProfessionalsTimeDTO;
import org.justlife.booking.model.BookingRequestDTO;
import org.justlife.booking.model.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAvailableList() throws Exception {
        LocalDate testDate = LocalDate.now();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/available-list")
                        .param("startDate", testDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Cevap metni alınır ve JSON dizisine çevrilir
        String responseContent = result.getResponse().getContentAsString();
        List<AvailableProfessionalsTimeDTO> availableProfessionalsTimeList = objectMapper.readValue(responseContent, new TypeReference<List<AvailableProfessionalsTimeDTO>>() {});

        // Listede 25 eleman olmalı
        assertThat(availableProfessionalsTimeList).hasSize(25);
    }

    @Test
    public void testCreateBooking() throws Exception {
        // Test için kullanılacak BookingRequestDTO örneği oluştur
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setStartDate(LocalDate.now());
        bookingRequest.setStartTime(LocalTime.of(10, 0));
        bookingRequest.setDuration(Duration.TWO_HOURS);
        bookingRequest.setProfessionals(Collections.singletonList(1L));

        // API isteği oluştur
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/bookings/createBooking")
                        .content(objectMapper.writeValueAsString(bookingRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        // "Booking created successfully." mesajını kontrol et
        assertThat(contentAsString).isEqualTo("Booking created successfully.");
    }

    @Test
    public void testUpdateBooking() throws Exception {
        // Test için kullanılacak BookingRequestDTO örneği oluştur
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setId(1L);
        bookingRequest.setStartDate(LocalDate.now());
        bookingRequest.setStartTime(LocalTime.of(14, 0));
        bookingRequest.setDuration(Duration.TWO_HOURS);
        bookingRequest.setProfessionals(Collections.singletonList(2L));

        // API isteği oluştur
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookings/updateBooking")
                        .content(objectMapper.writeValueAsString(bookingRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        // "Booking created successfully." mesajını kontrol et
        assertThat(contentAsString).isEqualTo("Booking update successfully.");
    }
}
