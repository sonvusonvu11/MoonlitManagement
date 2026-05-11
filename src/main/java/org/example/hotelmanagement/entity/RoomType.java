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
@Table(name = "RoomType")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TypeID")
    private int typeID;

    @Column(name = "Name", length = 50)
    private String name;

    @Column(name = "Description", length = 255)
    private String description;

    @Column(name = "PricePerNight")
    private Double pricePerNight;

    @Column(name = "Capacity")
    private Integer capacity;

    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;
}

