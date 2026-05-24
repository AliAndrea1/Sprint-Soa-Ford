package com.autoinsight.autoinsight_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "specifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "attribute_name", nullable = false, length = 150)
    private String attributeName;

    @Column(name = "attribute_value", length = 255)
    private String attributeValue;

    @Column(length = 50)
    private String unit;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
