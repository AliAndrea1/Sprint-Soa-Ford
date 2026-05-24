package com.autoinsight.autoinsight_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "action", length = 50)
    private String action;

    @Column(name = "endpoint", length = 200)
    private String endpoint;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
