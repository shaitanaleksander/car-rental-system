package org.ratifire.admin.carrentalsystem.dto;

import lombok.*;
import org.ratifire.admin.carrentalsystem.enums.CarType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {

    private Long id;
    private CarType carType;
    private String plateNumber;
}
