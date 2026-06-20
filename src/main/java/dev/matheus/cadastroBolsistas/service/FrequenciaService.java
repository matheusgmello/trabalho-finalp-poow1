package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.FrequenciaDAO;
import dev.matheus.cadastroBolsistas.model.Frequencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class FrequenciaService {

    @Autowired
    private FrequenciaDAO dao;

    public boolean registrar(Frequencia f) throws SQLException {
        f.setAtivo(true);
        return dao.inserir(f);
    }

    public Frequencia buscarPorId(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(Frequencia f) throws SQLException {
        return dao.atualizar(f);
    }

    public ArrayList<Frequencia> listarPorBolsista(int bolsistaId) throws SQLException {
        return dao.listarPorBolsista(bolsistaId);
    }

    public ArrayList<Frequencia> listarPorLaboratorio(int labId) throws SQLException {
        return dao.listarPorLaboratorio(labId);
    }

    public ArrayList<Frequencia> listarTodas() throws SQLException {
        return dao.listarTodas();
    }

    public ArrayList<Frequencia> listarTodasPaginado(int limit, int offset) throws SQLException {
        return dao.listarTodasPaginado(limit, offset);
    }

    public int contarTodas() throws SQLException {
        return dao.contarTodas();
    }

    public boolean excluir(int id) throws SQLException {
        return dao.excluir(id);
    }
}
