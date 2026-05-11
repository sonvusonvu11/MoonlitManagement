package org.example.hotelmanagement.service.roomType;

import org.example.hotelmanagement.entity.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomTypeService {
    List<RoomType> findAll();

    Optional<RoomType> findById(Integer typeID);

    RoomType save(RoomType roomType);

    void deleteById(Integer typeID);
}
