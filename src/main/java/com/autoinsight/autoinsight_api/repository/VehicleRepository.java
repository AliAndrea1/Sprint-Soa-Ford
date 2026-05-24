package com.autoinsight.autoinsight_api.repository;

import com.autoinsight.autoinsight_api.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByBrandIgnoreCase(String brand);

    List<Vehicle> findByBrandIgnoreCaseAndModelIgnoreCase(String brand, String model);

    Optional<Vehicle> findByBrandIgnoreCaseAndModelIgnoreCaseAndVersionIgnoreCase(
            String brand, String model, String version
    );

    boolean existsByBrandIgnoreCaseAndModelIgnoreCaseAndVersionIgnoreCase(
            String brand, String model, String version
    );
}
