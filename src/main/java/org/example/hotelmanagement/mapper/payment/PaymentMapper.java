package org.example.hotelmanagement.mapper.payment;

import org.example.hotelmanagement.dto.payment.PaymentDTO;
import org.example.hotelmanagement.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentDTO toDTO(Payment payment) {
        if (payment == null) return null;
        return PaymentDTO.builder()
                .paymentID(payment.getPaymentID())
                .bookingID(payment.getBooking() != null ? payment.getBooking().getBookingID() : null)
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod())
                .build();
    }
}
