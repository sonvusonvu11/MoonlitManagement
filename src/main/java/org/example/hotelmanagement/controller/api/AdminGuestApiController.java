package org.example.hotelmanagement.controller.api;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.guest.GuestDTO;
import org.example.hotelmanagement.entity.Guest;
import org.example.hotelmanagement.service.guest.GuestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/guests")
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
@RequiredArgsConstructor
public class AdminGuestApiController {

    private final GuestService guestService;

    @GetMapping
    public List<GuestDTO> all() {
        return guestService.findAll().stream().map(this::toDTO).toList();
    }

    private GuestDTO toDTO(Guest g) {
        return GuestDTO.builder()
                .guestID(g.getGuestID())
                .firstName(g.getFirstName())
                .lastName(g.getLastName())
                .dateOfBirth(g.getDateOfBirth())
                .address(g.getAddress())
                .phone(g.getPhone())
                .email(g.getEmail())
                .build();
    }
}
