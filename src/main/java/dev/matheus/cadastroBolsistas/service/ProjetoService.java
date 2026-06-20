package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.ProjetoDAO;
import dev.matheus.cadastroBolsistas.model.Projeto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoDAO dao;

    public boolean cadastrar(Projeto p) throws SQLException {
        p.setAtivo(true);
        return dao.inserir(p);
    }

    public ArrayList<Projeto> listarTodos() throws SQLException {
        return dao.getProjetos();
    }

    public ArrayList<Projeto> buscarProjetos(String buscaNome, Integer labId) throws SQLException {
        return dao.buscarProjetos(buscaNome, labId);
    }

    public ArrayList<Projeto> listarPorLaboratorio(int labId) throws SQLException {
        return dao.getProjetosPorLaboratorio(labId);
    }

    public Projeto buscarPorId(int id) throws SQLException {
        return dao.getProjetoPorId(id);
    }

    public boolean atualizar(Projeto p) throws SQLException {
        return dao.atualizar(p);
    }

    public boolean excluir(int id) throws SQLException {
        return dao.excluir(id);
    }

    public boolean vincularBolsista(int bolsistaId, int projetoId) throws SQLException {
        return dao.vincularBolsista(bolsistaId, projetoId);
    }

    public boolean desvincularBolsista(int bolsistaId, int projetoId) throws SQLException {
        return dao.desvincularBolsista(bolsistaId, projetoId);
    }

    public boolean desvincularBolsistaDeTodosProjetos(int bolsistaId) throws SQLException {
        return dao.desvincularBolsistaDeTodosProjetos(bolsistaId);
    }

    public ArrayList<Projeto> listarPorBolsista(int bolsistaId) throws SQLException {
        return dao.getProjetosPorBolsista(bolsistaId);
    }

    public Map<Integer, ArrayList<Projeto>> getProjetosDosBolsistasDoLaboratorio(int labId) throws SQLException {
        return dao.getProjetosDosBolsistasDoLaboratorio(labId);
    }
}
