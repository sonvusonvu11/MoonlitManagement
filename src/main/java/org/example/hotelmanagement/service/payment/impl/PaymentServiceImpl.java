package org.example.hotelmanagement.service.payment.impl;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.payment.PaymentDTO;
import org.example.hotelmanagement.entity.Booking;
import org.example.hotelmanagement.entity.Payment;
import org.example.hotelmanagement.mapper.payment.PaymentMapper;
import org.example.hotelmanagement.repository.BookingRepository;
import org.example.hotelmanagement.repository.PaymentRepository;
import org.example.hotelmanagement.service.payment.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentDTO createPayment(Integer bookingID, Double amount, String paymentMethod) {
        Booking booking = bookingRepository.findById(bookingID)
                .orElseThrow(() -> new IllegalArgumentException("Đơn đặt không tồn tại"));
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(amount)
                .paymentDate(LocalDate.now())
                .paymentMethod(paymentMethod)
                .build();
        return paymentMapper.toDTO(paymentRepository.save(payment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> findAll() {
        return paymentRepository.findAll().stream().map(paymentMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findById(Integer paymentID) {
        return paymentRepository.findById(paymentID);
    }
}
