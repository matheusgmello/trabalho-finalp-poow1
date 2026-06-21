package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.FrequenciaDAO;
import dev.matheus.cadastroBolsistas.model.Frequencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

/*
 * service responsavel pelas regras de negocio de registros de frequencia.
 * expoe metodos de listagem filtrada por bolsista, laboratorio e paginacao.
 * todo novo registro e marcado como ativo = true antes de ser persistido.
 */
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

    public ArrayList<Frequencia> buscarFrequencias(Integer bolsistaId, Integer limit, Integer offset) throws SQLException {
        return dao.buscarFrequencias(bolsistaId, limit, offset);
    }

    public int contarFrequencias(Integer bolsistaId) throws SQLException {
        return dao.contarFrequencias(bolsistaId);
    }

    public boolean excluir(int id) throws SQLException {
        return dao.excluir(id);
    }
}
