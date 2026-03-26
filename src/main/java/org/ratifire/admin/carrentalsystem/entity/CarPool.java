package org.ratifire.admin.carrentalsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car_pools")
public class CarPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String carType;

    @Column(nullable = false, unique = true)
    private String plateNumber;
}
