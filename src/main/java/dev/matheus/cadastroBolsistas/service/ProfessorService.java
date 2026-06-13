package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.ProfessorDAO;
import dev.matheus.cadastroBolsistas.model.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorDAO dao;

    public boolean inserir(Professor p) throws SQLException {
        return dao.inserir(p);
    }

    public ArrayList<Professor> listarTodos() throws SQLException {
        return dao.getProfessores();
    }

    public ArrayList<Professor> buscarPorNome(String nome) throws SQLException {
        return dao.getProfessoresPorNome(nome);
    }

    public Professor buscarPorId(int id) throws SQLException {
        return dao.getProfessorPorId(id);
    }

    public boolean atualizar(Professor p) throws SQLException {
        return dao.atualizar(p);
    }

    public boolean excluir(int id) throws SQLException {
        return dao.excluir(id);
    }
}
