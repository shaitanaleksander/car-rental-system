package org.ratifire.admin.carrentalsystem.repository;

import org.ratifire.admin.carrentalsystem.entity.CarPool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarPoolRepository extends JpaRepository<CarPool, Long> {

    List<CarPool> findByCarType(String carType);
}
