package service;

import dao.FrequenciaDAO;
import model.Frequencia;
import java.sql.SQLException;
import java.util.ArrayList;

public class FrequenciaService {
    private FrequenciaDAO dao;

    public FrequenciaService() {
        try {
            this.dao = new FrequenciaDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean registrar(Frequencia f) {
        try {
            return dao.inserir(f);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Frequencia> listarPorBolsista(int bolsistaId) {
        try {
            return dao.listarPorBolsista(bolsistaId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<Frequencia> listarTodas() {
        try {
            return dao.listarTodas();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Frequencia buscarPorId(int id) {
        try {
            return dao.buscarPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean atualizar(Frequencia f) {
        try {
            return dao.atualizar(f);
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
