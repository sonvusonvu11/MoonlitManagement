package org.example.hotelmanagement.service.guest.impl;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.entity.Guest;
import org.example.hotelmanagement.repository.GuestRepository;
import org.example.hotelmanagement.service.guest.GuestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Guest> findAll() {
        return guestRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Guest> findById(Integer guestID) {
        return guestRepository.findById(guestID);
    }

    @Override
    public Guest save(Guest guest) {
        return guestRepository.save(guest);
    }

    @Override
    public void deleteById(Integer guestID) {
        guestRepository.deleteById(guestID);
    }
}
