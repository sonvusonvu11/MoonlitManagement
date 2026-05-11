package org.example.hotelmanagement.mapper.user;

import org.example.hotelmanagement.dto.api.UserSummary;
import org.example.hotelmanagement.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserSummary toSummary(User user) {
        if (user == null) return null;
        return UserSummary.builder()
                .userID(user.getUserID())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .build();
    }
}
