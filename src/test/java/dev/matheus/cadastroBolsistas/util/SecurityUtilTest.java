package dev.matheus.cadastroBolsistas.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SecurityUtilTest {

    @Test
    void hashSenha_retornaHashCorretoParaSenhaConhecida() {
        String hash = SecurityUtil.hashSenha("teste123");
        assertEquals("289160db0d9f39f9ae1754c4ec9c16f90b50e32e09c5fb5481ae642b3d3d1a36", hash);
    }

    @Test
    void hashSenha_retornaSempre64Caracteres() {
        String hash = SecurityUtil.hashSenha("qualquer");
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void hashSenha_eDeterministico() {
        String hash1 = SecurityUtil.hashSenha("abc");
        String hash2 = SecurityUtil.hashSenha("abc");
        assertEquals(hash1, hash2);
    }

    @Test
    void hashSenha_senhasDiferentesGeramHashesDiferentes() {
        String hash1 = SecurityUtil.hashSenha("senha1");
        String hash2 = SecurityUtil.hashSenha("senha2");
        assertNotEquals(hash1, hash2);
    }

    @Test
    void hashSenha_retornaNullParaInputNull() {
        assertNull(SecurityUtil.hashSenha(null));
    }
}
