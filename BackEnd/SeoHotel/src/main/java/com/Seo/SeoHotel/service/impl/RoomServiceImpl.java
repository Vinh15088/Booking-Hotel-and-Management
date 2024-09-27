package com.Seo.SeoHotel.service.impl;

import com.Seo.SeoHotel.dto.Response;
import com.Seo.SeoHotel.dto.RoomDTO;
import com.Seo.SeoHotel.entity.Room;
import com.Seo.SeoHotel.exception.OurException;
import com.Seo.SeoHotel.mapper.RoomMapper;
import com.Seo.SeoHotel.repository.BookingRepository;
import com.Seo.SeoHotel.repository.RoomRepository;
import com.Seo.SeoHotel.service.AwsS3Service;
import com.Seo.SeoHotel.service.interfac.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();

        try {
            String imageUrl = awsS3Service.saveImageToS3(photo);

            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);

            Room savedRoom = roomRepository.save(room);

            RoomDTO roomDTO = RoomMapper.INSTANCE.roomToRoomDTO(savedRoom);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During Room Add " + e.getMessage());
        }

        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();

        try {
            List<Room> listRooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

            List<RoomDTO> listRoomDTO = new ArrayList<>();

            for(Room room:listRooms) {
                RoomDTO roomDTO = RoomMapper.INSTANCE.roomToRoomDTO(room);
                listRoomDTO.add(roomDTO);
            }

            response.setStatusCode(200);
            response.setMessage("Successfully Retrieved All Rooms");
            response.setListRooms(listRoomDTO);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During Room Retrieval " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();

        try {
            roomRepository.findById(roomId).orElseThrow(() ->
                    new OurException("Roon Not Found"));

            roomRepository.deleteById(roomId);

            response.setStatusCode(200);
            response.setMessage("Successfully Deleted Room");
        } catch (OurException e) {
          response.setStatusCode(404);
          response.setMessage(e.getMessage());

        } catch (Exception e) {
          response.setStatusCode(500);
            response.setMessage("Error Occurred During Room Deletion " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response updateRoom(Long roomId, MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();

        try {
            String imageUrl = null;
            if(photo != null && !photo.isEmpty()) {
                imageUrl = awsS3Service.saveImageToS3(photo);
            }

            Room room = roomRepository.findById(roomId).orElseThrow(() ->
                    new OurException("Room Not Found"));

            if(roomType != null) room.setRoomType(roomType);
            if(roomPrice != null && !roomPrice.toString().trim().isEmpty()) room.setRoomPrice(roomPrice);
            if(description != null) room.setRoomDescription(description);
            if(imageUrl != null) room.setRoomPhotoUrl(imageUrl);

            Room updatedRoom = roomRepository.save(room);

            RoomDTO roomDTO = RoomMapper.INSTANCE.roomToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("Successfully Updated Room");
            response.setRoom(roomDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During Room Update " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();

         try {
            Room room = roomRepository.findById(roomId).orElseThrow(() ->
                    new OurException("Room Not Found"));

            RoomDTO roomDTO = RoomMapper.INSTANCE.roomToRoomDTO(room);

            response.setStatusCode(200);
            response.setMessage("Successfully Get Room By Id");
            response.setRoom(roomDTO);


         } catch (OurException e) {
             response.setStatusCode(404);
             response.setMessage(e.getMessage());
         } catch (Exception e) {
             response.setStatusCode(500);
             response.setMessage("Error Occurred During Room Retrieval " + e.getMessage());
         }

        return response;
    }

    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();

        try {
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);

            List<RoomDTO> listRoomDTO = RoomMapper.INSTANCE.roomsToRoomDTOs(availableRooms);

            response.setStatusCode(200);
            response.setMessage("Successfully Get Available Rooms By Date And Type");
            response.setListRooms(listRoomDTO);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During Room Retrieval " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAvailableRooms() {
        Response response = new Response();

        try {
            List<Room> listRooms = roomRepository.getAllAvailableRooms();

            List<RoomDTO> listRoomDTO = new ArrayList<>();

            for(Room room:listRooms) {
                RoomDTO roomDTO = RoomMapper.INSTANCE.roomToRoomDTO(room);
                listRoomDTO.add(roomDTO);
            }

            response.setStatusCode(200);
            response.setMessage("Successfully Get Available Rooms");
            response.setListRooms(listRoomDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During Room Retrieval " + e.getMessage());
        }

        return response;
    }
}
