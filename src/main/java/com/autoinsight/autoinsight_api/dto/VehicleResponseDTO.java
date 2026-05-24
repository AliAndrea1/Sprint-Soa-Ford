package com.autoinsight.autoinsight_api.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponseDTO {

    private Long id;
    private String brand;
    private String model;
    private String version;
    private Integer year;
    private List<SpecificationResponseDTO> specifications;
    private LocalDateTime createdAt;
}
