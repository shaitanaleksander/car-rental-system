package org.ratifire.admin.carrentalsystem.repository;

import org.ratifire.admin.carrentalsystem.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByCarId(Long carId);

    @Query(value = "SELECT COUNT(*) > 0 FROM reservations r " +
            "WHERE r.car_id = :carId " +
            "AND r.start_date_time < :endDateTime " +
            "AND :startDateTime < r.start_date_time + (r.days || ' days')::interval",
            nativeQuery = true)
    boolean existsOverlappingReservation(
            @Param("carId") Long carId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
}
