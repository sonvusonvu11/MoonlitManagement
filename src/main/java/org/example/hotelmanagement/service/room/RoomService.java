package org.example.hotelmanagement.service.room;

import org.example.hotelmanagement.dto.room.RoomDTO;
import org.example.hotelmanagement.entity.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomService {

    List<RoomDTO> findAll();

    Optional<RoomDTO> findById(Integer roomNumber);

    Room getEntity(Integer roomNumber);

    List<RoomDTO> findAvailable(LocalDate checkin, LocalDate checkout);

    RoomDTO save(Room room);

    void deleteById(Integer roomNumber);
}
