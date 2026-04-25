package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Bolsista;
import service.BolsistaService;
import service.LaboratorioService;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Bolsista usuario = (Bolsista) req.getSession().getAttribute("usuario");
        if (usuario == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        try {
            req.setAttribute("totalBolsistas", new BolsistaService().listarTodos().size());
            req.setAttribute("totalLabs", new LaboratorioService().listarTodos().size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/dashbord.jsp");
        rd.forward(req, resp);
    }
}
