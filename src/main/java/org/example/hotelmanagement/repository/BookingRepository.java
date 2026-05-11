package org.example.hotelmanagement.repository;

import org.example.hotelmanagement.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByGuest_GuestIDOrderByCheckinDateDesc(Integer guestID);

    List<Booking> findByRoom_Room(Integer roomNumber);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.room.room = :roomNumber
              AND b.checkoutDate > :checkin
              AND b.checkinDate < :checkout
            """)
    List<Booking> findOverlapping(@Param("roomNumber") Integer roomNumber,
                                  @Param("checkin") LocalDate checkin,
                                  @Param("checkout") LocalDate checkout);
}
