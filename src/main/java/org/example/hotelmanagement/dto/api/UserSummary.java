package org.example.hotelmanagement.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummary {
    private Integer userID;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String role;
}
