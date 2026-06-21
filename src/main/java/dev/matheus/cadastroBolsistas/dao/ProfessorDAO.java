package dev.matheus.cadastroBolsistas.dao;

import dev.matheus.cadastroBolsistas.model.Professor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

/*
 * dao responsavel pelo acesso a tabela professor no banco de dados.
 * professores coordenam laboratorios e possuem permissoes intermediarias no sistema.
 * autenticacao verifica email e hash sha256 da senha, igual ao fluxo de bolsistas.
 */
@Repository
public class ProfessorDAO {

    public ProfessorDAO() {}

    public boolean inserir(Professor p) throws SQLException {
        String sql = "INSERT INTO professor (nome, email, senha, ativo, foto_url, bio) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getEmail());
            stmt.setString(3, p.getSenha());
            stmt.setBoolean(4, p.isAtivo());
            stmt.setString(5, p.getFotoUrl());
            stmt.setString(6, p.getBio());
            stmt.executeUpdate();
            return true;
        }
    }

    public Professor autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM professor WHERE email = ? AND senha = ? AND ativo = true";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairProfessor(rs);
                }
            }
            return null;
        }
    }

    public ArrayList<Professor> getProfessores() throws SQLException {
        String sql = "SELECT * FROM professor WHERE ativo = true ORDER BY nome";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            ArrayList<Professor> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(extrairProfessor(rs));
            }
            return lista;
        }
    }

    public ArrayList<Professor> getProfessoresPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM professor WHERE nome ILIKE ? AND ativo = true ORDER BY nome";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ArrayList<Professor> lista = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(extrairProfessor(rs));
                }
            }
            return lista;
        }
    }

    public Professor getProfessorPorId(int id) throws SQLException {
        String sql = "SELECT * FROM professor WHERE id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairProfessor(rs);
                }
            }
            return null;
        }
    }

    public boolean atualizar(Professor p) throws SQLException {
        String sql = "UPDATE professor SET nome = ?, email = ?, senha = ?, ativo = ?, foto_url = ?, bio = ? WHERE id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getEmail());
            stmt.setString(3, p.getSenha());
            stmt.setBoolean(4, p.isAtivo());
            stmt.setString(5, p.getFotoUrl());
            stmt.setString(6, p.getBio());
            stmt.setInt(7, p.getId());
            stmt.executeUpdate();
            return true;
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "UPDATE professor SET ativo = false WHERE id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
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
        p.setBio(rs.getString("bio"));
        return p;
    }
}
