package com.modulewise.demo.travel;

import java.util.ArrayList;
import java.util.List;

public class Person {

    private String id;
    private String firstName;
    private String lastName;
    private List<LoyaltyProgram> flightLoyaltyPrograms;
    private List<LoyaltyProgram> hotelLoyaltyPrograms;

    public Person() {
        this.flightLoyaltyPrograms = new ArrayList<>();
        this.hotelLoyaltyPrograms = new ArrayList<>();
    }

    public Person(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.flightLoyaltyPrograms = new ArrayList<>();
        this.hotelLoyaltyPrograms = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<LoyaltyProgram> getFlightLoyaltyPrograms() {
        return flightLoyaltyPrograms;
    }

    public void setFlightLoyaltyPrograms(List<LoyaltyProgram> flightLoyaltyPrograms) {
        this.flightLoyaltyPrograms = flightLoyaltyPrograms;
    }

    public List<LoyaltyProgram> getHotelLoyaltyPrograms() {
        return hotelLoyaltyPrograms;
    }

    public void setHotelLoyaltyPrograms(List<LoyaltyProgram> hotelLoyaltyPrograms) {
        this.hotelLoyaltyPrograms = hotelLoyaltyPrograms;
    }
}
