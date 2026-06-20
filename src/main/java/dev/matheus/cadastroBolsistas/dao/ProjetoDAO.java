package dev.matheus.cadastroBolsistas.dao;

import dev.matheus.cadastroBolsistas.model.Projeto;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ProjetoDAO {

    public ProjetoDAO() {}

    public boolean inserir(Projeto p) throws SQLException {
        String sql = "INSERT INTO projeto (nome, descricao, laboratorio_id, ativo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescricao());
            stmt.setInt(3, p.getLaboratorioId());
            stmt.setBoolean(4, p.isAtivo());
            stmt.executeUpdate();
            return true;
        }
    }

    public ArrayList<Projeto> getProjetos() throws SQLException {
        return buscarProjetos(null, null);
    }

    public ArrayList<Projeto> getProjetosPorLaboratorio(int labId) throws SQLException {
        return buscarProjetos(null, labId);
    }

    public ArrayList<Projeto> buscarProjetos(String buscaNome, Integer labId) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT p.*, l.nome as nome_laboratorio FROM projeto p ");
        sql.append("LEFT JOIN laboratorio l ON p.laboratorio_id = l.id ");
        sql.append("WHERE p.ativo = true ");
        
        if (buscaNome != null && !buscaNome.trim().isEmpty()) {
            sql.append("AND (p.nome ILIKE ? OR p.descricao ILIKE ?) ");
        }
        if (labId != null && labId > 0) {
            sql.append("AND p.laboratorio_id = ? ");
        }
        sql.append("ORDER BY p.nome");

        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (buscaNome != null && !buscaNome.trim().isEmpty()) {
                String seek = "%" + buscaNome.trim() + "%";
                stmt.setString(paramIndex++, seek);
                stmt.setString(paramIndex++, seek);
            }
            if (labId != null && labId > 0) {
                stmt.setInt(paramIndex++, labId);
            }

            ArrayList<Projeto> lista = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(extrairProjeto(rs));
                }
            }
            return lista;
        }
    }

    public Projeto getProjetoPorId(int id) throws SQLException {
        String sql = "SELECT p.*, l.nome as nome_laboratorio FROM projeto p " +
                     "LEFT JOIN laboratorio l ON p.laboratorio_id = l.id " +
                     "WHERE p.id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairProjeto(rs);
                }
            }
            return null;
        }
    }

    public boolean atualizar(Projeto p) throws SQLException {
        String sql = "UPDATE projeto SET nome = ?, descricao = ?, laboratorio_id = ?, ativo = ? WHERE id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescricao());
            stmt.setInt(3, p.getLaboratorioId());
            stmt.setBoolean(4, p.isAtivo());
            stmt.setInt(5, p.getId());
            stmt.executeUpdate();
            return true;
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "UPDATE projeto SET ativo = false WHERE id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        }
    }

    // Gerenciamento de Vinculos com Bolsistas (tabela bolsista_projeto)
    public boolean vincularBolsista(int bolsistaId, int projetoId) throws SQLException {
        String sqlVerifica = "SELECT 1 FROM bolsista_projeto WHERE bolsista_id = ? AND projeto_id = ?";
        String sqlInserir = "INSERT INTO bolsista_projeto (bolsista_id, projeto_id) VALUES (?, ?)";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmtVerifica = conn.prepareStatement(sqlVerifica)) {
            stmtVerifica.setInt(1, bolsistaId);
            stmtVerifica.setInt(2, projetoId);
            try (ResultSet rs = stmtVerifica.executeQuery()) {
                if (rs.next()) {
                    return true; // ja vinculado
                }
            }
            try (PreparedStatement stmtInserir = conn.prepareStatement(sqlInserir)) {
                stmtInserir.setInt(1, bolsistaId);
                stmtInserir.setInt(2, projetoId);
                stmtInserir.executeUpdate();
            }
            return true;
        }
    }

    public boolean desvincularBolsista(int bolsistaId, int projetoId) throws SQLException {
        String sql = "DELETE FROM bolsista_projeto WHERE bolsista_id = ? AND projeto_id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bolsistaId);
            stmt.setInt(2, projetoId);
            stmt.executeUpdate();
            return true;
        }
    }

    public boolean desvincularTodosBolsistas(int projetoId) throws SQLException {
        String sql = "DELETE FROM bolsista_projeto WHERE projeto_id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projetoId);
            stmt.executeUpdate();
            return true;
        }
    }

    public boolean desvincularBolsistaDeTodosProjetos(int bolsistaId) throws SQLException {
        String sql = "DELETE FROM bolsista_projeto WHERE bolsista_id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bolsistaId);
            stmt.executeUpdate();
            return true;
        }
    }

    public ArrayList<Projeto> getProjetosPorBolsista(int bolsistaId) throws SQLException {
        String sql = "SELECT p.*, l.nome as nome_laboratorio FROM projeto p " +
                     "INNER JOIN bolsista_projeto bp ON p.id = bp.projeto_id " +
                     "LEFT JOIN laboratorio l ON p.laboratorio_id = l.id " +
                     "WHERE bp.bolsista_id = ? AND p.ativo = true ORDER BY p.nome";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bolsistaId);
            ArrayList<Projeto> lista = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(extrairProjeto(rs));
                }
            }
            return lista;
        }
    }

    private Projeto extrairProjeto(ResultSet rs) throws SQLException {
        Projeto p = new Projeto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setDescricao(rs.getString("descricao"));
        p.setLaboratorioId(rs.getInt("laboratorio_id"));
        p.setNomeLaboratorio(rs.getString("nome_laboratorio"));
        p.setAtivo(rs.getBoolean("ativo"));
        return p;
    }

    public Map<Integer, ArrayList<Projeto>> getProjetosDosBolsistasDoLaboratorio(int labId) throws SQLException {
        String sql = "SELECT bp.bolsista_id, p.*, l.nome as nome_laboratorio FROM projeto p " +
                     "INNER JOIN bolsista_projeto bp ON p.id = bp.projeto_id " +
                     "INNER JOIN bolsista b ON bp.bolsista_id = b.id " +
                     "LEFT JOIN laboratorio l ON p.laboratorio_id = l.id " +
                     "WHERE b.laboratorio_id = ? AND p.ativo = true ORDER BY p.nome";
        Map<Integer, ArrayList<Projeto>> mapa = new HashMap<>();
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, labId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int bolsistaId = rs.getInt("bolsista_id");
                    Projeto p = extrairProjeto(rs);
                    mapa.computeIfAbsent(bolsistaId, k -> new ArrayList<>()).add(p);
                }
            }
        }
        return mapa;
    }
}
