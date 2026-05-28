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
        String sql = "INSERT INTO bolsista (nome, data_nascimento, curso, email, matricula, cpf, telefone, senha, ativo, laboratorio_id, tipo_usuario, foto_url) " +
                     "VALUES ('" + b.getNome() + "', '" + b.getDataNascimento() + "', '" + b.getCurso() + "', '" +
                     b.getEmail() + "', '" + b.getMatricula() + "', '" + b.getCpf() + "', '" + b.getTelefone() + "', '" +
                     b.getSenha() + "', " + b.isAtivo() + ", " + (b.getLaboratorioId() > 0 ? b.getLaboratorioId() : "NULL") + 
                     ", '" + b.getTipoUsuario() + "', '" + b.getFotoUrl() + "')";
        stmt.execute(sql);
        return true;
    }

    public Bolsista autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                     "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                     "WHERE b.email = '" + email + "' AND b.senha = '" + senha + "'";
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return extrairBolsista(rs);
        }
        return null;
    }

    public ArrayList<Bolsista> getBolsistasPorNome(String nome) throws SQLException {
        ArrayList<Bolsista> bolsistas = new ArrayList<>();
        String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                     "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                     "WHERE b.nome ILIKE '%" + nome + "%'";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            bolsistas.add(extrairBolsista(rs));
        }
        return bolsistas;
    }

    public ArrayList<Bolsista> getBolsistasPorCurso(String curso) throws SQLException {
        ArrayList<Bolsista> bolsistas = new ArrayList<>();
        String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                     "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                     "WHERE b.curso ILIKE '%" + curso + "%'";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            bolsistas.add(extrairBolsista(rs));
        }
        return bolsistas;
    }

    public ArrayList<Bolsista> getBolsistasPorLaboratorio(int laboratorioId) throws SQLException {
        ArrayList<Bolsista> bolsistas = new ArrayList<>();
        String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                     "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id " +
                     "WHERE b.laboratorio_id = " + laboratorioId;
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
        return b;
    }

    public ArrayList<Bolsista> getBolsistas() throws SQLException {
        ArrayList<Bolsista> bolsistas = new ArrayList<>();
        String sql = "SELECT b.*, l.nome as nome_laboratorio FROM bolsista b " +
                     "LEFT JOIN laboratorio l ON b.laboratorio_id = l.id";
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

    public boolean existeAdmin() throws SQLException {
        String sql = "SELECT 1 FROM bolsista WHERE tipo_usuario = 'ADMIN' LIMIT 1";
        ResultSet rs = stmt.executeQuery(sql);
        return rs.next();
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
                     "ativo = " + b.isAtivo() + ", " +
                     "laboratorio_id = " + (b.getLaboratorioId() > 0 ? b.getLaboratorioId() : "NULL") + ", " +
                     "tipo_usuario = '" + b.getTipoUsuario() + "', " +
                     "foto_url = '" + b.getFotoUrl() + "' " +
                     "WHERE id = b.getId()";
        sql = sql.replace("WHERE id = b.getId()", "WHERE id = " + b.getId());
        stmt.execute(sql);
        return true;
    }
}
