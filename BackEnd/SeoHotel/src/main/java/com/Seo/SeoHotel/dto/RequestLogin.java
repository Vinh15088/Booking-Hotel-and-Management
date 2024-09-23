package com.Seo.SeoHotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestLogin {
    @NotNull(message = "Email is required")
    String email;

    @NotNull(message = "Password is required")
    String password;
}
