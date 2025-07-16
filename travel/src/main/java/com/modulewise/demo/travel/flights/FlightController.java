package com.modulewise.demo.travel.flights;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flights")
@Tag(name = "Flights", description = "Flight management operations")
public class FlightController {

    private static final Logger logger = LoggerFactory.getLogger(FlightController.class);
    private final FlightService flightService;
    private final ObjectMapper objectMapper;

    @Autowired
    public FlightController(FlightService flightService, ObjectMapper objectMapper) {
        this.flightService = flightService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    @Operation(summary = "Get all flights", description = "Retrieves all available flights")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved flights")
    public String getFlights() {
        logger.info("GET /flights called");
        try {
            Map<Object, Object> flights = flightService.getAllFlights();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(flights);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing flights", e);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get flight by ID", description = "Retrieves a specific flight by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved flight")
    @ApiResponse(responseCode = "404", description = "Flight not found")
    public String getFlightById(@Parameter(description = "Flight ID") @PathVariable String id) {
        logger.info("GET /flights/{} called", id);
        String flightJson = flightService.getFlightById(id);
        if (flightJson == null) {
            return "{}";
        }
        return flightJson;
    }

    @PostMapping
    @Operation(summary = "Create flights", description = "Creates one or more flights")
    @ApiResponse(responseCode = "200", description = "Successfully created flights")
    @ApiResponse(responseCode = "400", description = "Invalid flight data")
    public ResponseEntity<String> postFlights(@RequestBody Object flightData) {
        logger.info("POST /flights called");
        try {
            if (flightData instanceof List) {
                List<Flight> flights = objectMapper.convertValue(flightData, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Flight.class));
                List<String> createdIds = createFlights(flights);
                return ResponseEntity.ok("Created " + createdIds.size() + " flights with IDs: " + createdIds);
            } else {
                Flight flight = objectMapper.convertValue(flightData, Flight.class);
                String flightId = flightService.createFlight(flight);
                return ResponseEntity.ok("Created flight with ID: " + flightId);
            }
        } catch (Exception e) {
            logger.error("Error creating flights", e);
            return ResponseEntity.badRequest().body("Error creating flights: " + e.getMessage());
        }
    }

    private List<String> createFlights(List<Flight> flights) {
        List<String> createdIds = new ArrayList<>();
        for (Flight flight : flights) {
            String flightId = flightService.createFlight(flight);
            createdIds.add(flightId);
        }
        return createdIds;
    }

    @PostMapping("/search")
    @Operation(summary = "Search flights", description = "Search for flights based on criteria")
    @ApiResponse(responseCode = "200", description = "Successfully found matching flights")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<List<Flight>> searchFlights(@RequestBody FlightSearch flightSearch) {
        logger.info("POST /flights/search called");
        try {
            List<Flight> matchingFlights = new ArrayList<>();
            Map<Object, Object> allFlights = flightService.getAllFlights();
            for (Map.Entry<Object, Object> entry : allFlights.entrySet()) {
                String flightJson = (String) entry.getValue();
                Flight flight = objectMapper.readValue(flightJson, Flight.class);
                if (isFlightMatch(flight, flightSearch)) {
                    matchingFlights.add(flight);
                }
            }
            return ResponseEntity.ok(matchingFlights);
        } catch (Exception e) {
            logger.error("Error searching flights", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean isFlightMatch(Flight flight, FlightSearch search) {
        if (!flight.getOrigin().equalsIgnoreCase(search.getOrigin()) ||
            !flight.getDestination().equalsIgnoreCase(search.getDestination())) {
            return false;
        }
        if (search.getDeparture() != null) {
            if (!isTimeWithinFlex(flight.getDeparture(), search.getDeparture(), search.getFlex())) {
                return false;
            }
        }
        if (search.getArrival() != null) {
            if (!isTimeWithinFlex(flight.getArrival(), search.getArrival(), search.getFlex())) {
                return false;
            }
        }
        return true;
    }

    private boolean isTimeWithinFlex(LocalDateTime flightTime, LocalDateTime searchTime, int flexHours) {
        LocalDateTime earliestAcceptable = searchTime.minusHours(flexHours);
        LocalDateTime latestAcceptable = searchTime.plusHours(flexHours);        
        return !flightTime.isBefore(earliestAcceptable) && !flightTime.isAfter(latestAcceptable);
    }

    @PostMapping("/bookings")
    @Operation(summary = "Create flight booking", description = "Creates a new flight booking")
    @ApiResponse(responseCode = "200", description = "Successfully created booking")
    @ApiResponse(responseCode = "400", description = "Invalid booking data")
    public ResponseEntity<String> createBooking(@RequestBody Object bookingData) {
        logger.info("POST /flights/bookings called");
        try {
            if (bookingData instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> requestMap = (Map<String, Object>) bookingData;
                if (requestMap.containsKey("flightId") && requestMap.containsKey("personId")) {
                    String flightId = (String) requestMap.get("flightId");
                    String personId = (String) requestMap.get("personId");
                    String bookingId = flightService.createBookingFromIds(flightId, personId);
                    return ResponseEntity.ok("Created booking with ID: " + bookingId);
                }
            }
            // Fall back to full booking object
            FlightBooking booking = objectMapper.convertValue(bookingData, FlightBooking.class);
            String bookingId = flightService.createBooking(booking);
            return ResponseEntity.ok("Created booking with ID: " + bookingId);
        } catch (Exception e) {
            logger.error("Error creating booking", e);
            return ResponseEntity.badRequest().body("Error creating booking: " + e.getMessage());
        }
    }

    @GetMapping("/bookings")
    @Operation(summary = "Get all flight bookings", description = "Retrieves all flight bookings")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved bookings")
    public String getBookings() {
        logger.info("GET /flights/bookings called");
        try {
            Map<Object, Object> bookings = flightService.getAllBookings();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookings);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing bookings", e);
        }
    }

    @GetMapping("/bookings/{id}")
    @Operation(summary = "Get flight booking by ID", description = "Retrieves a specific flight booking by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved booking")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    public String getBookingById(@Parameter(description = "Booking ID") @PathVariable String id) {
        logger.info("GET /flights/bookings/{} called", id);
        String bookingJson = flightService.getBookingById(id);
        if (bookingJson == null) {
            return "{}";
        }
        return bookingJson;
    }
}
