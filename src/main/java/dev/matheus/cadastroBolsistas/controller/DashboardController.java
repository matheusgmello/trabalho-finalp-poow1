package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.model.Usuario;
import dev.matheus.cadastroBolsistas.service.BolsistaService;
import dev.matheus.cadastroBolsistas.service.LaboratorioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class DashboardController {

    @Autowired
    private BolsistaService bolsistaService;

    @Autowired
    private LaboratorioService laboratorioService;

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
            } else {
                model.addAttribute("totalBolsistas", bolsistaService.listarTodos().size());
                model.addAttribute("totalLabs", laboratorioService.listarTodos().size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "dashbord";
    }
}
