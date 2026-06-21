package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.model.Usuario;
import dev.matheus.cadastroBolsistas.service.BolsistaService;
import dev.matheus.cadastroBolsistas.service.LaboratorioService;
import dev.matheus.cadastroBolsistas.service.ProjetoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

/*
 * controller responsavel pelo dashboard principal do sistema.
 * renderiza uma visao personalizada por tipo de usuario:
 * - admin: contadores e atalhos gerais
 * - professor: laboratorios coordenados e bolsistas supervisionados
 * - bolsista: equipe do laboratorio e projetos vinculados
 */
@Controller
public class DashboardController {

    @Autowired
    private BolsistaService bolsistaService;

    @Autowired
    private LaboratorioService laboratorioService;

    @Autowired
    private ProjetoService projetoService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            if (usuarioLogado.isBolsista()) {
                Bolsista bolsista = (Bolsista) usuarioLogado;
                int labId = bolsista.getLaboratorioId();
                if (labId > 0) {
                    Laboratorio lab = laboratorioService.buscarPorId(labId);
                    model.addAttribute("meuLaboratorio", lab);
                    
                    ArrayList<Bolsista> equipe = bolsistaService.buscarPorLaboratorio(labId);
                    model.addAttribute("equipe", equipe);
                }
                // Adiciona os projetos do bolsista ao model
                model.addAttribute("meusProjetos", projetoService.listarPorBolsista(bolsista.getId()));
            } else if (usuarioLogado.isProfessor()) {
                // Professor: Carregar laboratórios e bolsistas coordenados
                ArrayList<Laboratorio> meusLabs = laboratorioService.listarPorCoordenador(usuarioLogado.getId());
                int totalBolsistasCoordenados = 0;
                for (Laboratorio l : meusLabs) {
                    totalBolsistasCoordenados += laboratorioService.contarBolsistasNoLaboratorio(l.getId());
                }
                model.addAttribute("meusLaboratorios", meusLabs);
                model.addAttribute("totalBolsistasCoordenados", totalBolsistasCoordenados);
            } else {
                // Admin: Carregar dados agregados gerais do sistema
                model.addAttribute("totalBolsistas", bolsistaService.listarTodos().size());
                model.addAttribute("totalLabs", laboratorioService.listarTodos().size());
                model.addAttribute("totalProjetos", projetoService.listarTodos().size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "dashbord";
    }
}
