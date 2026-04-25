package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Bolsista;
import model.Frequencia;
import service.FrequenciaService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

@WebServlet("/frequencia")
public class FrequenciaServlet extends HttpServlet {

    private FrequenciaService service = new FrequenciaService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Bolsista usuarioLogado = (Bolsista) req.getSession().getAttribute("usuario");
        if (usuarioLogado == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        String dataStr = req.getParameter("data");
        String horasStr = req.getParameter("horas");
        String descricao = req.getParameter("descricao");

        Frequencia f = new Frequencia();
        f.setBolsistaId(usuarioLogado.getId());
        f.setData(LocalDate.parse(dataStr));
        f.setHorasTrabalhadas(Double.parseDouble(horasStr));
        f.setDescricao(descricao);

        if (service.registrar(f)) {
            resp.sendRedirect("frequencia");
        } else {
            req.setAttribute("erro", "Erro ao registrar frequência.");
            doGet(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Bolsista usuarioLogado = (Bolsista) req.getSession().getAttribute("usuario");
        if (usuarioLogado == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        String action = req.getParameter("action");

        if ("excluir".equals(action) && usuarioLogado.isAdmin()) {
            int id = Integer.parseInt(req.getParameter("id"));
            service.excluir(id);
            resp.sendRedirect("frequencia");
            return;
        }

        ArrayList<Frequencia> lista;
        if (usuarioLogado.isAdmin()) {
            lista = service.listarTodas();
        } else {
            lista = service.listarPorBolsista(usuarioLogado.getId());
        }

        req.setAttribute("listaFrequencia", lista);
        RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/frequencia.jsp");
        rd.forward(req, resp);
    }
}
