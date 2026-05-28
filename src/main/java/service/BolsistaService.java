package service;

import dao.BolsistaDAO;
import model.Bolsista;
import java.sql.SQLException;
import java.util.ArrayList;

public class BolsistaService {
    private BolsistaDAO dao;

    public BolsistaService() throws SQLException {
        this.dao = new BolsistaDAO();
    }

    public boolean inserir(Bolsista b) throws SQLException {
        return dao.inserir(b);
    }

    public ArrayList<Bolsista> listarTodos() throws SQLException {
        return dao.getBolsistas();
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

    public Bolsista autenticar(String email, String senha) throws SQLException {
        return dao.autenticar(email, senha);
    }

    public boolean excluir(int id) throws SQLException {
        return dao.excluir(id);
    }

    public boolean atualizar(Bolsista b) throws SQLException {
        return dao.atualizar(b);
    }

    public boolean existeAdmin() throws SQLException {
        return dao.existeAdmin();
    }
}
