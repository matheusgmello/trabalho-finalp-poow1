package dao;

import model.Frequencia;
import java.sql.*;
import java.util.ArrayList;

public class FrequenciaDAO {
    private Connection conexao;
    private Statement stmt;

    public FrequenciaDAO() throws SQLException {
        this.conexao = ConectaDBPostgres.getConexao();
        this.stmt = conexao.createStatement();
    }

    public boolean inserir(Frequencia f) throws SQLException {
        String sql = "INSERT INTO frequencia (bolsista_id, data, horas_trabalhadas, descricao) " +
                     "VALUES (" + f.getBolsistaId() + ", '" + f.getData() + "', " + 
                     f.getHorasTrabalhadas() + ", '" + f.getDescricao() + "')";
        stmt.execute(sql);
        return true;
    }

    public ArrayList<Frequencia> listarPorBolsista(int bolsistaId) throws SQLException {
        ArrayList<Frequencia> lista = new ArrayList<>();
        String sql = "SELECT f.*, b.nome as nome_bolsista FROM frequencia f " +
                     "JOIN bolsista b ON f.bolsista_id = b.id " +
                     "WHERE f.bolsista_id = " + bolsistaId + " ORDER BY f.data DESC";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            lista.add(extrairFrequencia(rs));
        }
        return lista;
    }

    public ArrayList<Frequencia> listarTodas() throws SQLException {
        ArrayList<Frequencia> lista = new ArrayList<>();
        String sql = "SELECT f.*, b.nome as nome_bolsista FROM frequencia f " +
                     "JOIN bolsista b ON f.bolsista_id = b.id ORDER BY f.data DESC";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            lista.add(extrairFrequencia(rs));
        }
        return lista;
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM frequencia WHERE id = " + id;
        stmt.execute(sql);
        return true;
    }

    private Frequencia extrairFrequencia(ResultSet rs) throws SQLException {
        Frequencia f = new Frequencia();
        f.setId(rs.getInt("id"));
        f.setBolsistaId(rs.getInt("bolsista_id"));
        f.setNomeBolsista(rs.getString("nome_bolsista"));
        f.setData(rs.getDate("data").toLocalDate());
        f.setHorasTrabalhadas(rs.getDouble("horas_trabalhadas"));
        f.setDescricao(rs.getString("descricao"));
        return f;
    }
}
