package com.modulewise.demo.travel.hotels;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelBookingRepository extends CrudRepository<HotelBooking, String> {
}
