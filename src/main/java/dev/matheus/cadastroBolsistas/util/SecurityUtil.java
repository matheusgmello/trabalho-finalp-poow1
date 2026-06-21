package dev.matheus.cadastroBolsistas.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * utilitario para seguranca de senhas do sistema
 * gera hash sha256 em formato hexadecimal
 */
public class SecurityUtil {

    public static String hashSenha(String senha) {
        if (senha == null) return null;
        try {
            /*
             * cria instancia do digest usando o algoritmo sha256 nativo
             */
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            /*
             * converte cada byte do hash para dois caracteres hexadecimais
             */
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro: Algoritmo SHA-256 nao suportado no ambiente JVM.", e);
        }
    }
}
