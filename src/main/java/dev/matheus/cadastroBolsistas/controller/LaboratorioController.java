package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.service.BolsistaService;
import dev.matheus.cadastroBolsistas.service.LaboratorioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequestMapping("/laboratorio")
public class LaboratorioController {

    @Autowired
    private LaboratorioService laboratorioService;

    @Autowired
    private BolsistaService bolsistaService;

    @GetMapping
    public String handleGet(@RequestParam(required = false) String action,
                            @RequestParam(required = false) String id,
                            HttpSession session,
                            Model model) {
        Bolsista usuarioLogado = (Bolsista) session.getAttribute("usuario");

        if ("novo".equals(action)) {
            if (!usuarioLogado.isAdmin()) {
                return "redirect:/laboratorio";
            }
            return "cadastro-laboratorio";
        }

        if ("editar".equals(action)) {
            if (!usuarioLogado.isAdmin()) {
                return "redirect:/laboratorio";
            }
            try {
                int labId = Integer.parseInt(id);
                Laboratorio lab = laboratorioService.buscarPorId(labId);
                model.addAttribute("laboratorio", lab);
                return "cadastro-laboratorio";
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID do laboratorio invalido.");
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("erro", "Erro ao carregar laboratorio.");
            }
        }

        if ("excluir".equals(action)) {
            if (!usuarioLogado.isAdmin()) {
                return "redirect:/laboratorio";
            }
            try {
                laboratorioService.excluir(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID do laboratorio invalido.");
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("erro", "Erro ao excluir laboratorio.");
            }
            return "redirect:/laboratorio";
        }

        if ("detalhes".equals(action)) {
            try {
                int labId = Integer.parseInt(id);
                Laboratorio lab = laboratorioService.buscarPorId(labId);
                ArrayList<Bolsista> bolsistas = bolsistaService.buscarPorLaboratorio(labId);
                model.addAttribute("laboratorio", lab);
                model.addAttribute("bolsistas", bolsistas);
                return "detalhes-laboratorio";
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/laboratorio";
            }
        }

        return listarLaboratorios(model);
    }

    @PostMapping
    public String salvar(@RequestParam(required = false) String id,
                         @RequestParam(required = false) String nome,
                         @RequestParam(required = false) String areaPesquisa,
                         @RequestParam(required = false) String tituloProjeto,
                         @RequestParam(required = false) String status,
                         @RequestParam(required = false) String capacidade,
                         @RequestParam(required = false) String coordenador,
                         HttpSession session,
                         Model model) {
        Bolsista usuarioLogado = (Bolsista) session.getAttribute("usuario");

        if (!usuarioLogado.isAdmin()) {
            return "redirect:/laboratorio";
        }

        Laboratorio lab = new Laboratorio();

        if (!estaVazio(id)) {
            try {
                lab.setId(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                model.addAttribute("erro", "ID do laboratorio invalido.");
                model.addAttribute("laboratorio", lab);
                return "cadastro-laboratorio";
            }
        }

        lab.setNome(limpar(nome));
        lab.setAreaPesquisa(limpar(areaPesquisa));
        lab.setTituloProjeto(limpar(tituloProjeto));
        lab.setStatus(limpar(status));
        lab.setCoordenador(limpar(coordenador));

        String erroValidacao = validarLaboratorio(lab, capacidade);
        if (erroValidacao != null) {
            model.addAttribute("erro", erroValidacao);
            model.addAttribute("laboratorio", lab);
            return "cadastro-laboratorio";
        }

        lab.setCapacidade(Integer.parseInt(capacidade));

        try {
            boolean sucesso = lab.getId() > 0 ? laboratorioService.atualizar(lab) : laboratorioService.cadastrar(lab);
            if (sucesso) {
                return "redirect:/laboratorio";
            } else {
                model.addAttribute("erro", "Erro ao salvar laboratorio.");
                model.addAttribute("laboratorio", lab);
                return "cadastro-laboratorio";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao salvar laboratorio: " + e.getMessage());
            model.addAttribute("laboratorio", lab);
            return "cadastro-laboratorio";
        }
    }

    private String listarLaboratorios(Model model) {
        try {
            model.addAttribute("listaLaboratorios", laboratorioService.listarTodos());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao listar laboratorios: " + e.getMessage());
        }
        return "laboratorios";
    }

    private String validarLaboratorio(Laboratorio lab, String capacidadeStr) {
        if (estaVazio(lab.getNome()) || lab.getNome().length() < 3) {
            return "O nome do laboratorio deve ter pelo menos 3 caracteres.";
        }
        if (estaVazio(lab.getAreaPesquisa())) {
            return "A area de pesquisa e obrigatoria.";
        }
        if (estaVazio(lab.getCoordenador())) {
            return "O professor coordenador e obrigatorio.";
        }
        if (estaVazio(lab.getTituloProjeto()) || lab.getTituloProjeto().length() < 5) {
            return "O titulo do projeto deve ter pelo menos 5 caracteres.";
        }
        if (!"Ativo".equals(lab.getStatus()) && !"Em Pausa".equals(lab.getStatus()) && !"Concluido".equals(lab.getStatus())) {
            return "Status do laboratorio invalido.";
        }
        if (estaVazio(capacidadeStr)) {
            return "A capacidade maxima e obrigatoria.";
        }
        try {
            int cap = Integer.parseInt(capacidadeStr);
            if (cap <= 0) {
                return "A capacidade maxima deve ser maior que zero.";
            }
        } catch (NumberFormatException e) {
            return "A capacidade maxima deve ser um numero inteiro.";
        }
        return null;
    }

    private String limpar(String valor) {
        return valor != null ? valor.trim() : "";
    }

    private boolean estaVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
