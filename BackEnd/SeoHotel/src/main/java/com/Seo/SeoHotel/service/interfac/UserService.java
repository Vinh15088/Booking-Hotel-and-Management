package com.Seo.SeoHotel.service.interfac;

import com.Seo.SeoHotel.dto.RequestLogin;
import com.Seo.SeoHotel.dto.Response;
import com.Seo.SeoHotel.entity.User;

public interface UserService {
    Response register(User user);

    Response login(RequestLogin requestLogin);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
