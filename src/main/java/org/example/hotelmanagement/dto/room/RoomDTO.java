package org.example.hotelmanagement.dto.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Integer roomNumber;
    private Integer hotelID;
    private String hotelName;
    private Integer typeID;
    private String typeName;
    private String typeDescription;
    private Double pricePerNight;
    private Integer capacity;
    private String status;
}
