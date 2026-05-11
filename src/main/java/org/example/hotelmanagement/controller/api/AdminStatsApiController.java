package org.example.hotelmanagement.controller.api;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.service.booking.BookingService;
import org.example.hotelmanagement.service.guest.GuestService;
import org.example.hotelmanagement.service.room.RoomService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
@RequiredArgsConstructor
public class AdminStatsApiController {

    private final BookingService bookingService;
    private final RoomService roomService;
    private final GuestService guestService;

    @GetMapping
    public Map<String, Object> stats() {
        return Map.of(
                "totalBookings", bookingService.findAll().size(),
                "totalRooms", roomService.findAll().size(),
                "totalGuests", guestService.findAll().size()
        );
    }
}
