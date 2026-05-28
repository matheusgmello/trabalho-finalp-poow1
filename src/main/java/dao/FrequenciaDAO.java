package dao;

import model.Frequencia;
import java.sql.*;
import java.util.ArrayList;

/*
 * responsavel pelas operacoes sql da tabela frequencia.
 * admin ve todos os registros; bolsista ve apenas os seus proprios.
 */
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

    // retorna um registro especifico pelo id — usado para carregar o formulario de edicao
    public Frequencia buscarPorId(int id) throws SQLException {
        String sql = "SELECT f.*, b.nome as nome_bolsista FROM frequencia f " +
                     "JOIN bolsista b ON f.bolsista_id = b.id WHERE f.id = " + id;
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return extrairFrequencia(rs);
        }
        return null;
    }

    // atualiza apenas data, horas e descricao — bolsista_id nao muda
    public boolean atualizar(Frequencia f) throws SQLException {
        String sql = "UPDATE frequencia SET data = '" + f.getData() + "', " +
                     "horas_trabalhadas = " + f.getHorasTrabalhadas() + ", " +
                     "descricao = '" + f.getDescricao() + "' " +
                     "WHERE id = " + f.getId();
        stmt.execute(sql);
        return true;
    }

    // lista registros de um bolsista especifico — usado para usuarios comuns
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

    // lista todos os registros — usado apenas por admins
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
