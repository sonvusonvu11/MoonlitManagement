package org.example.hotelmanagement.service.room.impl;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.room.RoomDTO;
import org.example.hotelmanagement.entity.Room;
import org.example.hotelmanagement.mapper.room.RoomMapper;
import org.example.hotelmanagement.repository.RoomRepository;
import org.example.hotelmanagement.service.room.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoomDTO> findAll() {
        return roomRepository.findAll().stream().map(roomMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomDTO> findById(Integer roomNumber) {
        return roomRepository.findById(roomNumber).map(roomMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Room getEntity(Integer roomNumber) {
        return roomRepository.findById(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng số " + roomNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDTO> findAvailable(LocalDate checkin, LocalDate checkout) {
        if (checkin == null || checkout == null || !checkout.isAfter(checkin)) {
            throw new IllegalArgumentException("Ngày trả phòng phải sau ngày nhận phòng");
        }
        return roomRepository.findAvailableRooms(checkin, checkout)
                .stream().map(roomMapper::toDTO).toList();
    }

    @Override
    public RoomDTO save(Room room) {
        return roomMapper.toDTO(roomRepository.save(room));
    }

    @Override
    public void deleteById(Integer roomNumber) {
        roomRepository.deleteById(roomNumber);
    }
}
