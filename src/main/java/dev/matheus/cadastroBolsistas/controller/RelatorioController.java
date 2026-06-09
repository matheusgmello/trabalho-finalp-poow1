package dev.matheus.cadastroBolsistas.controller;

import dev.matheus.cadastroBolsistas.model.Bolsista;
import dev.matheus.cadastroBolsistas.model.Laboratorio;
import dev.matheus.cadastroBolsistas.service.BolsistaService;
import dev.matheus.cadastroBolsistas.service.LaboratorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class RelatorioController {

    @Autowired
    private BolsistaService bolsistaService;

    @Autowired
    private LaboratorioService laboratorioService;

    @GetMapping("/relatorio")
    public String relatorio(Model model) {
        try {
            ArrayList<Bolsista> bolsistas = bolsistaService.listarTodos();
            ArrayList<Laboratorio> laboratorios = laboratorioService.listarTodos();

            long totalBolsistas = bolsistas.size();
            long totalLabs = laboratorios.size();
            long bolsistasAtivos = bolsistas.stream().filter(Bolsista::isAtivo).count();

            Map<String, Long> bolsistasPorCurso = bolsistas.stream()
                    .collect(Collectors.groupingBy(Bolsista::getCurso, Collectors.counting()));

            Map<String, Long> labsPorStatus = laboratorios.stream()
                    .collect(Collectors.groupingBy(Laboratorio::getStatus, Collectors.counting()));

            model.addAttribute("totalBolsistas", totalBolsistas);
            model.addAttribute("totalLabs", totalLabs);
            model.addAttribute("bolsistasAtivos", bolsistasAtivos);
            model.addAttribute("bolsistasPorCurso", bolsistasPorCurso);
            model.addAttribute("labsPorStatus", labsPorStatus);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/dashboard";
        }
        return "relatorios";
    }
}
