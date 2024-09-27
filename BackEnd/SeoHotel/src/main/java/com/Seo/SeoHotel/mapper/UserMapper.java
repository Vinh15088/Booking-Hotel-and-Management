package com.Seo.SeoHotel.mapper;

import com.Seo.SeoHotel.dto.BookingDTO;
import com.Seo.SeoHotel.dto.UserDTO;
import com.Seo.SeoHotel.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Named("default")
    @Mapping(target = "bookings", ignore = true)
    UserDTO userToUserDTO(User user);

    @Mapping(target = "bookings", ignore = false)
    UserDTO userToUserDTOWithBookings(User user);

    @Mapping(target = "bookings", ignore = true)
    List<UserDTO> usersToUserDTOs(List<User> users);
}

