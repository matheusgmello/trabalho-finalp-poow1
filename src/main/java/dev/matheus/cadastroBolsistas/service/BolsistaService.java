package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.BolsistaDAO;
import dev.matheus.cadastroBolsistas.model.Bolsista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class BolsistaService {

    @Autowired
    private BolsistaDAO dao;

    public boolean inserir(Bolsista b) throws SQLException {
        b.setAtivo(true);
        return dao.inserir(b);
    }

    public ArrayList<Bolsista> listarTodos() throws SQLException {
        return dao.getBolsistas();
    }

    public Bolsista buscarPorId(int id) throws SQLException {
        return dao.getBolsistaPorId(id);
    }

    public ArrayList<Bolsista> buscarPorNome(String nome) throws SQLException {
        return dao.getBolsistasPorNome(nome);
    }

    public ArrayList<Bolsista> buscarPorCurso(String curso) throws SQLException {
        return dao.getBolsistasPorCurso(curso);
    }

    public ArrayList<Bolsista> buscarPorLaboratorio(int laboratorioId) throws SQLException {
        return dao.getBolsistasPorLaboratorio(laboratorioId);
    }

    public ArrayList<Bolsista> buscarPorProjeto(int projetoId) throws SQLException {
        return dao.getBolsistasPorProjeto(projetoId);
    }

    public boolean atualizar(Bolsista b) throws SQLException {
        return dao.atualizar(b);
    }

    public boolean excluir(int id) throws SQLException {
        return dao.excluir(id);
    }

    public int contarAdmins() throws SQLException {
        return dao.contarAdmins();
    }
}
