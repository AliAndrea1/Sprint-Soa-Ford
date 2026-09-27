package com.autoinsight.autoinsight_api.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CryptoUtilsTest {

    @Test
    void devePermitirRecuperarHistoricoCriptografado() {
        CryptoUtils crypto = new CryptoUtils("1234567890123456");
        String encrypted = crypto.encrypt("Ford Ranger");
        assertEquals("Ford Ranger", crypto.decrypt(encrypted));
    }

    @Test
    void naoDeveDevolverTextoOriginalQuandoCriptografiaFalha() {
        CryptoUtils crypto = new CryptoUtils("chave-invalida");
        assertThrows(IllegalStateException.class, () -> crypto.encrypt("dado sensivel"));
    }

    @Test
    void naoDeveDevolverValorCorrompidoQuandoDescriptografiaFalha() {
        CryptoUtils crypto = new CryptoUtils("1234567890123456");
        assertThrows(IllegalStateException.class, () -> crypto.decrypt("dado-invalido"));
    }
}
