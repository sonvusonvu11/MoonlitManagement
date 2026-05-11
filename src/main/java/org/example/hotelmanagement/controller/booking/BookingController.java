package org.example.hotelmanagement.controller.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.booking.BookingDTO;
import org.example.hotelmanagement.dto.booking.BookingRequest;
import org.example.hotelmanagement.dto.room.RoomDTO;
import org.example.hotelmanagement.service.booking.BookingService;
import org.example.hotelmanagement.service.room.RoomService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final RoomService roomService;

    @GetMapping("/new")
    public String bookingForm(@RequestParam Integer roomNumber,
                              @RequestParam(required = false) LocalDate checkin,
                              @RequestParam(required = false) LocalDate checkout,
                              Model model) {
        RoomDTO room = roomService.findById(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng"));

        BookingRequest request = new BookingRequest();
        request.setRoomNumber(roomNumber);
        request.setCheckinDate(checkin != null ? checkin : LocalDate.now().plusDays(1));
        request.setCheckoutDate(checkout != null ? checkout : LocalDate.now().plusDays(2));

        model.addAttribute("room", room);
        model.addAttribute("bookingRequest", request);
        return "booking/form";
    }

    @PostMapping("/create")
    public String createBooking(@Valid @ModelAttribute("bookingRequest") BookingRequest request,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            roomService.findById(request.getRoomNumber()).ifPresent(r -> model.addAttribute("room", r));
            return "booking/form";
        }
        try {
            BookingDTO booking = bookingService.createBooking(request, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("success",
                    "Đặt phòng thành công! Mã đơn: #" + booking.getBookingID());
            return "redirect:/my-bookings";
        } catch (RuntimeException ex) {
            roomService.findById(request.getRoomNumber()).ifPresent(r -> model.addAttribute("room", r));
            bindingResult.reject("bookingError", ex.getMessage());
            return "booking/form";
        }
    }

    @GetMapping("/cancel")
    public String cancel(@RequestParam Integer bookingID,
                         @AuthenticationPrincipal UserDetails userDetails,
                         RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(bookingID, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("success", "Đã huỷ đơn đặt phòng");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/my-bookings";
    }
}
