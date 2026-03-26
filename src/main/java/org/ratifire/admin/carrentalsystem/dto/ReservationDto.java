package org.ratifire.admin.carrentalsystem.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private Long id;
    private Long userId;
    private Long carId;
    private LocalDateTime startDateTime;
    private Integer days;
}
