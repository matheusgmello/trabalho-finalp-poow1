package service;

import dao.LaboratorioDAO;
import model.Laboratorio;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * camada de regras de negocio para laboratorio.
 * isola os servlets do dao e trata excecoes de sql internamente.
 */
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

    // verifica se o laboratorio ainda tem vagas disponiveis para novos bolsistas
    public boolean temVaga(int labId) {
        try {
            Laboratorio lab = dao.getLaboratorioPorId(labId);
            if (lab == null) return false;
            int totalBolsistas = dao.contarBolsistasNoLaboratorio(labId);
            return totalBolsistas < lab.getCapacidade();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
