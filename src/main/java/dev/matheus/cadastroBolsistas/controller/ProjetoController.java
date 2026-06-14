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
    public String salvar(@RequestParam(required = false) Integer id,
                         @RequestParam String nome,
                         @RequestParam String descricao,
                         @RequestParam int laboratorioId,
                         @RequestParam(required = false) String origem,
                         HttpSession session,
                         Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

        if (!podeGerenciarLab(usuarioLogado, laboratorioId)) {
            return "redirect:/laboratorio";
        }

        try {
            Projeto p;
            if (id != null && id > 0) {
                p = projetoService.buscarPorId(id);
                if (p == null || p.getLaboratorioId() != laboratorioId) {
                    if ("editar".equals(origem)) {
                        return "redirect:/laboratorio?action=editar&id=" + laboratorioId;
                    }
                    return "redirect:/laboratorio?action=detalhes&id=" + laboratorioId;
                }
            } else {
                p = new Projeto();
                p.setLaboratorioId(laboratorioId);
                p.setAtivo(true);
            }
            p.setNome(nome != null ? nome.trim() : "");
            p.setDescricao(descricao != null ? descricao.trim() : "");
            
            if (p.getNome().isEmpty()) {
                if ("editar".equals(origem)) {
                    return "redirect:/laboratorio?action=editar&id=" + laboratorioId + "&erro=O nome do projeto nao pode ser vazio";
                }
                return "redirect:/laboratorio?action=detalhes&id=" + laboratorioId;
            }

            if (p.getId() > 0) {
                projetoService.atualizar(p);
            } else {
                projetoService.cadastrar(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if ("editar".equals(origem)) {
            return "redirect:/laboratorio?action=editar&id=" + laboratorioId;
        }
        return "redirect:/laboratorio?action=detalhes&id=" + laboratorioId;
    }

    @GetMapping("/desativar")
    public String desativar(@RequestParam int id,
                            @RequestParam int labId,
                            @RequestParam(required = false) String origem,
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

        if ("editar".equals(origem)) {
            return "redirect:/laboratorio?action=editar&id=" + labId;
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
