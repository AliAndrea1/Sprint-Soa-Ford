package com.autoinsight.autoinsight_api.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchHistoryResponseDTO {

    private Long id;
    private String brand;
    private String model;
    private String version;
    private LocalDateTime searchedAt;
}
