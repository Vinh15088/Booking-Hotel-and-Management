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
public class RoomDTO {
    String roomType;
    String roomPrice;
    String roomPhotoUrl;
    String roomDescription;
    List<BookingDTO> bookings;
}
