package org.example.hotelmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AppUser",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_username", columnNames = "Username"),
                @UniqueConstraint(name = "uk_user_email", columnNames = "Email")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Integer userID;

    @Column(name = "Username", length = 50, nullable = false)
    private String username;

    @Column(name = "Email", length = 255, nullable = false)
    private String email;

    @Column(name = "Password", length = 255, nullable = false)
    private String password;

    @Column(name = "FullName", length = 100)
    private String fullName;

    @Column(name = "Phone", length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", length = 20, nullable = false)
    private Role role;

    @Column(name = "Enabled", nullable = false)
    private boolean enabled;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GuestID")
    private Guest guest;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (role == null) {
            role = Role.CUSTOMER;
        }
    }
}
