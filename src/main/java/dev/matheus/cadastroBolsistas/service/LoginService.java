package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.BolsistaDAO;
import dev.matheus.cadastroBolsistas.dao.ProfessorDAO;
import dev.matheus.cadastroBolsistas.model.Usuario;
import dev.matheus.cadastroBolsistas.util.SecurityUtil;
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
            String senhaHash = SecurityUtil.hashSenha(senha);
            /*
             * primeiro tenta autenticar o usuario como bolsista ou administrador
             */
            Usuario u = bolsistaDao.autenticar(email, senhaHash);
            if (u != null) {
                return u;
            }
            /*
             * se nao encontrar nenhum bolsista ou administrador tenta como professor
             */
            return professorDao.autenticar(email, senhaHash);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
