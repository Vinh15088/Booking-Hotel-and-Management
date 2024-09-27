package com.Seo.SeoHotel.repository;

import com.Seo.SeoHotel.entity.Booking;
import com.Seo.SeoHotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRoomId(Long roomId);

    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);

    List<Booking> findByUserId(Long userId);
}
