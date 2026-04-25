package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Laboratorio;
import service.LaboratorioService;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/laboratorio")
public class LaboratorioServlet extends HttpServlet {

    private LaboratorioService service = new LaboratorioService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String nome = req.getParameter("nome");
        String areaPesquisa = req.getParameter("areaPesquisa");
        String tituloProjeto = req.getParameter("tituloProjeto");
        String status = req.getParameter("status");

        Laboratorio lab = new Laboratorio();
        if (idStr != null && !idStr.isEmpty()) {
            lab.setId(Integer.parseInt(idStr));
        }
        lab.setNome(nome);
        lab.setAreaPesquisa(areaPesquisa);
        lab.setTituloProjeto(tituloProjeto);
        lab.setStatus(status);

        boolean sucesso;
        if (lab.getId() > 0) {
            sucesso = service.atualizar(lab);
        } else {
            sucesso = service.cadastrar(lab);
        }

        if (sucesso) {
            resp.sendRedirect("laboratorio");
        } else {
            req.setAttribute("erro", "Erro ao salvar laboratório.");
            req.setAttribute("laboratorio", lab);
            RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/cadastro-laboratorio.jsp");
            rd.forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("novo".equals(action)) {
            RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/cadastro-laboratorio.jsp");
            rd.forward(req, resp);
        } else if ("editar".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Laboratorio lab = service.buscarPorId(id);
            req.setAttribute("laboratorio", lab);
            RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/cadastro-laboratorio.jsp");
            rd.forward(req, resp);
        } else if ("excluir".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            service.excluir(id);
            resp.sendRedirect("laboratorio");
        } else {
            ArrayList<Laboratorio> lista = service.listarTodos();
            req.setAttribute("listaLaboratorios", lista);
            RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/laboratorios.jsp");
            rd.forward(req, resp);
        }
    }
}
