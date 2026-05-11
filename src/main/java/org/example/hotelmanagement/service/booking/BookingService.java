package org.example.hotelmanagement.service.booking;

import org.example.hotelmanagement.dto.booking.BookingDTO;
import org.example.hotelmanagement.dto.booking.BookingRequest;
import org.example.hotelmanagement.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    BookingDTO createBooking(BookingRequest request, String username);

    List<BookingDTO> findByUsername(String username);

    List<BookingDTO> findAll();

    Optional<Booking> findEntityById(Integer bookingID);

    void cancelBooking(Integer bookingID, String username);
}
