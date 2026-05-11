package org.example.hotelmanagement.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.auth.RegisterRequest;
import org.example.hotelmanagement.dto.auth.UpdateProfileRequest;
import org.example.hotelmanagement.entity.Guest;
import org.example.hotelmanagement.entity.Role;
import org.example.hotelmanagement.entity.User;
import org.example.hotelmanagement.repository.GuestRepository;
import org.example.hotelmanagement.repository.UserRepository;
import org.example.hotelmanagement.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }

        Guest guest = createGuestFromRequest(request);
        guest = guestRepository.save(guest);

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(Role.CUSTOMER)
                .enabled(true)
                .guest(guest)
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateProfile(String username, UpdateProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Người dùng không tồn tại"));

        if (!user.getEmail().equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng bởi tài khoản khác");
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        Guest guest = user.getGuest();
        if (guest == null) {
            guest = new Guest();
        }
        String fullName = request.getFullName() != null ? request.getFullName().trim() : "";
        int idx = fullName.lastIndexOf(' ');
        if (idx > 0) {
            guest.setFirstName(fullName.substring(0, idx));
            guest.setLastName(fullName.substring(idx + 1));
        } else {
            guest.setFirstName(fullName);
            guest.setLastName("");
        }
        guest.setEmail(request.getEmail());
        guest.setPhone(request.getPhone());
        guest.setAddress(request.getAddress());
        guest.setDateOfBirth(request.getDateOfBirth());
        guest = guestRepository.save(guest);
        user.setGuest(guest);

        return userRepository.save(user);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Người dùng không tồn tại"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không đúng");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu mới tối thiểu 6 ký tự");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private Guest createGuestFromRequest(RegisterRequest request) {
        String fullName = request.getFullName() != null ? request.getFullName().trim() : request.getUsername();
        String firstName = fullName;
        String lastName = "";
        int idx = fullName.lastIndexOf(' ');
        if (idx > 0) {
            firstName = fullName.substring(0, idx);
            lastName = fullName.substring(idx + 1);
        }
        return Guest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
    }
}
