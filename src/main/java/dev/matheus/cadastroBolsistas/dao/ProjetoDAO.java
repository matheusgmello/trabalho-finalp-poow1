package dev.matheus.cadastroBolsistas.dao;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Projeto;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class ProjetoDAO {

    public ProjetoDAO() {}

    public boolean inserir(Projeto p) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO projeto (nome, descricao, laboratorio_id, ativo) " +
                         "VALUES ('" + p.getNome() + "', '" + p.getDescricao() + "', " + 
                         p.getLaboratorioId() + ", " + p.isAtivo() + ")";
            stmt.execute(sql);
            return true;
        }
    }

    public ArrayList<Projeto> getProjetos() throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Projeto> lista = new ArrayList<>();
            String sql = "SELECT p.*, l.nome as nome_laboratorio FROM projeto p " +
                         "LEFT JOIN laboratorio l ON p.laboratorio_id = l.id " +
                         "WHERE p.ativo = true ORDER BY p.nome";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(extrairProjeto(rs));
            }
            return lista;
        }
    }

    public ArrayList<Projeto> getProjetosPorLaboratorio(int labId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Projeto> lista = new ArrayList<>();
            String sql = "SELECT p.*, l.nome as nome_laboratorio FROM projeto p " +
                         "LEFT JOIN laboratorio l ON p.laboratorio_id = l.id " +
                         "WHERE p.laboratorio_id = " + labId + " AND p.ativo = true ORDER BY p.nome";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(extrairProjeto(rs));
            }
            return lista;
        }
    }

    public Projeto getProjetoPorId(int id) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT p.*, l.nome as nome_laboratorio FROM projeto p " +
                         "LEFT JOIN laboratorio l ON p.laboratorio_id = l.id " +
                         "WHERE p.id = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return extrairProjeto(rs);
            }
            return null;
        }
    }

    public boolean atualizar(Projeto p) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "UPDATE projeto SET " +
                         "nome = '" + p.getNome() + "', " +
                         "descricao = '" + p.getDescricao() + "', " +
                         "laboratorio_id = " + p.getLaboratorioId() + ", " +
                         "ativo = " + p.isAtivo() + " " +
                         "WHERE id = " + p.getId();
            stmt.execute(sql);
            return true;
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            // soft delete: set active to false
            stmt.execute("UPDATE projeto SET ativo = false WHERE id = " + id);
            return true;
        }
    }

    // Gerenciamento de Vinculos com Bolsistas (tabela bolsista_projeto)
    public boolean vincularBolsista(int bolsistaId, int projetoId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            // verifica se ja existe vinculo
            ResultSet rs = stmt.executeQuery("SELECT 1 FROM bolsista_projeto WHERE bolsista_id = " + bolsistaId + " AND projeto_id = " + projetoId);
            if (rs.next()) {
                return true; // ja vinculado
            }
            stmt.execute("INSERT INTO bolsista_projeto (bolsista_id, projeto_id) VALUES (" + bolsistaId + ", " + projetoId + ")");
            return true;
        }
    }

    public boolean desvincularBolsista(int bolsistaId, int projetoId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM bolsista_projeto WHERE bolsista_id = " + bolsistaId + " AND projeto_id = " + projetoId);
            return true;
        }
    }

    public boolean desvincularTodosBolsistas(int projetoId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM bolsista_projeto WHERE projeto_id = " + projetoId);
            return true;
        }
    }

    public boolean desvincularBolsistaDeTodosProjetos(int bolsistaId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM bolsista_projeto WHERE bolsista_id = " + bolsistaId);
            return true;
        }
    }

    public ArrayList<Projeto> getProjetosPorBolsista(int bolsistaId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Projeto> lista = new ArrayList<>();
            String sql = "SELECT p.*, l.nome as nome_laboratorio FROM projeto p " +
                         "INNER JOIN bolsista_projeto bp ON p.id = bp.projeto_id " +
                         "LEFT JOIN laboratorio l ON p.laboratorio_id = l.id " +
                         "WHERE bp.bolsista_id = " + bolsistaId + " AND p.ativo = true ORDER BY p.nome";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(extrairProjeto(rs));
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
}
