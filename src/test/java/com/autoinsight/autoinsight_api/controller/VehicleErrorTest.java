package com.autoinsight.autoinsight_api.controller;

import com.autoinsight.autoinsight_api.dto.VehicleRequestDTO;
import com.autoinsight.autoinsight_api.exception.GlobalExceptionHandler;
import com.autoinsight.autoinsight_api.exception.VehicleAlreadyExistsException;
import com.autoinsight.autoinsight_api.exception.VehicleNotFoundException;
import com.autoinsight.autoinsight_api.service.SearchHistoryService;
import com.autoinsight.autoinsight_api.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VehicleErrorTest {

    private MockMvc mockMvc;
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        vehicleService = mock(VehicleService.class);

        VehicleController controller = new VehicleController(
                vehicleService,
                mock(SearchHistoryService.class)
        );

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void deveRetornar404QuandoVeiculoNaoExiste() throws Exception {
        when(vehicleService.findById(999L))
                .thenThrow(new VehicleNotFoundException(
                        "Veículo não encontrado com id: 999"
                ));

        mockMvc.perform(get("/api/vehicles/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Veículo não encontrado com id: 999"));
    }

    @Test
    void deveRetornar409QuandoVeiculoJaExiste() throws Exception {
        when(vehicleService.create(any(VehicleRequestDTO.class)))
                .thenThrow(new VehicleAlreadyExistsException(
                        "Veículo já cadastrado"
                ));

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "brand": "Ford",
                                  "model": "RANGER",
                                  "version": "XLT 3.0L V6 AT",
                                  "year": 2026,
                                  "specifications": []
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Veículo já cadastrado"));
    }
}