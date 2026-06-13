package dev.matheus.cadastroBolsistas.dao;

import dev.matheus.cadastroBolsistas.model.Frequencia;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class FrequenciaDAO {

    public FrequenciaDAO() {}

    public boolean inserir(Frequencia f) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO frequencia (bolsista_id, data, horas_trabalhadas, descricao, ativo) " +
                         "VALUES (" + f.getBolsistaId() + ", '" + f.getData() + "', " +
                         f.getHorasTrabalhadas() + ", '" + f.getDescricao() + "', " + f.isAtivo() + ")";
            stmt.execute(sql);
            return true;
        }
    }

    public Frequencia buscarPorId(int id) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT f.*, b.nome as nome_bolsista FROM frequencia f " +
                         "JOIN bolsista b ON f.bolsista_id = b.id WHERE f.id = " + id + " AND f.ativo = true";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return extrairFrequencia(rs);
            }
            return null;
        }
    }

    public boolean atualizar(Frequencia f) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "UPDATE frequencia SET data = '" + f.getData() + "', " +
                         "horas_trabalhadas = " + f.getHorasTrabalhadas() + ", " +
                         "descricao = '" + f.getDescricao() + "' " +
                         "WHERE id = " + f.getId();
            stmt.execute(sql);
            return true;
        }
    }

    public ArrayList<Frequencia> listarPorBolsista(int bolsistaId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Frequencia> lista = new ArrayList<>();
            String sql = "SELECT f.*, b.nome as nome_bolsista FROM frequencia f " +
                         "JOIN bolsista b ON f.bolsista_id = b.id " +
                         "WHERE f.bolsista_id = " + bolsistaId + " AND f.ativo = true ORDER BY f.data DESC";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(extrairFrequencia(rs));
            }
            return lista;
        }
    }

    public ArrayList<Frequencia> listarPorLaboratorio(int labId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Frequencia> lista = new ArrayList<>();
            String sql = "SELECT f.*, b.nome as nome_bolsista FROM frequencia f " +
                         "JOIN bolsista b ON f.bolsista_id = b.id " +
                         "WHERE b.laboratorio_id = " + labId + " AND f.ativo = true ORDER BY f.data DESC";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(extrairFrequencia(rs));
            }
            return lista;
        }
    }

    public ArrayList<Frequencia> listarTodas() throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Frequencia> lista = new ArrayList<>();
            String sql = "SELECT f.*, b.nome as nome_bolsista FROM frequencia f " +
                         "JOIN bolsista b ON f.bolsista_id = b.id " +
                         "WHERE f.ativo = true ORDER BY f.data DESC";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(extrairFrequencia(rs));
            }
            return lista;
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            // soft delete: set active to false
            stmt.execute("UPDATE frequencia SET ativo = false WHERE id = " + id);
            return true;
        }
    }

    private Frequencia extrairFrequencia(ResultSet rs) throws SQLException {
        Frequencia f = new Frequencia();
        f.setId(rs.getInt("id"));
        f.setBolsistaId(rs.getInt("bolsista_id"));
        f.setNomeBolsista(rs.getString("nome_bolsista"));
        f.setData(rs.getDate("data").toLocalDate());
        f.setHorasTrabalhadas(rs.getDouble("horas_trabalhadas"));
        f.setDescricao(rs.getString("descricao"));
        f.setAtivo(rs.getBoolean("ativo"));
        return f;
    }
}
