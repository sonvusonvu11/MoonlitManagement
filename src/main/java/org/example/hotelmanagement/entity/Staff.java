package org.example.hotelmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StaffID")
    private int staffID;

    @ManyToOne
    @JoinColumn(name = "HotelID")
    private Hotel hotel;

    @Column(name = "FirstName", length = 50)
    private String firstName;

    @Column(name = "LastName", length = 50)
    private String lastName;

    @Column(name = "Position", length = 50)
    private String position;

    @Column(name = "Salary")
    private Double salary;

    @Column(name = "DateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "Phone", length = 15)
    private String phone;

    @Column(name = "Email", length = 255)
    private String email;

    @Column(name = "HireDate")
    private LocalDate hireDate;
}

