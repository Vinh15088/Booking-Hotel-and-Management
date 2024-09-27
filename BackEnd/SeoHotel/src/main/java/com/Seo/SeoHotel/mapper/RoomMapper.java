package com.Seo.SeoHotel.mapper;

import com.Seo.SeoHotel.dto.BookingDTO;
import com.Seo.SeoHotel.dto.RoomDTO;
import com.Seo.SeoHotel.entity.Booking;
import com.Seo.SeoHotel.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    @Named("default")
    @Mapping(target = "bookings", ignore = true)
    RoomDTO roomToRoomDTO(Room room);

    @Mapping(target = "bookings", ignore = false)
    RoomDTO roomToRoomDTOWithBookings(Room room);

    @Mapping(target = "bookings", ignore = true)
    List<RoomDTO> roomsToRoomDTOs(List<Room> rooms);
}


