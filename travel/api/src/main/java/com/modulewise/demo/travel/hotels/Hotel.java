package com.modulewise.demo.travel.hotels;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("hotels")
public class Hotel {

    @Id
    private String id;
    private String name;
    private String city;
    private int stars;
    private String description;

    public Hotel() {}

    public Hotel(String id, String name, String city, int stars, String description) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.stars = stars;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
