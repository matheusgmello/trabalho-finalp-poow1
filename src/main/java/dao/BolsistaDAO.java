package dao;

import model.Bolsista;
import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;

public class BolsistaDAO {
    private Connection conexao;
    private Statement stmt;

    public BolsistaDAO() throws SQLException {
        this.conexao = ConectaDBPostgres.getConexao();
        this.stmt = conexao.createStatement();
    }

    public boolean inserir(Bolsista b) throws SQLException {
        String sql = "INSERT INTO bolsista (nome, data_nascimento, curso, email, matricula, cpf, telefone, senha, ativo) " +
                     "VALUES ('" + b.getNome() + "', '" + b.getDataNascimento() + "', '" + b.getCurso() + "', '" +
                     b.getEmail() + "', '" + b.getMatricula() + "', '" + b.getCpf() + "', '" + b.getTelefone() + "', '" +
                     b.getSenha() + "', " + b.isAtivo() + ")";
        stmt.execute(sql);
        return true;
    }

    public Bolsista autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM bolsista WHERE email = '" + email + "' AND senha = '" + senha + "'";
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return extrairBolsista(rs);
        }
        return null;
    }

    public ArrayList<Bolsista> getBolsistasPorNome(String nome) throws SQLException {
        ArrayList<Bolsista> bolsistas = new ArrayList<>();
        String sql = "SELECT * FROM bolsista WHERE nome ILIKE '%" + nome + "%'";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            bolsistas.add(extrairBolsista(rs));
        }
        return bolsistas;
    }

    public ArrayList<Bolsista> getBolsistasPorCurso(String curso) throws SQLException {
        ArrayList<Bolsista> bolsistas = new ArrayList<>();
        String sql = "SELECT * FROM bolsista WHERE curso ILIKE '%" + curso + "%'";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            bolsistas.add(extrairBolsista(rs));
        }
        return bolsistas;
    }

    private Bolsista extrairBolsista(ResultSet rs) throws SQLException {
        Bolsista b = new Bolsista();
        b.setId(rs.getInt("id"));
        b.setNome(rs.getString("nome"));
        b.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        b.setCurso(rs.getString("curso"));
        b.setEmail(rs.getString("email"));
        b.setMatricula(rs.getString("matricula"));
        b.setCpf(rs.getString("cpf"));
        b.setTelefone(rs.getString("telefone"));
        b.setSenha(rs.getString("senha"));
        b.setAtivo(rs.getBoolean("ativo"));
        return b;
    }

    public ArrayList<Bolsista> getBolsistas() throws SQLException {
        ArrayList<Bolsista> bolsistas = new ArrayList<>();
        String sql = "SELECT * FROM bolsista";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            bolsistas.add(extrairBolsista(rs));
        }
        return bolsistas;
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM bolsista WHERE id = " + id;
        stmt.execute(sql);
        return true;
    }

    public boolean atualizar(Bolsista b) throws SQLException {
        String sql = "UPDATE bolsista SET " +
                     "nome = '" + b.getNome() + "', " +
                     "data_nascimento = '" + b.getDataNascimento() + "', " +
                     "curso = '" + b.getCurso() + "', " +
                     "email = '" + b.getEmail() + "', " +
                     "matricula = '" + b.getMatricula() + "', " +
                     "cpf = '" + b.getCpf() + "', " +
                     "telefone = '" + b.getTelefone() + "', " +
                     "senha = '" + b.getSenha() + "', " +
                     "ativo = " + b.isAtivo() + " " +
                     "WHERE id = " + b.getId();
        stmt.execute(sql);
        return true;
    }
}
