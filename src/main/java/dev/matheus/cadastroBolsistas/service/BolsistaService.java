package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.BolsistaDAO;
import dev.matheus.cadastroBolsistas.dao.LaboratorioDAO;
import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

/*
 * service responsavel pelas regras de negocio relacionadas a bolsistas.
 * centraliza a logica de permissao podeGerenciar para evitar duplicacao nos controllers.
 * delega o acesso ao banco para bolsistaDAO e laboratorioDAO.
 */
@Service
public class BolsistaService {

    @Autowired
    private BolsistaDAO dao;

    @Autowired
    private LaboratorioDAO laboratorioDao;

    public boolean podeGerenciar(Usuario usuarioLogado, Bolsista b) throws SQLException {
        if (usuarioLogado == null || b == null) return false;
        if (usuarioLogado.isAdmin()) return true;
        if (usuarioLogado.isProfessor()) {
            if (b.getLaboratorioId() > 0) {
                Laboratorio lab = laboratorioDao.getLaboratorioPorId(b.getLaboratorioId());
                return lab != null && lab.getCoordenadorId() == usuarioLogado.getId();
            }
        }
        return false;
    }

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
