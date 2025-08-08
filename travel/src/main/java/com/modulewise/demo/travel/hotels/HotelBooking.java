package com.modulewise.demo.travel.hotels;

import com.modulewise.demo.travel.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RedisHash("hotel_bookings")
public class HotelBooking {

    @Id
    private String id;
    private Hotel hotel;
    private Person guest;
    private String bookingReference;
    private String roomNumber;
    private String roomType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkin;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkout;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bookingTime;

    private String status; // CONFIRMED, CANCELLED, CHECKED_IN, CHECKED_OUT, etc.

    public HotelBooking() {}

    public HotelBooking(String id, Hotel hotel, Person guest, String bookingReference, 
                       String roomNumber, String roomType, LocalDate checkin, LocalDate checkout, 
                       LocalDateTime bookingTime, String status) {
        this.id = id;
        this.hotel = hotel;
        this.guest = guest;
        this.bookingReference = bookingReference;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.checkin = checkin;
        this.checkout = checkout;
        this.bookingTime = bookingTime;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Person getGuest() {
        return guest;
    }

    public void setGuest(Person guest) {
        this.guest = guest;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalDate checkout) {
        this.checkout = checkout;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
