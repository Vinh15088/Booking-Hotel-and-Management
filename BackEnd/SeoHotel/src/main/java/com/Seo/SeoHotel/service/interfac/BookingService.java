package com.Seo.SeoHotel.service.interfac;

import com.Seo.SeoHotel.dto.Response;
import com.Seo.SeoHotel.entity.Booking;

public interface BookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response findBookingByUserId(Long userId);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);
}
