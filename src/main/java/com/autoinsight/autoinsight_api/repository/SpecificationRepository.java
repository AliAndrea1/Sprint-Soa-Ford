package com.autoinsight.autoinsight_api.repository;

import com.autoinsight.autoinsight_api.model.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpecificationRepository extends JpaRepository<Specification, Long> {

    List<Specification> findByVehicleId(Long vehicleId);

    void deleteByVehicleId(Long vehicleId);
}
