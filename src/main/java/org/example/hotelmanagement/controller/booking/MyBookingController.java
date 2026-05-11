package org.example.hotelmanagement.controller.booking;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.booking.BookingDTO;
import org.example.hotelmanagement.service.booking.BookingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyBookingController {

    private final BookingService bookingService;

    @GetMapping("/my-bookings")
    public String myBookings(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<BookingDTO> list = bookingService.findByUsername(userDetails.getUsername());
        model.addAttribute("bookings", list);
        return "booking/my-bookings";
    }
}
