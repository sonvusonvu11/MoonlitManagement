package org.example.hotelmanagement.dto.guest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestDTO {
    private Integer guestID;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private String phone;
    private String email;
}
