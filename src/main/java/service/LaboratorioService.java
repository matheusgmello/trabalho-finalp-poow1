package service;

import dao.LaboratorioDAO;
import model.Laboratorio;
import java.sql.SQLException;
import java.util.ArrayList;

public class LaboratorioService {
    private LaboratorioDAO dao;

    public LaboratorioService() {
        try {
            this.dao = new LaboratorioDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean cadastrar(Laboratorio lab) {
        try {
            return dao.inserir(lab);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Laboratorio> listarTodos() {
        try {
            return dao.getLaboratorios();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Laboratorio buscarPorId(int id) {
        try {
            return dao.getLaboratorioPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean atualizar(Laboratorio lab) {
        try {
            return dao.atualizar(lab);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(int id) {
        try {
            return dao.excluir(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
