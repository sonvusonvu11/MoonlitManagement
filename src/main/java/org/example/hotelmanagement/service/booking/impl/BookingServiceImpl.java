package org.example.hotelmanagement.service.booking.impl;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.booking.BookingDTO;
import org.example.hotelmanagement.dto.booking.BookingRequest;
import org.example.hotelmanagement.entity.Booking;
import org.example.hotelmanagement.entity.Guest;
import org.example.hotelmanagement.entity.Room;
import org.example.hotelmanagement.entity.User;
import org.example.hotelmanagement.mapper.booking.BookingMapper;
import org.example.hotelmanagement.repository.BookingRepository;
import org.example.hotelmanagement.repository.GuestRepository;
import org.example.hotelmanagement.repository.RoomRepository;
import org.example.hotelmanagement.repository.UserRepository;
import org.example.hotelmanagement.service.booking.BookingService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final GuestRepository guestRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDTO createBooking(BookingRequest request, String username) {
        validateDates(request.getCheckinDate(), request.getCheckoutDate());

        Room room = roomRepository.findById(request.getRoomNumber())
                .orElseThrow(() -> new IllegalArgumentException("Phòng không tồn tại"));

        if ("MAINTENANCE".equalsIgnoreCase(room.getStatus())) {
            throw new IllegalStateException("Phòng đang bảo trì, không thể đặt");
        }

        List<Booking> overlapping = bookingRepository.findOverlapping(
                room.getRoom(), request.getCheckinDate(), request.getCheckoutDate());
        if (!overlapping.isEmpty()) {
            throw new IllegalStateException("Phòng đã được đặt trong khoảng thời gian này");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Người dùng không tồn tại"));

        Guest guest = ensureGuestForUser(user);

        long nights = ChronoUnit.DAYS.between(request.getCheckinDate(), request.getCheckoutDate());
        double pricePerNight = room.getRoomType() != null && room.getRoomType().getPricePerNight() != null
                ? room.getRoomType().getPricePerNight()
                : 0.0;
        double totalPrice = nights * pricePerNight;

        Booking booking = Booking.builder()
                .guest(guest)
                .room(room)
                .checkinDate(request.getCheckinDate())
                .checkoutDate(request.getCheckoutDate())
                .totalPrice(totalPrice)
                .build();

        return bookingMapper.toDTO(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Người dùng không tồn tại"));
        if (user.getGuest() == null) {
            return List.of();
        }
        return bookingRepository
                .findByGuest_GuestIDOrderByCheckinDateDesc(user.getGuest().getGuestID())
                .stream().map(bookingMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> findAll() {
        return bookingRepository.findAll().stream().map(bookingMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> findEntityById(Integer bookingID) {
        return bookingRepository.findById(bookingID);
    }

    @Override
    public void cancelBooking(Integer bookingID, String username) {
        Booking booking = bookingRepository.findById(bookingID)
                .orElseThrow(() -> new IllegalArgumentException("Đơn đặt không tồn tại"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Người dùng không tồn tại"));

        boolean isAdmin = user.getRole() != null
                && (user.getRole().name().equals("ADMIN") || user.getRole().name().equals("STAFF"));
        boolean isOwner = user.getGuest() != null
                && booking.getGuest() != null
                && user.getGuest().getGuestID() == booking.getGuest().getGuestID();

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Bạn không có quyền huỷ đơn đặt này");
        }

        if (booking.getCheckinDate() != null && booking.getCheckinDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Không thể huỷ đơn đã qua ngày nhận phòng");
        }

        bookingRepository.delete(booking);
    }

    private void validateDates(LocalDate checkin, LocalDate checkout) {
        if (checkin == null || checkout == null) {
            throw new IllegalArgumentException("Vui lòng chọn ngày nhận và trả phòng");
        }
        if (!checkout.isAfter(checkin)) {
            throw new IllegalArgumentException("Ngày trả phòng phải sau ngày nhận phòng");
        }
        if (checkin.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày nhận phòng không thể ở quá khứ");
        }
    }

    private Guest ensureGuestForUser(User user) {
        if (user.getGuest() != null) {
            return user.getGuest();
        }
        String fullName = user.getFullName() != null ? user.getFullName().trim() : user.getUsername();
        String firstName = fullName;
        String lastName = "";
        int idx = fullName.lastIndexOf(' ');
        if (idx > 0) {
            firstName = fullName.substring(0, idx);
            lastName = fullName.substring(idx + 1);
        }
        Guest guest = Guest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
        guest = guestRepository.save(guest);
        user.setGuest(guest);
        userRepository.save(user);
        return guest;
    }
}
