package dev.matheus.cadastroBolsistas.dao;

import dev.matheus.cadastroBolsistas.model.Professor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class ProfessorDAO {

    public ProfessorDAO() {}

    public boolean inserir(Professor p) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO professor (nome, email, senha, ativo, foto_url) " +
                         "VALUES ('" + p.getNome() + "', '" + p.getEmail() + "', '" + p.getSenha() + 
                         "', " + p.isAtivo() + ", " + (p.getFotoUrl() != null ? "'" + p.getFotoUrl() + "'" : "NULL") + ")";
            stmt.execute(sql);
            return true;
        }
    }

    public Professor autenticar(String email, String senha) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM professor WHERE email = '" + email + "' AND senha = '" + senha + "' AND ativo = true";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return extrairProfessor(rs);
            }
            return null;
        }
    }

    public ArrayList<Professor> getProfessores() throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Professor> lista = new ArrayList<>();
            ResultSet rs = stmt.executeQuery("SELECT * FROM professor WHERE ativo = true ORDER BY nome");
            while (rs.next()) {
                lista.add(extrairProfessor(rs));
            }
            return lista;
        }
    }

    public Professor getProfessorPorId(int id) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM professor WHERE id = " + id);
            if (rs.next()) {
                return extrairProfessor(rs);
            }
            return null;
        }
    }

    public boolean atualizar(Professor p) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "UPDATE professor SET " +
                         "nome = '" + p.getNome() + "', " +
                         "email = '" + p.getEmail() + "', " +
                         "senha = '" + p.getSenha() + "', " +
                         "ativo = " + p.isAtivo() + ", " +
                         "foto_url = " + (p.getFotoUrl() != null ? "'" + p.getFotoUrl() + "'" : "NULL") + " " +
                         "WHERE id = " + p.getId();
            stmt.execute(sql);
            return true;
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            // soft delete: set active to false
            stmt.execute("UPDATE professor SET ativo = false WHERE id = " + id);
            return true;
        }
    }

    private Professor extrairProfessor(ResultSet rs) throws SQLException {
        Professor p = new Professor();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setEmail(rs.getString("email"));
        p.setSenha(rs.getString("senha"));
        p.setAtivo(rs.getBoolean("ativo"));
        p.setFotoUrl(rs.getString("foto_url"));
        return p;
    }
}
