package com.autoinsight.autoinsight_api.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static final String SECRET =
            "chave-de-teste-com-mais-de-64-caracteres-para-assinatura-hs384-segura-123456";

    @Test
    void deveGerarTokenValidoComUsuarioEPerfil() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 60_000);

        String token = jwtUtil.generateToken("analyst", "ANALYST");

        assertTrue(jwtUtil.isTokenValid(token));
        assertEquals("analyst", jwtUtil.extractUsername(token));
        assertEquals("ANALYST", jwtUtil.extractRole(token));
    }

    @Test
    void deveRejeitarTokenExpirado() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, -60_000);

        String token = jwtUtil.generateToken("analyst", "ANALYST");

        assertFalse(jwtUtil.isTokenValid(token));
    }

    @Test
    void deveRejeitarTokenAssinadoComOutraChave() {
        JwtUtil emissor = new JwtUtil(SECRET, 60_000);
        JwtUtil validador = new JwtUtil(
                "outra-chave-de-teste-com-mais-de-64-caracteres-para-assinatura-hs384-123",
                60_000
        );

        String token = emissor.generateToken("admin", "ADMIN");

        assertFalse(validador.isTokenValid(token));
    }
}