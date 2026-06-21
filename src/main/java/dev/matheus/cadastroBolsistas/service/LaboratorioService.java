package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.LaboratorioDAO;
import dev.matheus.cadastroBolsistas.dao.ProjetoDAO;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

/*
 * service responsavel pelas regras de negocio de laboratorios.
 * centraliza a verificacao de permissao podeGerenciar para admin e professor coordenador.
 * o metodo temVaga consulta capacidade e ocupacao atual antes de permitir novo vinculo.
 */
@Service
public class LaboratorioService {

    @Autowired
    private LaboratorioDAO dao;

    @Autowired
    private ProjetoDAO projetoDao;

    public boolean podeGerenciar(Usuario usuarioLogado, int labId) throws SQLException {
        if (usuarioLogado == null) return false;
        if (usuarioLogado.isAdmin()) return true;
        if (usuarioLogado.isProfessor()) {
            Laboratorio lab = dao.getLaboratorioPorId(labId);
            return lab != null && lab.getCoordenadorId() == usuarioLogado.getId();
        }
        return false;
    }

    public boolean cadastrar(Laboratorio lab) throws SQLException {
        lab.setAtivo(true);
        return dao.inserir(lab);
    }

    public ArrayList<Laboratorio> listarTodos() throws SQLException {
        return dao.getLaboratorios();
    }

    public ArrayList<Laboratorio> listarPorCoordenador(int professorId) throws SQLException {
        return dao.getLaboratoriosPorCoordenador(professorId);
    }

    public Laboratorio buscarPorId(int id) throws SQLException {
        Laboratorio lab = dao.getLaboratorioPorId(id);
        if (lab != null) {
            lab.setProjetos(projetoDao.getProjetosPorLaboratorio(id));
        }
        return lab;
    }

    public boolean atualizar(Laboratorio lab) throws SQLException {
        return dao.atualizar(lab);
    }

    public boolean excluir(int id) throws SQLException {
        return dao.excluir(id);
    }

    public boolean temVaga(int labId) throws SQLException {
        Laboratorio lab = dao.getLaboratorioPorId(labId);
        if (lab == null) return false;
        int ocupados = dao.contarBolsistasNoLaboratorio(labId);
        return ocupados < lab.getCapacidade();
    }

    public int contarBolsistasNoLaboratorio(int labId) throws SQLException {
        return dao.contarBolsistasNoLaboratorio(labId);
    }
}
