package com.autoinsight.autoinsight_api.controller;

import com.autoinsight.autoinsight_api.dto.*;
import com.autoinsight.autoinsight_api.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Validated
@Tag(name = "Veículos", description = "Gerenciamento de veículos e especificações técnicas")
public class VehicleController {

    private final VehicleService vehicleService;
    private final SearchHistoryService searchHistoryService;

    @GetMapping
    @Operation(summary = "Listar todos os veículos")
    public ResponseEntity<ApiResponseDTO<List<VehicleResponseDTO>>> findAll() {
        return ResponseEntity.ok(
                ApiResponseDTO.success(vehicleService.findAll(), "Veículos encontrados com sucesso")
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veículo por ID")
    public ResponseEntity<ApiResponseDTO<VehicleResponseDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(vehicleService.findById(id), "Veículo encontrado com sucesso")
        );
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar veículo por marca, modelo e versão")
    public ResponseEntity<ApiResponseDTO<VehicleResponseDTO>> search(
            @RequestParam @Size(max = 100, message = "Marca deve ter no máximo 100 caracteres") String brand,
            @RequestParam @Size(max = 100, message = "Modelo deve ter no máximo 100 caracteres") String model,
            @RequestParam @Size(max = 100, message = "Versão deve ter no máximo 100 caracteres") String version) {

        searchHistoryService.save(brand, model, version);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        vehicleService.findByBrandModelVersion(brand, model, version),
                        "Veículo encontrado com sucesso"
                )
        );
    }

    @GetMapping("/brand/{brand}")
    @Operation(summary = "Buscar veículos por marca")
    public ResponseEntity<ApiResponseDTO<List<VehicleResponseDTO>>> findByBrand(
            @PathVariable String brand) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        vehicleService.findByBrand(brand),
                        "Veículos encontrados com sucesso"
                )
        );
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo veículo com especificações")
    public ResponseEntity<ApiResponseDTO<VehicleResponseDTO>> create(
            @Valid @RequestBody VehicleRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(
                        vehicleService.create(dto),
                        "Veículo cadastrado com sucesso"
                ));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veículo existente")
    public ResponseEntity<ApiResponseDTO<VehicleResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody VehicleRequestDTO dto) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        vehicleService.update(id, dto),
                        "Veículo atualizado com sucesso"
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar veículo")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Veículo deletado com sucesso")
        );
    }
}
