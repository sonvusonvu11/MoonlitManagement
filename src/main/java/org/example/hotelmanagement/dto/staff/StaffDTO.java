package org.example.hotelmanagement.dto.staff;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {
    private Integer staffID;
    private Integer hotelID;
    private String firstName;
    private String lastName;
    private String position;
    private Double salary;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
    private LocalDate hireDate;
}
