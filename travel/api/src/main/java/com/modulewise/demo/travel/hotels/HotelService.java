package com.modulewise.demo.travel.hotels;

import com.modulewise.demo.travel.Person;
import com.modulewise.demo.travel.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelBookingRepository hotelBookingRepository;
    private final PersonRepository personRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository, HotelBookingRepository hotelBookingRepository,
                       PersonRepository personRepository) {
        this.hotelRepository = hotelRepository;
        this.hotelBookingRepository = hotelBookingRepository;
        this.personRepository = personRepository;
    }

    public String createHotel(Hotel hotel) {
        Hotel savedHotel = hotelRepository.save(hotel);
        return savedHotel.getId();
    }

    public Hotel getHotelById(String id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        return hotel.orElse(null);
    }

    public List<Hotel> getAllHotels() {
        return StreamSupport.stream(hotelRepository.findAll().spliterator(), false)
                .toList();
    }

    public String createBooking(HotelBooking booking) {
        HotelBooking savedBooking = hotelBookingRepository.save(booking);
        return savedBooking.getId();
    }

    public HotelBooking getBookingById(String id) {
        Optional<HotelBooking> booking = hotelBookingRepository.findById(id);
        return booking.orElse(null);
    }

    public List<HotelBooking> getAllBookings() {
        return StreamSupport.stream(hotelBookingRepository.findAll().spliterator(), false)
                .toList();
    }

    public String createBookingFromIds(String hotelId, String personId, LocalDate checkin, LocalDate checkout) {
        Hotel hotel = getHotelById(hotelId);
        if (hotel == null) {
            throw new RuntimeException("Hotel not found: " + hotelId);
        }

        Optional<Person> personOpt = personRepository.findById(personId);
        if (personOpt.isEmpty()) {
            throw new RuntimeException("Person not found: " + personId);
        }
        Person person = personOpt.get();

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
    }

    private String generateBookingReference() {
        return "HBR" + System.currentTimeMillis() % 1000000;
    }

    public String createPerson(Person person) {
        Person savedPerson = personRepository.save(person);
        return savedPerson.getId();
    }

    public Person getPersonById(String id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }
}
