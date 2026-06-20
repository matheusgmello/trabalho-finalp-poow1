package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.RelatorioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    @Autowired
    private RelatorioDAO dao;

    public List<Map<String, Object>> getHorasBolsistasMesCorrente() throws SQLException {
        return dao.getHorasBolsistasMesCorrente();
    }

    public List<Map<String, Object>> getProjetosAtivosPorLaboratorio() throws SQLException {
        return dao.getProjetosAtivosPorLaboratorio();
    }

    public List<Map<String, Object>> getBolsistasPorCargo() throws SQLException {
        return dao.getBolsistasPorCargo();
    }

    public List<Map<String, Object>> getLaboratoriosOcupacao() throws SQLException {
        return dao.getLaboratoriosOcupacao();
    }
}
