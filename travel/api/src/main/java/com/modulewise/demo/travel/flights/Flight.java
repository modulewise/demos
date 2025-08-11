package com.modulewise.demo.travel.flights;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.time.LocalDateTime;

@RedisHash("flights")
public class Flight {

    @Id
    private String id;
    private String airline;
    private String number;
    private String origin;
    private String destination;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departure;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrival;

    public Flight() {}

    public Flight(String id, String airline, String number, String origin, String destination, LocalDateTime departure, LocalDateTime arrival) {
        this.id = id;
        this.airline = airline;
        this.number = number;
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.arrival = arrival;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalDateTime arrival) {
        this.arrival = arrival;
    }
}
