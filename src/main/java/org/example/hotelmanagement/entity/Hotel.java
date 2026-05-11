package org.example.hotelmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalTime;
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
@Table(name = "Hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HotelID")
    private int hotelID;

    @Column(name = "Name", length = 255)
    private String name;

    @Column(name = "Address", length = 255)
    private String address;

    @Column(name = "Phone", length = 15)
    private String phone;

    @Column(name = "Email", length = 255)
    private String email;

    @Column(name = "Stars")
    private Integer stars;

    @Column(name = "CheckinTime")
    private LocalTime checkinTime;

    @Column(name = "CheckoutTime")
    private LocalTime checkoutTime;

    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel")
    private List<Staff> staffList;
}

