package service;

import dao.BolsistaDAO;
import model.Bolsista;

public class LoginService {

    public Bolsista autenticar(String email, String senha) {
        try {
            BolsistaDAO dao = new BolsistaDAO();
            return dao.autenticar(email, senha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
