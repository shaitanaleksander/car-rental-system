package org.ratifire.admin.carrentalsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ratifire.admin.carrentalsystem.enums.CarType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarType carType;

    @Column(nullable = false, unique = true)
    private String plateNumber;
}
