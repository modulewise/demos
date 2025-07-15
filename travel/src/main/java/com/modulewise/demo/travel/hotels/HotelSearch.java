package com.modulewise.demo.travel.hotels;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class HotelSearch {

    private String city;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkin;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkout;

    private int flex = 0; // flexibility in days
    private int minStars = 1; // minimum star rating

    public HotelSearch() {}

    public HotelSearch(String city, LocalDate checkin, LocalDate checkout, int flex, int minStars) {
        this.city = city;
        this.checkin = checkin;
        this.checkout = checkout;
        this.flex = flex;
        this.minStars = minStars;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public int getFlex() {
        return flex;
    }

    public void setFlex(int flex) {
        this.flex = flex;
    }

    public int getMinStars() {
        return minStars;
    }

    public void setMinStars(int minStars) {
        this.minStars = minStars;
    }
}
