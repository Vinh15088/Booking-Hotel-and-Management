package com.Seo.SeoHotel.mapper;

import ch.qos.logback.core.pattern.color.CyanCompositeConverter;
import com.Seo.SeoHotel.dto.BookingDTO;
import com.Seo.SeoHotel.dto.RoomDTO;
import com.Seo.SeoHotel.dto.UserDTO;
import com.Seo.SeoHotel.entity.Booking;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Named("default")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "room", ignore = true)
    BookingDTO bookingToBookingDTO(Booking booking);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "room", source = "room")
    BookingDTO bookingToBookingDTOWithDetails(Booking booking);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "room", ignore = true)
    List<BookingDTO> bookingsToBookingDTOs(List<Booking> bookings);
}
