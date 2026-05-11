package org.example.hotelmanagement.dto.roomType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDTO {
    private Integer typeID;
    private String name;
    private String description;
    private Double pricePerNight;
    private Integer capacity;
}
