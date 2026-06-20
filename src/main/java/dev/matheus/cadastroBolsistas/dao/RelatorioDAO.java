package dev.matheus.cadastroBolsistas.dao;

import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RelatorioDAO {

    public List<Map<String, Object>> getHorasBolsistasMesCorrente() throws SQLException {
        String sql = "SELECT b.nome, SUM(f.horas_trabalhadas) as total_horas " +
                     "FROM frequencia f " +
                     "JOIN bolsista b ON f.bolsista_id = b.id " +
                     "WHERE f.ativo = true AND b.ativo = true " +
                     "  AND EXTRACT(MONTH FROM f.data) = EXTRACT(MONTH FROM CURRENT_DATE) " +
                     "  AND EXTRACT(YEAR FROM f.data) = EXTRACT(YEAR FROM CURRENT_DATE) " +
                     "GROUP BY b.nome " +
                     "ORDER BY total_horas DESC";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Map<String, Object>> lista = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("nome", rs.getString("nome"));
                map.put("totalHoras", rs.getDouble("total_horas"));
                lista.add(map);
            }
            return lista;
        }
    }

    public List<Map<String, Object>> getProjetosAtivosPorLaboratorio() throws SQLException {
        String sql = "SELECT l.nome, COUNT(p.id) as total_projetos " +
                     "FROM laboratorio l " +
                     "LEFT JOIN projeto p ON p.laboratorio_id = l.id AND p.ativo = true " +
                     "WHERE l.ativo = true " +
                     "GROUP BY l.nome " +
                     "ORDER BY total_projetos DESC";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Map<String, Object>> lista = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("nome", rs.getString("nome"));
                map.put("totalProjetos", rs.getLong("total_projetos"));
                lista.add(map);
            }
            return lista;
        }
    }

    public List<Map<String, Object>> getBolsistasPorCargo() throws SQLException {
        String sql = "SELECT cargo, COUNT(*) as total_bolsistas " +
                     "FROM bolsista " +
                     "WHERE ativo = true AND cargo IS NOT NULL " +
                     "GROUP BY cargo " +
                     "ORDER BY total_bolsistas DESC";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Map<String, Object>> lista = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("cargo", rs.getString("cargo"));
                map.put("totalBolsistas", rs.getLong("total_bolsistas"));
                lista.add(map);
            }
            return lista;
        }
    }

    public List<Map<String, Object>> getLaboratoriosOcupacao() throws SQLException {
        String sql = "SELECT l.id, l.nome, l.capacidade, COUNT(b.id) as total_bolsistas " +
                     "FROM laboratorio l " +
                     "LEFT JOIN bolsista b ON b.laboratorio_id = l.id AND b.ativo = true " +
                     "WHERE l.ativo = true " +
                     "GROUP BY l.id, l.nome, l.capacidade " +
                     "ORDER BY l.nome";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Map<String, Object>> lista = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("nome", rs.getString("nome"));
                map.put("capacidade", rs.getInt("capacidade"));
                map.put("totalBolsistas", rs.getInt("total_bolsistas"));
                double percentual = 0.0;
                int cap = rs.getInt("capacidade");
                if (cap > 0) {
                    percentual = (rs.getInt("total_bolsistas") / (double) cap) * 100.0;
                }
                map.put("percentualOcupacao", percentual);
                lista.add(map);
            }
            return lista;
        }
    }
}
