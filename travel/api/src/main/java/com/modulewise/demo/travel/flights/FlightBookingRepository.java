package com.modulewise.demo.travel.flights;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightBookingRepository extends CrudRepository<FlightBooking, String> {
}
