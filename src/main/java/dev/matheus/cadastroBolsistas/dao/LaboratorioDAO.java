package dev.matheus.cadastroBolsistas.dao;

import dev.matheus.cadastroBolsistas.model.Laboratorio;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

/*
 * responsavel pelas operacoes sql da tabela laboratorio.
 */
@Repository
public class LaboratorioDAO {

    public LaboratorioDAO() {}

    public boolean inserir(Laboratorio lab) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO laboratorio (nome, area_pesquisa, titulo_projeto, status, capacidade, coordenador) " +
                         "VALUES ('" + lab.getNome() + "', '" + lab.getAreaPesquisa() + "', '" +
                         lab.getTituloProjeto() + "', '" + lab.getStatus() + "', " + lab.getCapacidade() + ", '" + lab.getCoordenador() + "')";
            stmt.execute(sql);
            return true;
        }
    }

    public ArrayList<Laboratorio> getLaboratorios() throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ArrayList<Laboratorio> laboratorios = new ArrayList<>();
            ResultSet rs = stmt.executeQuery("SELECT * FROM laboratorio");
            while (rs.next()) {
                laboratorios.add(extrairLaboratorio(rs));
            }
            return laboratorios;
        }
    }

    public Laboratorio getLaboratorioPorId(int id) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM laboratorio WHERE id = " + id);
            if (rs.next()) {
                return extrairLaboratorio(rs);
            }
            return null;
        }
    }

    public boolean atualizar(Laboratorio lab) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
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
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM laboratorio WHERE id = " + id);
            return true;
        }
    }

    // conta bolsistas vinculados ao laboratorio — usado para verificar se ha vaga
    public int contarBolsistasNoLaboratorio(int labId) throws SQLException {
        try (Connection conn = ConectaDBPostgres.getConexao();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM bolsista WHERE laboratorio_id = " + labId);
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
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
