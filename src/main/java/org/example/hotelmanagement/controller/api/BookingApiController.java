package org.example.hotelmanagement.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.booking.BookingDTO;
import org.example.hotelmanagement.dto.booking.BookingRequest;
import org.example.hotelmanagement.service.booking.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingApiController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDTO> create(@Valid @RequestBody BookingRequest request,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        BookingDTO dto = bookingService.createBooking(request, userDetails.getUsername());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me")
    public List<BookingDTO> myBookings(@AuthenticationPrincipal UserDetails userDetails) {
        return bookingService.findByUsername(userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Integer id,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        bookingService.cancelBooking(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
