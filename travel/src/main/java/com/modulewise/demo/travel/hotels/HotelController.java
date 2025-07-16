package com.modulewise.demo.travel.hotels;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hotels")
@Tag(name = "Hotels", description = "Hotel management operations")
public class HotelController {

    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);
    private final HotelService hotelService;
    private final ObjectMapper objectMapper;

    @Autowired
    public HotelController(HotelService hotelService, ObjectMapper objectMapper) {
        this.hotelService = hotelService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    @Operation(summary = "Get all hotels", description = "Retrieves all available hotels")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved hotels")
    public String getHotels() {
        logger.info("GET /hotels called");
        try {
            Map<Object, Object> hotels = hotelService.getAllHotels();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(hotels);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing hotels", e);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get hotel by ID", description = "Retrieves a specific hotel by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved hotel")
    @ApiResponse(responseCode = "404", description = "Hotel not found")
    public String getHotelById(@Parameter(description = "Hotel ID") @PathVariable String id) {
        logger.info("GET /hotels/{} called", id);
        String hotelJson = hotelService.getHotelById(id);
        if (hotelJson == null) {
            return "{}";
        }
        return hotelJson;
    }

    @PostMapping
    @Operation(summary = "Create hotels", description = "Creates one or more hotels")
    @ApiResponse(responseCode = "200", description = "Successfully created hotels")
    @ApiResponse(responseCode = "400", description = "Invalid hotel data")
    public ResponseEntity<String> postHotels(@RequestBody Object hotelData) {
        logger.info("POST /hotels called");
        try {
            if (hotelData instanceof List) {
                List<Hotel> hotels = objectMapper.convertValue(hotelData, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Hotel.class));
                List<String> createdIds = createHotels(hotels);
                return ResponseEntity.ok("Created " + createdIds.size() + " hotels with IDs: " + createdIds);
            } else {
                Hotel hotel = objectMapper.convertValue(hotelData, Hotel.class);
                String hotelId = hotelService.createHotel(hotel);
                return ResponseEntity.ok("Created hotel with ID: " + hotelId);
            }
        } catch (Exception e) {
            logger.error("Error creating hotels", e);
            return ResponseEntity.badRequest().body("Error creating hotels: " + e.getMessage());
        }
    }

    private List<String> createHotels(List<Hotel> hotels) {
        List<String> createdIds = new ArrayList<>();
        for (Hotel hotel : hotels) {
            String hotelId = hotelService.createHotel(hotel);
            createdIds.add(hotelId);
        }
        return createdIds;
    }

    @PostMapping("/search")
    @Operation(summary = "Search hotels", description = "Search for hotels based on criteria")
    @ApiResponse(responseCode = "200", description = "Successfully found matching hotels")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<List<Hotel>> searchHotels(@RequestBody HotelSearch hotelSearch) {
        logger.info("POST /hotels/search called");
        try {
            List<Hotel> matchingHotels = new ArrayList<>();
            Map<Object, Object> allHotels = hotelService.getAllHotels();

            for (Map.Entry<Object, Object> entry : allHotels.entrySet()) {
                String hotelJson = (String) entry.getValue();
                Hotel hotel = objectMapper.readValue(hotelJson, Hotel.class);

                if (isHotelMatch(hotel, hotelSearch)) {
                    matchingHotels.add(hotel);
                }
            }
            return ResponseEntity.ok(matchingHotels);
        } catch (Exception e) {
            logger.error("Error searching hotels", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean isHotelMatch(Hotel hotel, HotelSearch search) {
        if (!hotel.getCity().equalsIgnoreCase(search.getCity())) {
            return false;
        }
        if (hotel.getStars() < search.getMinStars()) {
            return false;
        }
        // For now, we're not checking availability dates since we don't have room inventory
        // In a real system, we would check room availability for the checkin/checkout dates
        return true;
    }

    @PostMapping("/bookings")
    @Operation(summary = "Create hotel booking", description = "Creates a new hotel booking")
    @ApiResponse(responseCode = "200", description = "Successfully created booking")
    @ApiResponse(responseCode = "400", description = "Invalid booking data")
    public ResponseEntity<String> createBooking(@RequestBody Object bookingData) {
        logger.info("POST /hotels/bookings called");
        try {
            if (bookingData instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> requestMap = (Map<String, Object>) bookingData;
                if (requestMap.containsKey("hotelId") && requestMap.containsKey("personId") 
                    && requestMap.containsKey("checkin") && requestMap.containsKey("checkout")) {
                    String hotelId = (String) requestMap.get("hotelId");
                    String personId = (String) requestMap.get("personId");
                    LocalDate checkin = LocalDate.parse((String) requestMap.get("checkin"));
                    LocalDate checkout = LocalDate.parse((String) requestMap.get("checkout"));
                    String bookingId = hotelService.createBookingFromIds(hotelId, personId, checkin, checkout);
                    return ResponseEntity.ok("Created booking with ID: " + bookingId);
                }
            }
            // Fall back to full booking object
            HotelBooking booking = objectMapper.convertValue(bookingData, HotelBooking.class);
            String bookingId = hotelService.createBooking(booking);
            return ResponseEntity.ok("Created booking with ID: " + bookingId);
        } catch (Exception e) {
            logger.error("Error creating booking", e);
            return ResponseEntity.badRequest().body("Error creating booking: " + e.getMessage());
        }
    }

    @GetMapping("/bookings")
    @Operation(summary = "Get all hotel bookings", description = "Retrieves all hotel bookings")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved bookings")
    public String getBookings() {
        logger.info("GET /hotels/bookings called");
        try {
            Map<Object, Object> bookings = hotelService.getAllBookings();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookings);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing bookings", e);
        }
    }

    @GetMapping("/bookings/{id}")
    @Operation(summary = "Get hotel booking by ID", description = "Retrieves a specific hotel booking by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved booking")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    public String getBookingById(@Parameter(description = "Booking ID") @PathVariable String id) {
        logger.info("GET /hotels/bookings/{} called", id);
        String bookingJson = hotelService.getBookingById(id);
        if (bookingJson == null) {
            return "{}";
        }
        return bookingJson;
    }
}
