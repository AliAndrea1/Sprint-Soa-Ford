package com.autoinsight.autoinsight_api.controller;

import com.autoinsight.autoinsight_api.dto.ApiResponseDTO;
import com.autoinsight.autoinsight_api.model.AuditLog;
import com.autoinsight.autoinsight_api.repository.AuditLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Tag(name = "Auditoria", description = "Logs de auditoria das ações realizadas na API")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping
    @Operation(summary = "Listar últimos 20 logs de auditoria")
    public ResponseEntity<ApiResponseDTO<List<AuditLog>>> findRecent() {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        auditLogRepository.findTop20ByOrderByCreatedAtDesc(),
                        "Logs encontrados com sucesso"
                )
        );
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Buscar logs por usuário")
    public ResponseEntity<ApiResponseDTO<List<AuditLog>>> findByUser(
            @PathVariable String username) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        auditLogRepository.findByUsername(username),
                        "Logs encontrados com sucesso"
                )
        );
    }
}
