package dev.matheus.cadastroBolsistas.dao;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Cargo;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

/*
 * dao responsavel pelo acesso a tabela bolsista no banco de dados.
 * todas as queries usam preparedstatement para prevencao de sql injection.
 * operacoes de exclusao sao logicas (soft delete via campo ativo = false).
 */
@Repository
public class BolsistaDAO {

    public BolsistaDAO() {}

    /*
     * insere um novo bolsista no banco com todos os campos mapeados.
     * a senha ja deve chegar hasheada em sha256 antes de ser persistida.
     */
    public boolean inserir(Bolsista b) throws SQLException {
        String sql = "INSERT INTO bolsista (nome, data_nascimento, curso, email, matricula, cpf, telefone, senha, ativo, laboratorio_id, tipo_usuario, foto_url, cargo, bio) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, b.getNome());
            stmt.setDate(2, b.getDataNascimento() != null ? Date.valueOf(b.getDataNascimento()) : null);
            stmt.setString(3, b.getCurso());
            stmt.setString(4, b.getEmail());
            stmt.setString(5, b.getMatricula());
            stmt.setString(6, b.getCpf());
            stmt.setString(7, b.getTelefone());
            stmt.setString(8, b.getSenha());
            stmt.setBoolean(9, b.isAtivo());
            if (b.getLaboratorioId() > 0) {
                stmt.setInt(10, b.getLaboratorioId());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }
            stmt.setString(11, b.getTipoUsuario());
            stmt.setString(12, b.getFotoUrl());
            stmt.setString(13, b.getCargo() != null ? b.getCargo().name() : null);
            stmt.setString(14, b.getBio());
            
            stmt.executeUpdate();
            return true;
        }
    }

    /*
     * autentica o usuario comparando email e hash sha256 da senha.
     * retorna o objeto bolsista com o nome do laboratorio via join, ou null se nao encontrado.
     */
    public Bolsista autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                     "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                     "WHERE b.email = ? AND b.senha = ? AND b.ativo = true";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairBolsista(rs);
                }
            }
            return null;
        }
    }

    public Bolsista getBolsistaPorId(int id) throws SQLException {
        String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                     "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                     "WHERE b.id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairBolsista(rs);
                }
            }
            return null;
        }
    }

    /*
     * busca bolsistas por nome usando ilike (case insensitive) com wildcards.
     * retorna apenas registros ativos ordenados por nome.
     */
    public ArrayList<Bolsista> getBolsistasPorNome(String nome) throws SQLException {
        String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                     "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                     "WHERE b.nome ILIKE ? AND b.ativo = true ORDER BY b.nome";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ArrayList<Bolsista> bolsistas = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bolsistas.add(extrairBolsista(rs));
                }
            }
            return bolsistas;
        }
    }

    public ArrayList<Bolsista> getBolsistasPorCurso(String curso) throws SQLException {
        String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                     "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                     "WHERE b.curso ILIKE ? AND b.ativo = true ORDER BY b.nome";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + curso + "%");
            ArrayList<Bolsista> bolsistas = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bolsistas.add(extrairBolsista(rs));
                }
            }
            return bolsistas;
        }
    }

    public ArrayList<Bolsista> getBolsistasPorLaboratorio(int laboratorioId) throws SQLException {
        String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                     "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                     "WHERE b.laboratorio_id = ? AND b.ativo = true ORDER BY b.nome";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, laboratorioId);
            ArrayList<Bolsista> bolsistas = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bolsistas.add(extrairBolsista(rs));
                }
            }
            return bolsistas;
        }
    }

    public ArrayList<Bolsista> getBolsistasPorProjeto(int projetoId) throws SQLException {
        String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                     "INNER JOIN bolsista_projeto bp ON b.id = bp.bolsista_id " +
                     "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                     "WHERE bp.projeto_id = ? AND b.ativo = true ORDER BY b.nome";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projetoId);
            ArrayList<Bolsista> bolsistas = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bolsistas.add(extrairBolsista(rs));
                }
            }
            return bolsistas;
        }
    }

    public ArrayList<Bolsista> getBolsistas() throws SQLException {
        String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                     "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                     "WHERE b.ativo = true ORDER BY b.nome";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            ArrayList<Bolsista> bolsistas = new ArrayList<>();
            while (rs.next()) {
                bolsistas.add(extrairBolsista(rs));
            }
            return bolsistas;
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "UPDATE bolsista SET ativo = false WHERE id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        }
    }

    public int contarAdmins() throws SQLException {
        String sql = "SELECT COUNT(*) FROM bolsista WHERE tipo_usuario = 'ADMIN' AND ativo = true";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    public boolean atualizar(Bolsista b) throws SQLException {
        String sql = "UPDATE bolsista SET nome = ?, data_nascimento = ?, curso = ?, email = ?, matricula = ?, cpf = ?, telefone = ?, senha = ?, ativo = ?, laboratorio_id = ?, tipo_usuario = ?, foto_url = ?, cargo = ?, bio = ? WHERE id = ?";
        try (Connection conn = ConectaDBPostgres.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, b.getNome());
            stmt.setDate(2, b.getDataNascimento() != null ? Date.valueOf(b.getDataNascimento()) : null);
            stmt.setString(3, b.getCurso());
            stmt.setString(4, b.getEmail());
            stmt.setString(5, b.getMatricula());
            stmt.setString(6, b.getCpf());
            stmt.setString(7, b.getTelefone());
            stmt.setString(8, b.getSenha());
            stmt.setBoolean(9, b.isAtivo());
            if (b.getLaboratorioId() > 0) {
                stmt.setInt(10, b.getLaboratorioId());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }
            stmt.setString(11, b.getTipoUsuario());
            stmt.setString(12, b.getFotoUrl());
            stmt.setString(13, b.getCargo() != null ? b.getCargo().name() : null);
            stmt.setString(14, b.getBio());
            stmt.setInt(15, b.getId());
            
            stmt.executeUpdate();
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
        b.setCargo(Cargo.deString(rs.getString("cargo")));
        b.setBio(rs.getString("bio"));
        return b;
    }
}
