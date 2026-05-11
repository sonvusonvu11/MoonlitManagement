package org.example.hotelmanagement.controller.room;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.room.RoomDTO;
import org.example.hotelmanagement.service.room.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public String listRooms(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkin,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkout,
                            Model model) {
        List<RoomDTO> rooms;
        if (checkin != null && checkout != null && checkout.isAfter(checkin)) {
            rooms = roomService.findAvailable(checkin, checkout);
            model.addAttribute("filtered", true);
        } else {
            rooms = roomService.findAll();
            model.addAttribute("filtered", false);
        }
        model.addAttribute("rooms", rooms);
        model.addAttribute("checkin", checkin);
        model.addAttribute("checkout", checkout);
        return "rooms/list";
    }

    @GetMapping("/{roomNumber}")
    public String roomDetail(@PathVariable Integer roomNumber, Model model) {
        RoomDTO room = roomService.findById(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng"));
        model.addAttribute("room", room);
        return "rooms/detail";
    }
}
