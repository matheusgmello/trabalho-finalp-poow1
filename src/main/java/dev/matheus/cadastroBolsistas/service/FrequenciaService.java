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

    public ArrayList<Frequencia> listarTodas() throws SQLException {
        return dao.listarTodas();
    }

    public boolean excluir(int id) throws SQLException {
        return dao.excluir(id);
    }
}
