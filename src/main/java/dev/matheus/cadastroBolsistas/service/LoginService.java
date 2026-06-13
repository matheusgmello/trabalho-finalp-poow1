package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.BolsistaDAO;
import dev.matheus.cadastroBolsistas.dao.ProfessorDAO;
import dev.matheus.cadastroBolsistas.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private BolsistaDAO bolsistaDao;

    @Autowired
    private ProfessorDAO professorDao;

    public Usuario autenticar(String email, String senha) {
        try {
            // primeiro tenta autenticar como bolsista (ou admin)
            Usuario u = bolsistaDao.autenticar(email, senha);
            if (u != null) {
                return u;
            }
            // se nao encontrar, tenta autenticar como professor
            return professorDao.autenticar(email, senha);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
