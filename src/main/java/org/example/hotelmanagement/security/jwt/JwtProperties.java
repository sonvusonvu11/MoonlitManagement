package org.example.hotelmanagement.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secret;
    private long expirationMs = 3_600_000L;
    private long refreshExpirationMs = 604_800_000L;
    private String cookieName = "JWT_TOKEN";
    private String refreshCookieName = "JWT_REFRESH";
}
