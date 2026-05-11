package org.example.hotelmanagement.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Integer bookingID;
    private Integer roomNumber;
    private String roomTypeName;
    private String guestName;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Long nights;
    private Double totalPrice;
}
