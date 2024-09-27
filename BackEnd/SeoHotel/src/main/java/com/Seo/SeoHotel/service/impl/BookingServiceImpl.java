package com.Seo.SeoHotel.service.impl;

import com.Seo.SeoHotel.dto.BookingDTO;
import com.Seo.SeoHotel.dto.Response;
import com.Seo.SeoHotel.entity.Booking;
import com.Seo.SeoHotel.entity.Room;
import com.Seo.SeoHotel.entity.User;
import com.Seo.SeoHotel.exception.OurException;
import com.Seo.SeoHotel.mapper.BookingMapper;
import com.Seo.SeoHotel.repository.BookingRepository;
import com.Seo.SeoHotel.repository.RoomRepository;
import com.Seo.SeoHotel.repository.UserRepository;
import com.Seo.SeoHotel.service.interfac.BookingService;
import com.Seo.SeoHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response = new Response();

        try {
            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Check in date must come after check out date");
            }

            Room room = roomRepository.findById(roomId).orElseThrow(() ->
                    new OurException("Room Not Found"));

            User user = userRepository.findById(userId).orElseThrow(() ->
                    new OurException("User Not Found"));

            List<Booking> existingBookings = room.getBookings();

            if(!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new OurException("Room Not Available For Selected Date Range");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);

            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);

            bookingRepository.save(bookingRequest);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving A Booking " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() ->
                    new OurException("Booking Not Found"));

            BookingDTO bookingDTO = BookingMapper.INSTANCE.bookingToBookingDTO(booking);

            response.setStatusCode(200);
            response.setMessage("Successfully Find Booking By Confirmation Code");
            response.setBooking(bookingDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During Booking Retrieval " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response findBookingByUserId(Long userId) {
        Response response = new Response();

        try {
            List<Booking> listBookings = bookingRepository.findByUserId(userId);

            List<BookingDTO> listBookingDTO = BookingMapper.INSTANCE.bookingsToBookingDTOs(listBookings);

            response.setStatusCode(200);
            response.setMessage("Successfully Find Booking By Confirmation Code");
            response.setListBookings(listBookingDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During Booking Retrieval " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();

        try {
            List<Booking> listBookings = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

            List<BookingDTO> listBookingDTO = new ArrayList<>();

            for(Booking booking:listBookings) {
                BookingDTO bookingDTO = BookingMapper.INSTANCE.bookingToBookingDTO(booking);
                listBookingDTO.add(bookingDTO);
            }

            response.setStatusCode(200);
            response.setMessage("Successfully Get All Booking");
            response.setListBookings(listBookingDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During Booking Retrieval " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();

        try {
            bookingRepository.findById(bookingId).orElseThrow(() ->
                    new OurException("Booking Not Found"));

            bookingRepository.deleteById(bookingId);

            response.setStatusCode(200);
            response.setMessage("Successfully Cancel Booking");


        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During Booing Cancel " + e.getMessage());
        }

        return response;
    }


    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                ||
                                bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate())
                                ||
                                (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                ||
                                (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                ||
                                (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

//                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
//                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
//
//                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
//                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
