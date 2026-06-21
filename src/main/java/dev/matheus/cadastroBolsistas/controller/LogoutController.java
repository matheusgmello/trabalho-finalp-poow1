package dev.matheus.cadastroBolsistas.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * controller responsavel pelo logout do usuario.
 * invalida a sessao http e redireciona para a tela de login.
 */
@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
