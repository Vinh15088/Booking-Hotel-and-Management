package com.Seo.SeoHotel.dto;

import com.Seo.SeoHotel.entity.Booking;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    Long id;
    String email;
    String name;
    String phoneNumber;
    String role;
    List<BookingDTO> bookings = new ArrayList<>();
}
