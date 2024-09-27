package com.Seo.SeoHotel.service.impl;

import com.Seo.SeoHotel.dto.RequestLogin;
import com.Seo.SeoHotel.dto.Response;
import com.Seo.SeoHotel.dto.UserDTO;
import com.Seo.SeoHotel.entity.User;
import com.Seo.SeoHotel.exception.OurException;
import com.Seo.SeoHotel.mapper.UserMapper;
import com.Seo.SeoHotel.repository.UserRepository;
import com.Seo.SeoHotel.service.interfac.UserService;
import com.Seo.SeoHotel.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Response register(User user) {
        Response response = new Response();

        try {
            if(user.getRole() == null || user.getRole().isBlank()) user.setRole("USER");

            if(userRepository.existsByEmail(user.getEmail()))
                throw new OurException(user.getEmail() + " Already Exists");

            user.setPassword((passwordEncoder.encode(user.getPassword())));

            User savedUser = userRepository.save(user);

            UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(savedUser);

            response.setStatusCode(200);
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Registration " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response login(RequestLogin requestLogin) {
        Response response = new Response();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword()));

            var user = userRepository.findByEmail(requestLogin.getEmail()).orElseThrow(() ->
                    new OurException("user not found"));

            var token = jwtUtils.generateToken(user);

            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 days");
            response.setMessage("Successfully Logged In");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Login " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();

        try {
            List<User> listUsers = userRepository.findAll();

            List<UserDTO> userDTOList = new ArrayList<>();

            for (User user:listUsers) {
                UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);
                userDTOList.add(userDTO);
            }

            response.setStatusCode(200);
            response.setMessage("Successfully Retrieved All Users");
            response.setListUsers(userDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Retrieval " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() ->
                    new OurException("User Not Found"));

            UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);

            response.setStatusCode(200);
            response.setMessage("Successfully Retrieved User Booking History");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Booking History " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();

        try {
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() ->
                    new OurException("User Not Found"));

            userRepository.deleteById(Long.valueOf(userId));

            response.setStatusCode(200);
            response.setMessage("Successfully Deleted User");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Deletion " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() ->
                    new OurException("User Not Found"));

            UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);

            response.setStatusCode(200);
            response.setMessage("Successfully Retrieved User By ID");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Retrieval " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try {
            User user = userRepository.findByEmail(email).orElseThrow(() ->
                    new OurException("User Not Found"));

            UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);

            response.setStatusCode(200);
            response.setMessage("Successfully Retrieved User By Email");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Retrieval " + e.getMessage());
        }

        return response;
    }
}
