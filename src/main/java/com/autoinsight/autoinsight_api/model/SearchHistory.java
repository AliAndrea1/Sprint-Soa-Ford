package com.autoinsight.autoinsight_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String brand;

    @Column(length = 200)
    private String model;

    @Column(length = 200)
    private String version;

    @Column(name = "searched_at")
    private LocalDateTime searchedAt;

    @PrePersist
    public void prePersist() {
        this.searchedAt = LocalDateTime.now();
    }
}