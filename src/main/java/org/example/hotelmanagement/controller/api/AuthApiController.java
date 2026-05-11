package org.example.hotelmanagement.controller.api;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hotelmanagement.dto.api.LoginRequest;
import org.example.hotelmanagement.dto.api.LoginResponse;
import org.example.hotelmanagement.dto.api.RefreshRequest;
import org.example.hotelmanagement.dto.api.UserSummary;
import org.example.hotelmanagement.dto.auth.RegisterRequest;
import org.example.hotelmanagement.entity.User;
import org.example.hotelmanagement.mapper.user.UserMapper;
import org.example.hotelmanagement.security.jwt.JwtProperties;
import org.example.hotelmanagement.security.jwt.JwtService;
import org.example.hotelmanagement.service.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        addAuthCookies(response, accessToken, refreshToken);

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Người dùng không tồn tại"));

        return ResponseEntity.ok(LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresInMs(jwtService.getAccessExpirationMs())
                .user(userMapper.toSummary(user))
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserSummary> register(@Valid @RequestBody RegisterRequest request) {
        User created = userService.register(request);
        return ResponseEntity.ok(userMapper.toSummary(created));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody(required = false) RefreshRequest body,
                                                 jakarta.servlet.http.HttpServletRequest request,
                                                 HttpServletResponse response) {
        String refreshToken = body != null ? body.getRefreshToken() : null;
        if (refreshToken == null) {
            refreshToken = readCookie(request, jwtProperties.getRefreshCookieName());
        }
        if (refreshToken == null) {
            throw new JwtException("Thiếu refresh token");
        }
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new JwtException("Token không phải refresh token");
        }
        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtService.isValid(refreshToken, userDetails)) {
            throw new JwtException("Refresh token không hợp lệ hoặc đã hết hạn");
        }

        String newAccess = jwtService.generateAccessToken(userDetails);
        String newRefresh = jwtService.generateRefreshToken(userDetails);
        addAuthCookies(response, newAccess, newRefresh);

        User user = userService.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(LoginResponse.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .tokenType("Bearer")
                .expiresInMs(jwtService.getAccessExpirationMs())
                .user(userMapper.toSummary(user))
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        clearAuthCookies(response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserSummary> me(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Người dùng không tồn tại"));
        return ResponseEntity.ok(userMapper.toSummary(user));
    }

    private void addAuthCookies(HttpServletResponse response, String access, String refresh) {
        ResponseCookie accessCookie = ResponseCookie.from(jwtProperties.getCookieName(), access)
                .httpOnly(true).secure(false).sameSite("Lax").path("/")
                .maxAge(Duration.ofMillis(jwtService.getAccessExpirationMs()))
                .build();
        ResponseCookie refreshCookie = ResponseCookie.from(jwtProperties.getRefreshCookieName(), refresh)
                .httpOnly(true).secure(false).sameSite("Lax").path("/")
                .maxAge(Duration.ofMillis(jwtService.getRefreshExpirationMs()))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    private void clearAuthCookies(HttpServletResponse response) {
        ResponseCookie clearAccess = ResponseCookie.from(jwtProperties.getCookieName(), "")
                .httpOnly(true).secure(false).sameSite("Lax").path("/").maxAge(0).build();
        ResponseCookie clearRefresh = ResponseCookie.from(jwtProperties.getRefreshCookieName(), "")
                .httpOnly(true).secure(false).sameSite("Lax").path("/").maxAge(0).build();
        response.addHeader(HttpHeaders.SET_COOKIE, clearAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearRefresh.toString());
    }

    private String readCookie(jakarta.servlet.http.HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (jakarta.servlet.http.Cookie c : request.getCookies()) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }
}
