package com.autoinsight.autoinsight_api.controller;

import com.autoinsight.autoinsight_api.dto.*;
import com.autoinsight.autoinsight_api.service.SearchHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@Tag(name = "Histórico", description = "Histórico de buscas realizadas")
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @GetMapping
    @Operation(summary = "Listar últimas 10 buscas realizadas")
    public ResponseEntity<ApiResponseDTO<List<SearchHistoryResponseDTO>>> findRecent() {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        searchHistoryService.findRecent(),
                        "Histórico encontrado com sucesso"
                )
        );
    }

    @DeleteMapping
    @Operation(summary = "Limpar histórico de buscas")
    public ResponseEntity<ApiResponseDTO<Void>> deleteAll() {
        searchHistoryService.deleteAll();
        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Histórico limpo com sucesso")
        );
    }
}