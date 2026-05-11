package org.example.hotelmanagement.service.user;

import org.example.hotelmanagement.dto.auth.RegisterRequest;
import org.example.hotelmanagement.dto.auth.UpdateProfileRequest;
import org.example.hotelmanagement.entity.User;

import java.util.Optional;

public interface UserService {

    User register(RegisterRequest request);

    Optional<User> findByUsername(String username);

    User updateProfile(String username, UpdateProfileRequest request);

    void changePassword(String username, String oldPassword, String newPassword);
}
