package dev.matheus.cadastroBolsistas.util;

public class StringUtil {

    public static String limpar(String valor) {
        return valor != null ? valor.trim() : "";
    }

    public static boolean estaVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
