package org.example.hotelmanagement.controller.api;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.roomType.RoomTypeDTO;
import org.example.hotelmanagement.entity.RoomType;
import org.example.hotelmanagement.service.roomType.RoomTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeApiController {

    private final RoomTypeService roomTypeService;

    @GetMapping
    public List<RoomTypeDTO> list() {
        return roomTypeService.findAll().stream().map(this::toDTO).toList();
    }

    private RoomTypeDTO toDTO(RoomType rt) {
        return RoomTypeDTO.builder()
                .typeID(rt.getTypeID())
                .name(rt.getName())
                .description(rt.getDescription())
                .pricePerNight(rt.getPricePerNight())
                .capacity(rt.getCapacity())
                .build();
    }
}
