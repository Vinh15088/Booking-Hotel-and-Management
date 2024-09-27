package com.Seo.SeoHotel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "check in data is required")
    LocalDate checkInDate;

    @Future(message = "check out data must be in the future")
    LocalDate checkOutDate;

    @Min(value = 1, message = "Number of adults must not be less that 1")
    int numOfAdults;

    @Min(value = 0, message = "Number of children must not be less that 0")
    int numOfChildren;

    int totalNumOfGuest;
    String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @ToString.Exclude
    Room room;

    public void calculateTotalNumOfGuest() {
        this.totalNumOfGuest = this.numOfAdults + this.numOfChildren;
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumOfGuest();
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumOfGuest();
    }

}
