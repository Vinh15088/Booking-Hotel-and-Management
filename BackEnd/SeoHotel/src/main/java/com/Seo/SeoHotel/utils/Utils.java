package com.Seo.SeoHotel.utils;

import com.Seo.SeoHotel.dto.BookingDTO;
import com.Seo.SeoHotel.dto.RoomDTO;
import com.Seo.SeoHotel.dto.UserDTO;
import com.Seo.SeoHotel.entity.Booking;
import com.Seo.SeoHotel.entity.Room;
import com.Seo.SeoHotel.entity.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    private static final String AlphaNumeric_String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i=0; i<length; i++) {
            int randomIndex = secureRandom.nextInt(AlphaNumeric_String.length());
            char randomChar = AlphaNumeric_String.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

}
