package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.service.BolsistaService;
import dev.matheus.cadastroBolsistas.service.LaboratorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private BolsistaService bolsistaService;

    @Autowired
    private LaboratorioService laboratorioService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            model.addAttribute("totalBolsistas", bolsistaService.listarTodos().size());
            model.addAttribute("totalLabs", laboratorioService.listarTodos().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "dashbord";
    }
}
