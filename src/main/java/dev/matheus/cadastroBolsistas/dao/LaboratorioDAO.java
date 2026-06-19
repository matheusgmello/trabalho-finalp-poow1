package dev.matheus.cadastroBolsistas.dao;

import dev.matheus.cadastroBolsistas.model.Laboratorio;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class LaboratorioDAO {

    public LaboratorioDAO() {}

    public boolean inserir(Laboratorio lab) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO laboratorio (nome, area_pesquisa, status, capacidade, coordenador_id, ativo) " +
                         "VALUES ('" + lab.getNome() + "', '" + lab.getAreaPesquisa() + "', '" +
                         lab.getStatus() + "', " + lab.getCapacidade() + ", " + 
                         (lab.getCoordenadorId() > 0 ? lab.getCoordenadorId() : "NULL") + ", " + lab.isAtivo() + ")";
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lab.setId(rs.getInt(1));
                }
            }
            return true;
        }
    }

    public ArrayList<Laboratorio> getLaboratorios() throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Laboratorio> laboratorios = new ArrayList<>();
            String sql = "SELECT l.*, p.nome as coordenador FROM laboratorio l " +
                         "LEFT JOIN professor p ON l.coordenador_id = p.id " +
                         "WHERE l.ativo = true ORDER BY l.nome";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                laboratorios.add(extrairLaboratorio(rs));
            }
            return laboratorios;
        }
    }

    public ArrayList<Laboratorio> getLaboratoriosPorCoordenador(int professorId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Laboratorio> laboratorios = new ArrayList<>();
            String sql = "SELECT l.*, p.nome as coordenador FROM laboratorio l " +
                         "LEFT JOIN professor p ON l.coordenador_id = p.id " +
                         "WHERE l.coordenador_id = " + professorId + " AND l.ativo = true ORDER BY l.nome";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                laboratorios.add(extrairLaboratorio(rs));
            }
            return laboratorios;
        }
    }

    public Laboratorio getLaboratorioPorId(int id) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT l.*, p.nome as coordenador FROM laboratorio l " +
                         "LEFT JOIN professor p ON l.coordenador_id = p.id " +
                         "WHERE l.id = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return extrairLaboratorio(rs);
            }
            return null;
        }
    }

    public boolean atualizar(Laboratorio lab) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "UPDATE laboratorio SET " +
                         "nome = '" + lab.getNome() + "', " +
                         "area_pesquisa = '" + lab.getAreaPesquisa() + "', " +
                         "status = '" + lab.getStatus() + "', " +
                         "capacidade = " + lab.getCapacidade() + ", " +
                         "coordenador_id = " + (lab.getCoordenadorId() > 0 ? lab.getCoordenadorId() : "NULL") + ", " +
                         "ativo = " + lab.isAtivo() + " " +
                         "WHERE id = " + lab.getId();
            stmt.execute(sql);
            return true;
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            // soft delete: set active to false
            stmt.execute("UPDATE laboratorio SET ativo = false WHERE id = " + id);
            return true;
        }
    }

    public int contarBolsistasNoLaboratorio(int labId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            // only count active bolsistas
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM bolsista WHERE laboratorio_id = " + labId + " AND ativo = true");
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    private Laboratorio extrairLaboratorio(ResultSet rs) throws SQLException {
        Laboratorio lab = new Laboratorio();
        lab.setId(rs.getInt("id"));
        lab.setNome(rs.getString("nome"));
        lab.setAreaPesquisa(rs.getString("area_pesquisa"));
        lab.setStatus(rs.getString("status"));
        lab.setCapacidade(rs.getInt("capacidade"));
        lab.setCoordenadorId(rs.getInt("coordenador_id"));
        lab.setCoordenador(rs.getString("coordenador"));
        lab.setAtivo(rs.getBoolean("ativo"));
        return lab;
    }
}
