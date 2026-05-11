package org.example.hotelmanagement.service.guest;

import org.example.hotelmanagement.entity.Guest;

import java.util.List;
import java.util.Optional;

public interface GuestService {

    List<Guest> findAll();

    Optional<Guest> findById(Integer guestID);

    Guest save(Guest guest);

    void deleteById(Integer guestID);
}
