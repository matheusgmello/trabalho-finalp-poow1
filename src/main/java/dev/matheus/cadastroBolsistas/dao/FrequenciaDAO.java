package dev.matheus.cadastroBolsistas.dao;

import dev.matheus.cadastroBolsistas.model.Frequencia;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

/*
 * dao responsavel pelo acesso a tabela frequencia no banco de dados.
 * suporta paginacao via limit e offset para listagens grandes.
 * registros sao desativados por soft delete (ativo = false), nao removidos fisicamente.
 */
@Repository
public class FrequenciaDAO {

    public FrequenciaDAO() {}

    public boolean inserir(Frequencia f) throws SQLException {
        String sql = "INSERT INTO frequencia (bolsista_id, data, horas_trabalhadas, descricao, ativo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, f.getBolsistaId());
            stmt.setDate(2, f.getData() != null ? Date.valueOf(f.getData()) : null);
            stmt.setDouble(3, f.getHorasTrabalhadas());
            stmt.setString(4, f.getDescricao());
            stmt.setBoolean(5, f.isAtivo());
            stmt.executeUpdate();
            return true;
        }
    }

    public Frequencia buscarPorId(int id) throws SQLException {
        String sql = "SELECT f.*, b.nome as nome_bolsista FROM frequencia f " +
                     "JOIN bolsista b ON f.bolsista_id = b.id WHERE f.id = ? AND f.ativo = true";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairFrequencia(rs);
                }
            }
            return null;
        }
    }

    public boolean atualizar(Frequencia f) throws SQLException {
        String sql = "UPDATE frequencia SET data = ?, horas_trabalhadas = ?, descricao = ? WHERE id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, f.getData() != null ? Date.valueOf(f.getData()) : null);
            stmt.setDouble(2, f.getHorasTrabalhadas());
            stmt.setString(3, f.getDescricao());
            stmt.setInt(4, f.getId());
            stmt.executeUpdate();
            return true;
        }
    }

    public ArrayList<Frequencia> listarPorBolsista(int bolsistaId) throws SQLException {
        String sql = "SELECT f.*, b.nome as nome_bolsista FROM frequencia f " +
                     "JOIN bolsista b ON f.bolsista_id = b.id " +
                     "WHERE f.bolsista_id = ? AND f.ativo = true ORDER BY f.data DESC";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bolsistaId);
            ArrayList<Frequencia> lista = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(extrairFrequencia(rs));
                }
            }
            return lista;
        }
    }

    public ArrayList<Frequencia> listarPorLaboratorio(int labId) throws SQLException {
        String sql = "SELECT f.*, b.nome as nome_bolsista FROM frequencia f " +
                     "JOIN bolsista b ON f.bolsista_id = b.id " +
                     "WHERE b.laboratorio_id = ? AND f.ativo = true ORDER BY f.data DESC";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, labId);
            ArrayList<Frequencia> lista = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(extrairFrequencia(rs));
                }
            }
            return lista;
        }
    }

    public ArrayList<Frequencia> listarTodas() throws SQLException {
        String sql = "SELECT f.*, b.nome as nome_bolsista FROM frequencia f " +
                     "JOIN bolsista b ON f.bolsista_id = b.id " +
                     "WHERE f.ativo = true ORDER BY f.data DESC";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            ArrayList<Frequencia> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(extrairFrequencia(rs));
            }
            return lista;
        }
    }

    /*
     * busca frequencias com filtro opcional por bolsista e paginacao via limit/offset.
     * a query e construida dinamicamente conforme os parametros fornecidos.
     */
    public ArrayList<Frequencia> buscarFrequencias(Integer bolsistaId, Integer limit, Integer offset) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT f.*, b.nome as nome_bolsista FROM frequencia f ");
        sql.append("JOIN bolsista b ON f.bolsista_id = b.id ");
        sql.append("WHERE f.ativo = true ");
        if (bolsistaId != null && bolsistaId > 0) {
            sql.append("AND f.bolsista_id = ? ");
        }
        sql.append("ORDER BY f.data DESC ");
        if (limit != null && offset != null) {
            sql.append("LIMIT ? OFFSET ? ");
        }
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (bolsistaId != null && bolsistaId > 0) {
                stmt.setInt(idx++, bolsistaId);
            }
            if (limit != null && offset != null) {
                stmt.setInt(idx++, limit);
                stmt.setInt(idx++, offset);
            }
            ArrayList<Frequencia> lista = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(extrairFrequencia(rs));
                }
            }
            return lista;
        }
    }

    public int contarFrequencias(Integer bolsistaId) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM frequencia WHERE ativo = true ");
        if (bolsistaId != null && bolsistaId > 0) {
            sql.append("AND bolsista_id = ? ");
        }
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            if (bolsistaId != null && bolsistaId > 0) {
                stmt.setInt(1, bolsistaId);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "UPDATE frequencia SET ativo = false WHERE id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
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
