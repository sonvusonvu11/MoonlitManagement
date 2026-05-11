package org.example.hotelmanagement.service.payment;

import org.example.hotelmanagement.dto.payment.PaymentDTO;
import org.example.hotelmanagement.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {

    PaymentDTO createPayment(Integer bookingID, Double amount, String paymentMethod);

    List<PaymentDTO> findAll();

    Optional<Payment> findById(Integer paymentID);
}
