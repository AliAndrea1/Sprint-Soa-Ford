package com.autoinsight.autoinsight_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificationRequestDTO {

    @NotBlank(message = "Nome do atributo é obrigatório")
    private String attributeName;

    private String attributeValue;
    private String unit;
}
