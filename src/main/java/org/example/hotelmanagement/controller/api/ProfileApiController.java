package org.example.hotelmanagement.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.api.ChangePasswordRequest;
import org.example.hotelmanagement.dto.api.UserSummary;
import org.example.hotelmanagement.dto.auth.UpdateProfileRequest;
import org.example.hotelmanagement.entity.User;
import org.example.hotelmanagement.mapper.user.UserMapper;
import org.example.hotelmanagement.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileApiController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public UserSummary getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Người dùng không tồn tại"));
        return userMapper.toSummary(user);
    }

    @PutMapping
    public UserSummary updateProfile(@Valid @RequestBody UpdateProfileRequest request,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.updateProfile(userDetails.getUsername(), request);
        return userMapper.toSummary(user);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        userService.changePassword(userDetails.getUsername(), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }
}
