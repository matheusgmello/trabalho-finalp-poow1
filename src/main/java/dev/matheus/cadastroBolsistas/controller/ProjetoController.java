package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.model.Projeto;
import dev.matheus.cadastroBolsistas.model.Usuario;
import dev.matheus.cadastroBolsistas.service.LaboratorioService;
import dev.matheus.cadastroBolsistas.service.ProjetoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;

@Controller
@RequestMapping("/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private LaboratorioService laboratorioService;

    @PostMapping("/salvar")
    public String salvar(@RequestParam String nome,
                         @RequestParam String descricao,
                         @RequestParam int laboratorioId,
                         HttpSession session,
                         Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

        if (!podeGerenciarLab(usuarioLogado, laboratorioId)) {
            return "redirect:/laboratorio";
        }

        try {
            Projeto p = new Projeto();
            p.setNome(nome != null ? nome.trim() : "");
            p.setDescricao(descricao != null ? descricao.trim() : "");
            p.setLaboratorioId(laboratorioId);
            
            if (p.getNome().isEmpty()) {
                return "redirect:/laboratorio?action=detalhes&id=" + laboratorioId;
            }

            projetoService.cadastrar(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/laboratorio?action=detalhes&id=" + laboratorioId;
    }

    @GetMapping("/desativar")
    public String desativar(@RequestParam int id,
                            @RequestParam int labId,
                            HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

        if (!podeGerenciarLab(usuarioLogado, labId)) {
            return "redirect:/laboratorio";
        }

        try {
            projetoService.excluir(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/laboratorio?action=detalhes&id=" + labId;
    }

    @PostMapping("/vincular")
    public String vincularBolsista(@RequestParam int bolsistaId,
                                   @RequestParam int projetoId,
                                   @RequestParam int labId,
                                   HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

        if (!podeGerenciarLab(usuarioLogado, labId)) {
            return "redirect:/laboratorio";
        }

        try {
            projetoService.vincularBolsista(bolsistaId, projetoId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/laboratorio?action=detalhes&id=" + labId;
    }

    @GetMapping("/desvincular")
    public String desvincularBolsista(@RequestParam int bolsistaId,
                                      @RequestParam int projetoId,
                                      @RequestParam int labId,
                                      HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

        if (!podeGerenciarLab(usuarioLogado, labId)) {
            return "redirect:/laboratorio";
        }

        try {
            projetoService.desvincularBolsista(bolsistaId, projetoId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/laboratorio?action=detalhes&id=" + labId;
    }

    private boolean podeGerenciarLab(Usuario usuarioLogado, int labId) {
        if (usuarioLogado.isAdmin()) return true;
        if (usuarioLogado.isProfessor()) {
            try {
                ArrayList<Laboratorio> labs = laboratorioService.listarPorCoordenador(usuarioLogado.getId());
                return labs.stream().anyMatch(l -> l.getId() == labId);
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }
}
