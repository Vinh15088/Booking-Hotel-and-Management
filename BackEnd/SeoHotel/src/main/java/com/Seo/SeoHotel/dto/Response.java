package com.Seo.SeoHotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Response {
    int statusCode;
    String message;

    String token;
    String role;
    String expirationTime;
    String bookingConfirmationCode;

    UserDTO user;
    RoomDTO room;
    BookingDTO booking;
    List<UserDTO> listUsers;
    List<RoomDTO> listRooms;
    List<BookingDTO> listBookings;

}
