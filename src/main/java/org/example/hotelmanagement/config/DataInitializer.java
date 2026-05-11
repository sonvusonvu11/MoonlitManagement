package org.example.hotelmanagement.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hotelmanagement.entity.Hotel;
import org.example.hotelmanagement.entity.Role;
import org.example.hotelmanagement.entity.Room;
import org.example.hotelmanagement.entity.RoomType;
import org.example.hotelmanagement.entity.User;
import org.example.hotelmanagement.repository.HotelRepository;
import org.example.hotelmanagement.repository.RoomRepository;
import org.example.hotelmanagement.repository.RoomTypeRepository;
import org.example.hotelmanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedHotelAndRooms();
    }

    private void seedAdmin() {
        if (userRepository.existsByUsername("admin")) {
            return;
        }
        User admin = User.builder()
                .username("admin")
                .email("admin@hotel.local")
                .password(passwordEncoder.encode("admin123"))
                .fullName("Quản trị viên")
                .role(Role.ADMIN)
                .enabled(true)
                .build();
        userRepository.save(admin);
        log.info(">>> Đã tạo tài khoản admin / admin123");
    }

    private void seedHotelAndRooms() {
        if (hotelRepository.count() > 0) {
            return;
        }

        Hotel hotel = Hotel.builder()
                .name("Moonlit Hotel & Resort")
                .address("280 Augusta Avenue, Toronto")
                .phone("0123456789")
                .email("contact@moonlit.local")
                .stars(5)
                .checkinTime(LocalTime.of(14, 0))
                .checkoutTime(LocalTime.of(12, 0))
                .build();
        hotel = hotelRepository.save(hotel);

        RoomType standard = roomTypeRepository.save(RoomType.builder()
                .name("Standard")
                .description("Phòng tiêu chuẩn 1 giường đôi, view thành phố")
                .pricePerNight(80.0)
                .capacity(2)
                .build());

        RoomType deluxe = roomTypeRepository.save(RoomType.builder()
                .name("Deluxe")
                .description("Phòng cao cấp 1 giường King, view biển")
                .pricePerNight(150.0)
                .capacity(2)
                .build());

        RoomType suite = roomTypeRepository.save(RoomType.builder()
                .name("Suite")
                .description("Phòng Suite có phòng khách riêng, dành cho gia đình")
                .pricePerNight(250.0)
                .capacity(4)
                .build());

        for (int i = 1; i <= 5; i++) {
            roomRepository.save(Room.builder()
                    .hotel(hotel)
                    .roomType(standard)
                    .status("AVAILABLE")
                    .build());
        }
        for (int i = 1; i <= 3; i++) {
            roomRepository.save(Room.builder()
                    .hotel(hotel)
                    .roomType(deluxe)
                    .status("AVAILABLE")
                    .build());
        }
        for (int i = 1; i <= 2; i++) {
            roomRepository.save(Room.builder()
                    .hotel(hotel)
                    .roomType(suite)
                    .status("AVAILABLE")
                    .build());
        }
        log.info(">>> Đã tạo dữ liệu mẫu: 1 khách sạn, 3 loại phòng, 10 phòng");
    }
}
