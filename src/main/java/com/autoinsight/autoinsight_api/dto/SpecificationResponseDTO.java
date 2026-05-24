package com.autoinsight.autoinsight_api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificationResponseDTO {

    private Long id;
    private String attributeName;
    private String attributeValue;
    private String unit;
}
