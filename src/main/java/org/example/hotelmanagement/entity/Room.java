package org.example.hotelmanagement.entity;

import jakarta.persistence.*;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Room")
    private int room;

    @ManyToOne
    @JoinColumn(name = "HotelID")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "TypeID")
    private RoomType roomType;

    @Column(name = "Status", length = 20)
    private String status;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;
}

