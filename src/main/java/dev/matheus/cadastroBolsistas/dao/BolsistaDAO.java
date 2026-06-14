package dev.matheus.cadastroBolsistas.dao;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class BolsistaDAO {

    public BolsistaDAO() {}

    public boolean inserir(Bolsista b) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO bolsista (nome, data_nascimento, curso, email, matricula, cpf, telefone, senha, ativo, laboratorio_id, tipo_usuario, foto_url, funcao, bio) " +
                         "VALUES ('" + b.getNome() + "', '" + b.getDataNascimento() + "', '" + b.getCurso() + "', '" +
                         b.getEmail() + "', '" + b.getMatricula() + "', '" + b.getCpf() + "', '" + b.getTelefone() + "', '" +
                         b.getSenha() + "', " + b.isAtivo() + ", " + (b.getLaboratorioId() > 0 ? b.getLaboratorioId() : "NULL") +
                         ", '" + b.getTipoUsuario() + "', " + (b.getFotoUrl() != null ? "'" + b.getFotoUrl() + "'" : "NULL") + 
                         ", " + (b.getFuncao() != null ? "'" + b.getFuncao() + "'" : "NULL") + 
                         ", " + (b.getBio() != null ? "'" + b.getBio().replace("'", "''") + "'" : "NULL") + ")";
            stmt.execute(sql);
            return true;
        }
    }

    public Bolsista autenticar(String email, String senha) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                         "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                         "WHERE b.email = '" + email + "' AND b.senha = '" + senha + "' AND b.ativo = true";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return extrairBolsista(rs);
            }
            return null;
        }
    }

    public Bolsista getBolsistaPorId(int id) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                         "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                         "WHERE b.id = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return extrairBolsista(rs);
            }
            return null;
        }
    }

    public ArrayList<Bolsista> getBolsistasPorNome(String nome) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Bolsista> bolsistas = new ArrayList<>();
            String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                         "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                         "WHERE b.nome ILIKE '%" + nome + "%' AND b.ativo = true";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                bolsistas.add(extrairBolsista(rs));
            }
            return bolsistas;
        }
    }

    public ArrayList<Bolsista> getBolsistasPorCurso(String curso) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Bolsista> bolsistas = new ArrayList<>();
            String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                         "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                         "WHERE b.curso ILIKE '%" + curso + "%' AND b.ativo = true";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                bolsistas.add(extrairBolsista(rs));
            }
            return bolsistas;
        }
    }

    public ArrayList<Bolsista> getBolsistasPorLaboratorio(int laboratorioId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Bolsista> bolsistas = new ArrayList<>();
            String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                         "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                         "WHERE b.laboratorio_id = " + laboratorioId + " AND b.ativo = true ORDER BY b.nome";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                bolsistas.add(extrairBolsista(rs));
            }
            return bolsistas;
        }
    }

    public ArrayList<Bolsista> getBolsistasPorProjeto(int projetoId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Bolsista> bolsistas = new ArrayList<>();
            String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                         "INNER JOIN bolsista_projeto bp ON b.id = bp.bolsista_id " +
                         "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                         "WHERE bp.projeto_id = " + projetoId + " AND b.ativo = true ORDER BY b.nome";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                bolsistas.add(extrairBolsista(rs));
            }
            return bolsistas;
        }
    }

    public ArrayList<Bolsista> getBolsistas() throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Bolsista> bolsistas = new ArrayList<>();
            String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                         "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                         "WHERE b.ativo = true ORDER BY b.nome";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                bolsistas.add(extrairBolsista(rs));
            }
            return bolsistas;
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            // soft delete: set active to false
            stmt.execute("UPDATE bolsista SET ativo = false WHERE id = " + id);
            return true;
        }
    }

    public int contarAdmins() throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM bolsista WHERE tipo_usuario = 'ADMIN' AND ativo = true");
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    public boolean atualizar(Bolsista b) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "UPDATE bolsista SET " +
                         "nome = '" + b.getNome() + "', " +
                         "data_nascimento = '" + b.getDataNascimento() + "', " +
                         "curso = '" + b.getCurso() + "', " +
                         "email = '" + b.getEmail() + "', " +
                         "matricula = '" + b.getMatricula() + "', " +
                         "cpf = '" + b.getCpf() + "', " +
                         "telefone = '" + b.getTelefone() + "', " +
                         "senha = '" + b.getSenha() + "', " +
                         "ativo = " + b.isAtivo() + ", " +
                         "laboratorio_id = " + (b.getLaboratorioId() > 0 ? b.getLaboratorioId() : "NULL") + ", " +
                         "tipo_usuario = '" + b.getTipoUsuario() + "', " +
                         "foto_url = " + (b.getFotoUrl() != null ? "'" + b.getFotoUrl() + "'" : "NULL") + ", " +
                         "funcao = " + (b.getFuncao() != null ? "'" + b.getFuncao() + "'" : "NULL") + ", " +
                         "bio = " + (b.getBio() != null ? "'" + b.getBio().replace("'", "''") + "'" : "NULL") + " " +
                         "WHERE id = " + b.getId();
            stmt.execute(sql);
            return true;
        }
    }

    private Bolsista extrairBolsista(ResultSet rs) throws SQLException {
        Bolsista b = new Bolsista();
        b.setId(rs.getInt("id"));
        b.setNome(rs.getString("nome"));
        Date dataNascimento = rs.getDate("data_nascimento");
        if (dataNascimento != null) {
            b.setDataNascimento(dataNascimento.toLocalDate());
        }
        b.setCurso(rs.getString("curso"));
        b.setEmail(rs.getString("email"));
        b.setMatricula(rs.getString("matricula"));
        b.setCpf(rs.getString("cpf"));
        b.setTelefone(rs.getString("telefone"));
        b.setSenha(rs.getString("senha"));
        b.setAtivo(rs.getBoolean("ativo"));
        b.setLaboratorioId(rs.getInt("laboratorio_id"));
        b.setNomeLaboratorio(rs.getString("nome_laboratorio"));
        b.setTipoUsuario(rs.getString("tipo_usuario"));
        b.setFotoUrl(rs.getString("foto_url"));
        b.setFuncao(rs.getString("funcao"));
        b.setBio(rs.getString("bio"));
        return b;
    }
}
