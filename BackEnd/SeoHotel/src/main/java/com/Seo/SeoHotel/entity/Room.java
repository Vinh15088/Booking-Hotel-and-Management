package com.Seo.SeoHotel.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String roomType;
    String roomPrice;
    String roomPhotoUrl;
    String roomDescription;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Booking> bookings = new ArrayList<>();
}
