package com.autoinsight.autoinsight_api.repository;

import com.autoinsight.autoinsight_api.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findTop20ByOrderByCreatedAtDesc();
    List<AuditLog> findByUsername(String username);
    List<AuditLog> findByStatusCode(Integer statusCode);
}
