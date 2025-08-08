package com.modulewise.demo.travel.flights;

import com.modulewise.demo.travel.Person;
import com.modulewise.demo.travel.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightBookingRepository flightBookingRepository;
    private final PersonRepository personRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository, FlightBookingRepository flightBookingRepository,
                        PersonRepository personRepository) {
        this.flightRepository = flightRepository;
        this.flightBookingRepository = flightBookingRepository;
        this.personRepository = personRepository;
    }

    public String createFlight(Flight flight) {
        Flight savedFlight = flightRepository.save(flight);
        return savedFlight.getId();
    }

    public Flight getFlightById(String id) {
        Optional<Flight> flight = flightRepository.findById(id);
        return flight.orElse(null);
    }

    public List<Flight> getAllFlights() {
        try {
            return StreamSupport.stream(flightRepository.findAll().spliterator(), false)
                    .toList();
        } catch (Exception e) {
            System.err.println("Error in getAllFlights: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve flights", e);
        }
    }

    public String createBooking(FlightBooking booking) {
        FlightBooking savedBooking = flightBookingRepository.save(booking);
        return savedBooking.getId();
    }

    public FlightBooking getBookingById(String id) {
        Optional<FlightBooking> booking = flightBookingRepository.findById(id);
        return booking.orElse(null);
    }

    public List<FlightBooking> getAllBookings() {
        return StreamSupport.stream(flightBookingRepository.findAll().spliterator(), false)
                .toList();
    }

    public String createBookingFromIds(String flightId, String personId) {
        Flight flight = getFlightById(flightId);
        if (flight == null) {
            throw new RuntimeException("Flight not found: " + flightId);
        }

        Optional<Person> personOpt = personRepository.findById(personId);
        if (personOpt.isEmpty()) {
            throw new RuntimeException("Person not found: " + personId);
        }
        Person person = personOpt.get();

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
    }

    private String generateBookingReference() {
        return "FBR" + System.currentTimeMillis() % 1000000;
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
