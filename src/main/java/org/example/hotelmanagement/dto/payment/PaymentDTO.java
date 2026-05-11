package org.example.hotelmanagement.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Integer paymentID;
    private Integer bookingID;
    private Double amount;
    private LocalDate paymentDate;
    private String paymentMethod;
}
