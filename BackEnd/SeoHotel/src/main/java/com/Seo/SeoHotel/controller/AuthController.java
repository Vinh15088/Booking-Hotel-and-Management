package com.Seo.SeoHotel.controller;


import com.Seo.SeoHotel.dto.RequestLogin;
import com.Seo.SeoHotel.dto.Response;
import com.Seo.SeoHotel.entity.User;
import com.Seo.SeoHotel.service.interfac.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user) {
        Response response = userService.register(user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody RequestLogin requestLogin) {
        Response response = userService.login(requestLogin);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
