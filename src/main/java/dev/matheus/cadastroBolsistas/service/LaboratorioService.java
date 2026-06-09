package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.LaboratorioDAO;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class LaboratorioService {

    @Autowired
    private LaboratorioDAO dao;

    public boolean cadastrar(Laboratorio lab) throws SQLException {
        return dao.inserir(lab);
    }

    public ArrayList<Laboratorio> listarTodos() throws SQLException {
        return dao.getLaboratorios();
    }

    public Laboratorio buscarPorId(int id) throws SQLException {
        return dao.getLaboratorioPorId(id);
    }

    public boolean atualizar(Laboratorio lab) throws SQLException {
        return dao.atualizar(lab);
    }

    public boolean excluir(int id) throws SQLException {
        return dao.excluir(id);
    }

    // verifica se o laboratorio ainda tem vaga disponivel
    public boolean temVaga(int labId) throws SQLException {
        Laboratorio lab = dao.getLaboratorioPorId(labId);
        if (lab == null) return false;
        int ocupados = dao.contarBolsistasNoLaboratorio(labId);
        return ocupados < lab.getCapacidade();
    }
}
