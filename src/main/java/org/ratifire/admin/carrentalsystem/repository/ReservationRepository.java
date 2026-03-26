package org.ratifire.admin.carrentalsystem.repository;

import org.ratifire.admin.carrentalsystem.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByCarPoolId(Long carPoolId);
}
