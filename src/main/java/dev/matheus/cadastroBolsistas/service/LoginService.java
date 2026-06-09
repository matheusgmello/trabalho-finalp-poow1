package dev.matheus.cadastroBolsistas.service;

import dev.matheus.cadastroBolsistas.dao.BolsistaDAO;
import dev.matheus.cadastroBolsistas.model.Bolsista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private BolsistaDAO dao;

    public Bolsista autenticar(String email, String senha) {
        try {
            return dao.autenticar(email, senha);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
