package com.modulewise.demo.travel.flights;

import com.modulewise.demo.travel.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class FlightBooking {

    private String id;
    private Flight flight;
    private Person passenger;
    private String bookingReference;
    private String seatNumber;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bookingTime;
    
    private String status; // CONFIRMED, CANCELLED, CHECKED_IN, etc.

    public FlightBooking() {}

    public FlightBooking(String id, Flight flight, Person passenger, String bookingReference, String seatNumber, LocalDateTime bookingTime, String status) {
        this.id = id;
        this.flight = flight;
        this.passenger = passenger;
        this.bookingReference = bookingReference;
        this.seatNumber = seatNumber;
        this.bookingTime = bookingTime;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Person getPassenger() {
        return passenger;
    }

    public void setPassenger(Person passenger) {
        this.passenger = passenger;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
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
