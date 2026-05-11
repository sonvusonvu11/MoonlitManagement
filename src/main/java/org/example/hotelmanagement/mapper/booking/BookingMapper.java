package org.example.hotelmanagement.mapper.booking;

import org.example.hotelmanagement.dto.booking.BookingDTO;
import org.example.hotelmanagement.entity.Booking;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class BookingMapper {

    public BookingDTO toDTO(Booking booking) {
        if (booking == null) return null;
        long nights = 0;
        if (booking.getCheckinDate() != null && booking.getCheckoutDate() != null) {
            nights = ChronoUnit.DAYS.between(booking.getCheckinDate(), booking.getCheckoutDate());
        }
        return BookingDTO.builder()
                .bookingID(booking.getBookingID())
                .roomNumber(booking.getRoom() != null ? booking.getRoom().getRoom() : null)
                .roomTypeName(booking.getRoom() != null && booking.getRoom().getRoomType() != null
                        ? booking.getRoom().getRoomType().getName()
                        : null)
                .guestName(booking.getGuest() != null
                        ? (booking.getGuest().getFirstName() + " " + booking.getGuest().getLastName()).trim()
                        : null)
                .checkinDate(booking.getCheckinDate())
                .checkoutDate(booking.getCheckoutDate())
                .nights(nights)
                .totalPrice(booking.getTotalPrice())
                .build();
    }
}
