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
@Table(name = "Booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingID")
    private int bookingID;

    @ManyToOne
    @JoinColumn(name = "GuestID")
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "RoomNumber")
    private Room room;

    @Column(name = "CheckinDate")
    private LocalDate checkinDate;

    @Column(name = "CheckoutDate")
    private LocalDate checkoutDate;

    @Column(name = "TotalPrice")
    private Double totalPrice;

    @OneToMany(mappedBy = "booking")
    private List<Payment> payments;
}

