package com.autoinsight.autoinsight_api.service;

import com.autoinsight.autoinsight_api.dto.*;
import com.autoinsight.autoinsight_api.model.*;
import com.autoinsight.autoinsight_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final SpecificationRepository specificationRepository;

    public List<VehicleResponseDTO> findAll() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VehicleResponseDTO findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com id: " + id));
        return toResponseDTO(vehicle);
    }

    public VehicleResponseDTO findByBrandModelVersion(String brand, String model, String version) {
        Vehicle vehicle = vehicleRepository
                .findByBrandIgnoreCaseAndModelIgnoreCaseAndVersionIgnoreCase(brand, model, version)
                .orElseThrow(() -> new RuntimeException(
                        "Veículo não encontrado: " + brand + " " + model + " " + version
                ));
        return toResponseDTO(vehicle);
    }

    public List<VehicleResponseDTO> findByBrand(String brand) {
        return vehicleRepository.findByBrandIgnoreCase(brand)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VehicleResponseDTO create(VehicleRequestDTO dto) {
        if (vehicleRepository.existsByBrandIgnoreCaseAndModelIgnoreCaseAndVersionIgnoreCase(
                dto.getBrand(), dto.getModel(), dto.getVersion())) {
            throw new RuntimeException(
                    "Veículo já cadastrado: " + dto.getBrand() + " " + dto.getModel() + " " + dto.getVersion()
            );
        }

        Vehicle vehicle = Vehicle.builder()
                .brand(dto.getBrand())
                .model(dto.getModel())
                .version(dto.getVersion())
                .year(dto.getYear())
                .build();

        Vehicle saved = vehicleRepository.save(vehicle);

        if (dto.getSpecifications() != null) {
            List<Specification> specs = dto.getSpecifications().stream()
                    .map(specDTO -> Specification.builder()
                            .vehicle(saved)
                            .attributeName(specDTO.getAttributeName())
                            .attributeValue(specDTO.getAttributeValue() != null
                                    ? specDTO.getAttributeValue() : "Não disponível")
                            .unit(specDTO.getUnit())
                            .build())
                    .collect(Collectors.toList());
            specificationRepository.saveAll(specs);
            saved.setSpecifications(specs);
        }

        return toResponseDTO(saved);
    }

    @Transactional
    public VehicleResponseDTO update(Long id, VehicleRequestDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com id: " + id));

        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setVersion(dto.getVersion());
        vehicle.setYear(dto.getYear());

        if (dto.getSpecifications() != null) {
            specificationRepository.deleteByVehicleId(id);
            List<Specification> specs = dto.getSpecifications().stream()
                    .map(specDTO -> Specification.builder()
                            .vehicle(vehicle)
                            .attributeName(specDTO.getAttributeName())
                            .attributeValue(specDTO.getAttributeValue() != null
                                    ? specDTO.getAttributeValue() : "Não disponível")
                            .unit(specDTO.getUnit())
                            .build())
                    .collect(Collectors.toList());
            specificationRepository.saveAll(specs);
            vehicle.setSpecifications(specs);
        }

        return toResponseDTO(vehicleRepository.save(vehicle));
    }

    @Transactional
    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Veículo não encontrado com id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    private VehicleResponseDTO toResponseDTO(Vehicle vehicle) {
        List<SpecificationResponseDTO> specs = vehicle.getSpecifications() != null
                ? vehicle.getSpecifications().stream()
                .map(spec -> SpecificationResponseDTO.builder()
                        .id(spec.getId())
                        .attributeName(spec.getAttributeName())
                        .attributeValue(spec.getAttributeValue())
                        .unit(spec.getUnit())
                        .build())
                .collect(Collectors.toList())
                : List.of();

        return VehicleResponseDTO.builder()
                .id(vehicle.getId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .version(vehicle.getVersion())
                .year(vehicle.getYear())
                .specifications(specs)
                .createdAt(vehicle.getCreatedAt())
                .build();
    }
}
