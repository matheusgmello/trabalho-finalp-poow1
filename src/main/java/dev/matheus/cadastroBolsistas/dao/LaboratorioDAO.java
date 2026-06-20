package dev.matheus.cadastroBolsistas.dao;

import dev.matheus.cadastroBolsistas.model.Laboratorio;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class LaboratorioDAO {

    public LaboratorioDAO() {}

    public boolean inserir(Laboratorio lab) throws SQLException {
        String sql = "INSERT INTO laboratorio (nome, area_pesquisa, status, capacidade, coordenador_id, ativo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, lab.getNome());
            stmt.setString(2, lab.getAreaPesquisa());
            stmt.setString(3, lab.getStatus());
            stmt.setInt(4, lab.getCapacidade());
            if (lab.getCoordenadorId() > 0) {
                stmt.setInt(5, lab.getCoordenadorId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setBoolean(6, lab.isAtivo());
            
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lab.setId(rs.getInt(1));
                }
            }
            return true;
        }
    }

    public ArrayList<Laboratorio> getLaboratorios() throws SQLException {
        String sql = "SELECT l.*, p.nome as coordenador FROM laboratorio l " +
                     "LEFT JOIN professor p ON l.coordenador_id = p.id " +
                     "WHERE l.ativo = true ORDER BY l.nome";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            ArrayList<Laboratorio> laboratorios = new ArrayList<>();
            while (rs.next()) {
                laboratorios.add(extrairLaboratorio(rs));
            }
            return laboratorios;
        }
    }

    public ArrayList<Laboratorio> getLaboratoriosPorCoordenador(int professorId) throws SQLException {
        String sql = "SELECT l.*, p.nome as coordenador FROM laboratorio l " +
                     "LEFT JOIN professor p ON l.coordenador_id = p.id " +
                     "WHERE l.coordenador_id = ? AND l.ativo = true ORDER BY l.nome";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professorId);
            ArrayList<Laboratorio> laboratorios = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    laboratorios.add(extrairLaboratorio(rs));
                }
            }
            return laboratorios;
        }
    }

    public Laboratorio getLaboratorioPorId(int id) throws SQLException {
        String sql = "SELECT l.*, p.nome as coordenador FROM laboratorio l " +
                     "LEFT JOIN professor p ON l.coordenador_id = p.id " +
                     "WHERE l.id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairLaboratorio(rs);
                }
            }
            return null;
        }
    }

    public boolean atualizar(Laboratorio lab) throws SQLException {
        String sql = "UPDATE laboratorio SET nome = ?, area_pesquisa = ?, status = ?, capacidade = ?, coordenador_id = ?, ativo = ? WHERE id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lab.getNome());
            stmt.setString(2, lab.getAreaPesquisa());
            stmt.setString(3, lab.getStatus());
            stmt.setInt(4, lab.getCapacidade());
            if (lab.getCoordenadorId() > 0) {
                stmt.setInt(5, lab.getCoordenadorId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setBoolean(6, lab.isAtivo());
            stmt.setInt(7, lab.getId());
            
            stmt.executeUpdate();
            return true;
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "UPDATE laboratorio SET ativo = false WHERE id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        }
    }

    public int contarBolsistasNoLaboratorio(int labId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bolsista WHERE laboratorio_id = ? AND ativo = true";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, labId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
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
