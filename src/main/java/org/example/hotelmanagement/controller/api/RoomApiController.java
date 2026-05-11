package org.example.hotelmanagement.controller.api;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.room.RoomDTO;
import org.example.hotelmanagement.service.room.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomService roomService;

    @GetMapping
    public List<RoomDTO> list() {
        return roomService.findAll();
    }

    @GetMapping("/{roomNumber}")
    public ResponseEntity<RoomDTO> get(@PathVariable Integer roomNumber) {
        return roomService.findById(roomNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/available")
    public List<RoomDTO> available(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkin,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkout) {
        return roomService.findAvailable(checkin, checkout);
    }
}
