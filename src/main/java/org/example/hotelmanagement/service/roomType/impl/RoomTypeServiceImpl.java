package org.example.hotelmanagement.service.roomType.impl;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.entity.RoomType;
import org.example.hotelmanagement.repository.RoomTypeRepository;
import org.example.hotelmanagement.service.roomType.RoomTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RoomType> findAll() {
        return roomTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomType> findById(Integer typeID) {
        return roomTypeRepository.findById(typeID);
    }

    @Override
    public RoomType save(RoomType roomType) {
        return roomTypeRepository.save(roomType);
    }

    @Override
    public void deleteById(Integer typeID) {
        roomTypeRepository.deleteById(typeID);
    }
}
