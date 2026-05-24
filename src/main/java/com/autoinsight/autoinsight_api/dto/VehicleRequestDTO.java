package com.autoinsight.autoinsight_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleRequestDTO {

    @NotBlank(message = "Marca é obrigatória")
    private String brand;

    @NotBlank(message = "Modelo é obrigatório")
    private String model;

    @NotBlank(message = "Versão é obrigatória")
    private String version;

    @NotNull(message = "Ano é obrigatório")
    private Integer year;

    private List<SpecificationRequestDTO> specifications;
}
