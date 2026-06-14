package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Professor;
import dev.matheus.cadastroBolsistas.model.Usuario;
import dev.matheus.cadastroBolsistas.service.BolsistaService;
import dev.matheus.cadastroBolsistas.service.ProfessorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private BolsistaService bolsistaService;

    @Autowired
    private ProfessorService professorService;

    @GetMapping
    public String perfilPage(HttpSession session, Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuarioLogado);
        return "perfil";
    }

    @PostMapping
    public String atualizarPerfil(@RequestParam String nome,
                                  @RequestParam String email,
                                  @RequestParam String senha,
                                  @RequestParam String confirmaSenha,
                                  @RequestParam(required = false) String fotoUrl,
                                  @RequestParam(required = false) String bio,
                                  HttpSession session,
                                  Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

        nome = nome != null ? nome.trim() : "";
        email = email != null ? email.trim() : "";
        senha = senha != null ? senha.trim() : "";
        confirmaSenha = confirmaSenha != null ? confirmaSenha.trim() : "";
        fotoUrl = fotoUrl != null ? fotoUrl.trim() : "";
        bio = bio != null ? bio.trim() : "";

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            model.addAttribute("erro", "Todos os campos obrigatórios devem ser preenchidos.");
            model.addAttribute("usuario", usuarioLogado);
            return "perfil";
        }

        if (senha.length() < 6) {
            model.addAttribute("erro", "A senha deve ter pelo menos 6 caracteres.");
            model.addAttribute("usuario", usuarioLogado);
            return "perfil";
        }

        if (!senha.equals(confirmaSenha)) {
            model.addAttribute("erro", "As senhas não coincidem.");
            model.addAttribute("usuario", usuarioLogado);
            return "perfil";
        }

        try {
            if (usuarioLogado.isProfessor()) {
                Professor prof = professorService.buscarPorId(usuarioLogado.getId());
                prof.setNome(nome);
                prof.setEmail(email);
                prof.setSenha(senha);
                if (!fotoUrl.isEmpty()) {
                    prof.setFotoUrl(fotoUrl);
                }
                prof.setBio(bio);
                professorService.atualizar(prof);
                session.setAttribute("usuario", prof);
            } else {
                Bolsista bol = bolsistaService.buscarPorId(usuarioLogado.getId());
                bol.setNome(nome);
                bol.setEmail(email);
                bol.setSenha(senha);
                if (!fotoUrl.isEmpty()) {
                    bol.setFotoUrl(fotoUrl);
                }
                bol.setBio(bio);
                bolsistaService.atualizar(bol);
                session.setAttribute("usuario", bol);
            }
            model.addAttribute("sucesso", "Perfil atualizado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao atualizar perfil: " + e.getMessage());
        }

        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "perfil";
    }
}
