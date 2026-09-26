package com.autoinsight.autoinsight_api.service;

import com.autoinsight.autoinsight_api.dto.SpecificationResponseDTO;
import com.autoinsight.autoinsight_api.repository.SpecificationRepository;
import com.autoinsight.autoinsight_api.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class VehicleServiceTest {

    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        vehicleService = new VehicleService(
                mock(VehicleRepository.class),
                mock(SpecificationRepository.class)
        );
    }

    @Test
    void deveRetornarAtributosEncontradosNaOrdemSolicitada() {
        List<SpecificationResponseDTO> disponiveis = List.of(
                SpecificationResponseDTO.builder()
                        .id(8L)
                        .attributeName("Potência")
                        .attributeValue("250")
                        .build(),
                SpecificationResponseDTO.builder()
                        .id(9L)
                        .attributeName("Torque")
                        .attributeValue("600")
                        .build()
        );

        List<SpecificationResponseDTO> resultado =
                vehicleService.selectSpecifications(
                        disponiveis,
                        List.of("torque", " Potência ")
                );

        assertEquals(2, resultado.size());
        assertEquals("Torque", resultado.get(0).getAttributeName());
        assertEquals("600", resultado.get(0).getAttributeValue());
        assertEquals("Potência", resultado.get(1).getAttributeName());
        assertEquals("250", resultado.get(1).getAttributeValue());
    }

    @Test
    void deveInformarQuandoAtributoNaoEstaCadastrado() {
        List<SpecificationResponseDTO> resultado =
                vehicleService.selectSpecifications(
                        List.of(),
                        List.of("Cor do volante")
                );

        assertEquals(1, resultado.size());
        assertEquals("Cor do volante", resultado.get(0).getAttributeName());
        assertEquals("Não disponível", resultado.get(0).getAttributeValue());
        assertNull(resultado.get(0).getId());
    }

    @Test
    void deveIgnorarNomesVaziosERepetidos() {
        List<SpecificationResponseDTO> resultado =
                vehicleService.selectSpecifications(
                        List.of(),
                        List.of("", "  ", "Torque", "Torque")
                );

        assertEquals(1, resultado.size());
        assertEquals("Torque", resultado.get(0).getAttributeName());
    }
}