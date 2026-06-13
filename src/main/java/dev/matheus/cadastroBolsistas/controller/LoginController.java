package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.model.Usuario;
import dev.matheus.cadastroBolsistas.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * responsavel pelo login do sistema.
 * autentica o usuario (bolsista, admin ou professor) e armazena o objeto na sessao.
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String autenticar(@RequestParam String email,
                             @RequestParam String senha,
                             HttpSession session,
                             Model model) {
        email = email != null ? email.trim() : "";
        senha = senha != null ? senha.trim() : "";

        Usuario usuarioAutenticado = loginService.autenticar(email, senha);

        if (usuarioAutenticado != null) {
            // salva o usuario na sessao para ser verificado em todas as paginas protegidas
            session.setAttribute("usuario", usuarioAutenticado);
            return "redirect:/dashboard";
        }

        // credenciais invalidas — volta para o login com mensagem de erro
        model.addAttribute("erro", "USUARIO OU SENHA INCORRETOS");
        return "login";
    }
}
