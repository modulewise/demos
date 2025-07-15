package com.modulewise.demo.travel.flights;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modulewise.demo.travel.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class FlightService {

    private static final String FLIGHTS_HASH = "flights";
    private static final String BOOKINGS_HASH = "flight_bookings";
    private static final String PERSONS_HASH = "persons";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public FlightService(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public String createFlight(Flight flight) {
        try {
            String key = flight.getId();
            String flightJson = objectMapper.writeValueAsString(flight);
            redisTemplate.opsForHash().put(FLIGHTS_HASH, key, flightJson);
            return key;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing flight", e);
        }
    }

    public String getFlightById(String id) {
        return (String) redisTemplate.opsForHash().get(FLIGHTS_HASH, id);
    }

    public Map<Object, Object> getAllFlights() {
        return redisTemplate.opsForHash().entries(FLIGHTS_HASH);
    }

    public String createBooking(FlightBooking booking) {
        try {
            String key = booking.getId();
            String bookingJson = objectMapper.writeValueAsString(booking);
            redisTemplate.opsForHash().put(BOOKINGS_HASH, key, bookingJson);
            return key;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing booking", e);
        }
    }

    public String getBookingById(String id) {
        return (String) redisTemplate.opsForHash().get(BOOKINGS_HASH, id);
    }

    public Map<Object, Object> getAllBookings() {
        return redisTemplate.opsForHash().entries(BOOKINGS_HASH);
    }

    public String createBookingFromIds(String flightId, String personId) {
        try {
            String flightJson = getFlightById(flightId);
            if (flightJson == null) {
                throw new RuntimeException("Flight not found: " + flightId);
            }
            Flight flight = objectMapper.readValue(flightJson, Flight.class);

            String personJson = (String) redisTemplate.opsForHash().get(PERSONS_HASH, personId);
            if (personJson == null) {
                throw new RuntimeException("Person not found: " + personId);
            }
            Person person = objectMapper.readValue(personJson, Person.class);

            String bookingId = UUID.randomUUID().toString();
            String bookingReference = generateBookingReference();

            FlightBooking booking = new FlightBooking(
                bookingId,
                flight,
                person,
                bookingReference,
                null, // seat number - to be assigned later
                LocalDateTime.now(),
                "CONFIRMED"
            );
            return createBooking(booking);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing booking creation", e);
        }
    }

    private String generateBookingReference() {
        return "FBR" + System.currentTimeMillis() % 1000000;
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
