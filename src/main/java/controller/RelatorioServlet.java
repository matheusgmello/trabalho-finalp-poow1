package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Bolsista;
import model.Laboratorio;
import service.BolsistaService;
import service.LaboratorioService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/relatorio")
public class RelatorioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("usuario") == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        try {
            BolsistaService bolsistaService = new BolsistaService();
            LaboratorioService labService = new LaboratorioService();

            ArrayList<Bolsista> bolsistas = bolsistaService.listarTodos();
            ArrayList<Laboratorio> laboratorios = labService.listarTodos();

            // Processamento de dados (Requisito 1.4)
            long totalBolsistas = bolsistas.size();
            long totalLabs = laboratorios.size();
            long bolsistasAtivos = bolsistas.stream().filter(Bolsista::isAtivo).count();
            
            Map<String, Long> bolsistasPorCurso = bolsistas.stream()
                    .collect(Collectors.groupingBy(Bolsista::getCurso, Collectors.counting()));

            Map<String, Long> labsPorStatus = laboratorios.stream()
                    .collect(Collectors.groupingBy(Laboratorio::getStatus, Collectors.counting()));

            req.setAttribute("totalBolsistas", totalBolsistas);
            req.setAttribute("totalLabs", totalLabs);
            req.setAttribute("bolsistasAtivos", bolsistasAtivos);
            req.setAttribute("bolsistasPorCurso", bolsistasPorCurso);
            req.setAttribute("labsPorStatus", labsPorStatus);

            RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/relatorios.jsp");
            rd.forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("dashboard");
        }
    }
}
