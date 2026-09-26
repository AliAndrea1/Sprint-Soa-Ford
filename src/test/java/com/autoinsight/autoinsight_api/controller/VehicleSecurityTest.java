package com.autoinsight.autoinsight_api.controller;

import com.autoinsight.autoinsight_api.config.SecurityConfig;
import com.autoinsight.autoinsight_api.repository.AuditLogRepository;
import com.autoinsight.autoinsight_api.security.AuditLogFilter;
import com.autoinsight.autoinsight_api.security.JwtFilter;
import com.autoinsight.autoinsight_api.security.JwtUtil;
import com.autoinsight.autoinsight_api.security.RateLimitFilter;
import com.autoinsight.autoinsight_api.service.SearchHistoryService;
import com.autoinsight.autoinsight_api.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleController.class)
@Import({
        SecurityConfig.class,
        JwtFilter.class,
        JwtUtil.class,
        RateLimitFilter.class,
        AuditLogFilter.class
})
@TestPropertySource(properties = {
        "app.jwt.secret=chave-de-teste-com-mais-de-64-caracteres-para-assinatura-hs384-segura-123456",
        "app.jwt.expiration=60000"
})
class VehicleSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockitoBean
    private VehicleService vehicleService;

    @MockitoBean
    private SearchHistoryService searchHistoryService;

    @MockitoBean
    private AuditLogRepository auditLogRepository;

    @Test
    void deveRetornar401QuandoNaoHaToken() throws Exception {
        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void devePermitirConsultaParaAnalyst() throws Exception {
        when(vehicleService.findAll()).thenReturn(List.of());

        String token = jwtUtil.generateToken("analyst", "ANALYST");

        mockMvc.perform(get("/api/vehicles")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void deveProibirCadastroParaAnalyst() throws Exception {
        String token = jwtUtil.generateToken("analyst", "ANALYST");

        mockMvc.perform(post("/api/vehicles")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "brand": "Ford",
                                  "model": "TestePermissao",
                                  "version": "Sprint3",
                                  "year": 2026,
                                  "specifications": []
                                }
                                """))
                .andExpect(status().isForbidden());
    }
}