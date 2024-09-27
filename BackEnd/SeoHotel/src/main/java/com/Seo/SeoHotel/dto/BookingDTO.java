package com.Seo.SeoHotel.dto;

import com.Seo.SeoHotel.entity.Booking;
import com.Seo.SeoHotel.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDTO {
    Long id;
    LocalDate checkInDate;
    LocalDate checkOutDate;
    int numOfAdults;
    int numOfChildren;
    int totalNumOfGuest;
    String bookingConfirmationCode;
    UserDTO user;
    RoomDTO room;
}
