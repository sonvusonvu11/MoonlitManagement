package org.example.hotelmanagement.repository;

import org.example.hotelmanagement.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByRoomType_TypeID(Integer typeID);

    @Query("""
            SELECT r FROM Room r
            WHERE r.status <> 'MAINTENANCE'
              AND r.room NOT IN (
                  SELECT b.room.room FROM Booking b
                  WHERE b.checkoutDate > :checkin
                    AND b.checkinDate < :checkout
              )
            """)
    List<Room> findAvailableRooms(@Param("checkin") LocalDate checkin,
                                  @Param("checkout") LocalDate checkout);
}
