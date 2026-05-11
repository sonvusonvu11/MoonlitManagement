package org.example.hotelmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
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
@Table(name = "Guest")
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GuestID")
    private int guestID;

    @Column(name = "FirstName", length = 50)
    private String firstName;

    @Column(name = "LastName", length = 50)
    private String lastName;

    @Column(name = "DateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "Address", length = 255)
    private String address;

    @Column(name = "Phone", length = 15)
    private String phone;

    @Column(name = "Email", length = 255)
    private String email;

    @OneToMany(mappedBy = "guest")
    private List<Booking> bookings;
}

