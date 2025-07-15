package com.modulewise.demo.travel.flights;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class FlightSearch {

    private String origin;
    private String destination;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departure;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrival;

    private int flex = 1;

    public FlightSearch() {}

    public FlightSearch(String id, String origin, String destination, LocalDateTime departure, LocalDateTime arrival, int flex) {
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.arrival = arrival;
        this.flex = flex;
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

    public int getFlex() {
        return flex;
    }

    public void setFlex(int flex) {
        this.flex = flex;
    }
}
