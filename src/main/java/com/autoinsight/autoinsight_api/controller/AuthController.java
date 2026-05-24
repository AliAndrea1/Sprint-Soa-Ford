package com.autoinsight.autoinsight_api.controller;

import com.autoinsight.autoinsight_api.dto.*;
import com.autoinsight.autoinsight_api.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de autenticação e geração de token JWT")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private static final Map<String, String[]> USERS = new HashMap<>();

    @jakarta.annotation.PostConstruct
    public void init() {
        USERS.put("admin", new String[]{
                passwordEncoder.encode("admin123"),
                "ADMIN"
        });
        USERS.put("analyst", new String[]{
                passwordEncoder.encode("analyst123"),
                "ANALYST"
        });
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login e obter token JWT")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        String[] userInfo = USERS.get(dto.getUsername());

        if (userInfo == null || !passwordEncoder.matches(dto.getPassword(), userInfo[0])) {
            log.warn("[AUTH] Tentativa de login falha para usuário: {}", dto.getUsername());
            return ResponseEntity.status(401).body(
                    ApiResponseDTO.error("Usuário ou senha inválidos")
            );
        }

        log.info("[AUTH] Login realizado com sucesso para usuário: {}", dto.getUsername());
        String token = jwtUtil.generateToken(dto.getUsername(), userInfo[1]);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        LoginResponseDTO.builder()
                                .token(token)
                                .username(dto.getUsername())
                                .role(userInfo[1])
                                .message("Login realizado com sucesso")
                                .build(),
                        "Login realizado com sucesso"
                )
        );
    }
}