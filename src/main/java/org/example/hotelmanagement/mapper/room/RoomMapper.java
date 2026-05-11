package org.example.hotelmanagement.mapper.room;

import org.example.hotelmanagement.dto.room.RoomDTO;
import org.example.hotelmanagement.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomDTO toDTO(Room room) {
        if (room == null) return null;
        return RoomDTO.builder()
                .roomNumber(room.getRoom())
                .hotelID(room.getHotel() != null ? room.getHotel().getHotelID() : null)
                .hotelName(room.getHotel() != null ? room.getHotel().getName() : null)
                .typeID(room.getRoomType() != null ? room.getRoomType().getTypeID() : null)
                .typeName(room.getRoomType() != null ? room.getRoomType().getName() : null)
                .typeDescription(room.getRoomType() != null ? room.getRoomType().getDescription() : null)
                .pricePerNight(room.getRoomType() != null ? room.getRoomType().getPricePerNight() : null)
                .capacity(room.getRoomType() != null ? room.getRoomType().getCapacity() : null)
                .status(room.getStatus())
                .build();
    }
}
