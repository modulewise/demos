package com.modulewise.demo.travel.hotels;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modulewise.demo.travel.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class HotelService {

    private static final String HOTELS_HASH = "hotels";
    private static final String HOTEL_BOOKINGS_HASH = "hotel_bookings";
    private static final String PERSONS_HASH = "persons";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public HotelService(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public String createHotel(Hotel hotel) {
        try {
            String key = hotel.getId();
            String hotelJson = objectMapper.writeValueAsString(hotel);
            redisTemplate.opsForHash().put(HOTELS_HASH, key, hotelJson);
            return key;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing hotel", e);
        }
    }

    public String getHotelById(String id) {
        return (String) redisTemplate.opsForHash().get(HOTELS_HASH, id);
    }

    public Map<Object, Object> getAllHotels() {
        return redisTemplate.opsForHash().entries(HOTELS_HASH);
    }

    public String createBooking(HotelBooking booking) {
        try {
            String key = booking.getId();
            String bookingJson = objectMapper.writeValueAsString(booking);
            redisTemplate.opsForHash().put(HOTEL_BOOKINGS_HASH, key, bookingJson);
            return key;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing booking", e);
        }
    }

    public String getBookingById(String id) {
        return (String) redisTemplate.opsForHash().get(HOTEL_BOOKINGS_HASH, id);
    }

    public Map<Object, Object> getAllBookings() {
        return redisTemplate.opsForHash().entries(HOTEL_BOOKINGS_HASH);
    }

    public String createBookingFromIds(String hotelId, String personId, LocalDate checkin, LocalDate checkout) {
        try {
            String hotelJson = getHotelById(hotelId);
            if (hotelJson == null) {
                throw new RuntimeException("Hotel not found: " + hotelId);
            }
            Hotel hotel = objectMapper.readValue(hotelJson, Hotel.class);

            String personJson = (String) redisTemplate.opsForHash().get(PERSONS_HASH, personId);
            if (personJson == null) {
                throw new RuntimeException("Person not found: " + personId);
            }
            Person person = objectMapper.readValue(personJson, Person.class);

            String bookingId = UUID.randomUUID().toString();
            String bookingReference = generateBookingReference();

            HotelBooking booking = new HotelBooking(
                bookingId,
                hotel,
                person,
                bookingReference,
                null, // room number - to be assigned later
                "Standard", // default room type
                checkin,
                checkout,
                LocalDateTime.now(),
                "CONFIRMED"
            );
            return createBooking(booking);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing booking creation", e);
        }
    }

    private String generateBookingReference() {
        return "HBR" + System.currentTimeMillis() % 1000000;
    }

    public String createPerson(Person person) {
        try {
            String key = person.getId();
            String personJson = objectMapper.writeValueAsString(person);
            redisTemplate.opsForHash().put(PERSONS_HASH, key, personJson);
            return key;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing person", e);
        }
    }

    public String getPersonById(String id) {
        return (String) redisTemplate.opsForHash().get(PERSONS_HASH, id);
    }
}
