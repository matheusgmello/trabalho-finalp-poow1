package dao;

import model.Laboratorio;
import java.sql.*;
import java.util.ArrayList;

/*
 * responsavel pelas operacoes sql da tabela laboratorio.
 */
public class LaboratorioDAO {
    private Connection conexao;
    private Statement stmt;

    public LaboratorioDAO() throws SQLException {
        this.conexao = ConectaDBPostgres.getConexao();
        this.stmt = conexao.createStatement();
    }

    public boolean inserir(Laboratorio lab) throws SQLException {
        String sql = "INSERT INTO laboratorio (nome, area_pesquisa, titulo_projeto, status, capacidade, coordenador) " +
                     "VALUES ('" + lab.getNome() + "', '" + lab.getAreaPesquisa() + "', '" +
                     lab.getTituloProjeto() + "', '" + lab.getStatus() + "', " + lab.getCapacidade() + ", '" + lab.getCoordenador() + "')";
        stmt.execute(sql);
        return true;
    }

    public ArrayList<Laboratorio> getLaboratorios() throws SQLException {
        ArrayList<Laboratorio> laboratorios = new ArrayList<>();
        String sql = "SELECT * FROM laboratorio";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            laboratorios.add(extrairLaboratorio(rs));
        }
        return laboratorios;
    }

    public Laboratorio getLaboratorioPorId(int id) throws SQLException {
        String sql = "SELECT * FROM laboratorio WHERE id = " + id;
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return extrairLaboratorio(rs);
        }
        return null;
    }

    public boolean atualizar(Laboratorio lab) throws SQLException {
        String sql = "UPDATE laboratorio SET " +
                     "nome = '" + lab.getNome() + "', " +
                     "area_pesquisa = '" + lab.getAreaPesquisa() + "', " +
                     "titulo_projeto = '" + lab.getTituloProjeto() + "', " +
                     "status = '" + lab.getStatus() + "', " +
                     "capacidade = " + lab.getCapacidade() + ", " +
                     "coordenador = '" + lab.getCoordenador() + "' " +
                     "WHERE id = " + lab.getId();
        stmt.execute(sql);
        return true;
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM laboratorio WHERE id = " + id;
        stmt.execute(sql);
        return true;
    }

    // conta bolsistas vinculados ao laboratorio — usado para verificar se ha vaga
    public int contarBolsistasNoLaboratorio(int labId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bolsista WHERE laboratorio_id = " + labId;
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    private Laboratorio extrairLaboratorio(ResultSet rs) throws SQLException {
        Laboratorio lab = new Laboratorio();
        lab.setId(rs.getInt("id"));
        lab.setNome(rs.getString("nome"));
        lab.setAreaPesquisa(rs.getString("area_pesquisa"));
        lab.setTituloProjeto(rs.getString("titulo_projeto"));
        lab.setStatus(rs.getString("status"));
        lab.setCapacidade(rs.getInt("capacidade"));
        lab.setCoordenador(rs.getString("coordenador"));
        return lab;
    }
}
