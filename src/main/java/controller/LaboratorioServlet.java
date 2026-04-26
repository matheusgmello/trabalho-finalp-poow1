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

@WebServlet("/laboratorio")
public class LaboratorioServlet extends HttpServlet {

    private LaboratorioService service = new LaboratorioService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Bolsista usuarioLogado = (Bolsista) req.getSession().getAttribute("usuario");
        if (usuarioLogado == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        if (!usuarioLogado.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String idStr = limpar(req.getParameter("id"));
        String nome = limpar(req.getParameter("nome"));
        String areaPesquisa = limpar(req.getParameter("areaPesquisa"));
        String tituloProjeto = limpar(req.getParameter("tituloProjeto"));
        String status = limpar(req.getParameter("status"));
        String capacidadeStr = limpar(req.getParameter("capacidade"));
        String coordenador = limpar(req.getParameter("coordenador"));

        Laboratorio lab = new Laboratorio();

        if (!estaVazio(idStr)) {
            try {
                lab.setId(Integer.parseInt(idStr));
            } catch (NumberFormatException e) {
                req.setAttribute("erro", "ID do laboratório inválido.");
                encaminharFormulario(req, resp, lab);
                return;
            }
        }

        lab.setNome(nome);
        lab.setAreaPesquisa(areaPesquisa);
        lab.setTituloProjeto(tituloProjeto);
        lab.setStatus(status);
        lab.setCoordenador(coordenador);

        String erroValidacao = validarLaboratorio(lab, capacidadeStr);
        if (erroValidacao != null) {
            req.setAttribute("erro", erroValidacao);
            encaminharFormulario(req, resp, lab);
            return;
        }

        lab.setCapacidade(Integer.parseInt(capacidadeStr));

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
            encaminharFormulario(req, resp, lab);
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

        if ("novo".equals(action)) {
            if (!usuarioLogado.isAdmin()) {
                resp.sendRedirect("laboratorio");
                return;
            }
            RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/cadastro-laboratorio.jsp");
            rd.forward(req, resp);
        } else if ("editar".equals(action)) {
            if (!usuarioLogado.isAdmin()) {
                resp.sendRedirect("laboratorio");
                return;
            }
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                Laboratorio lab = service.buscarPorId(id);
                req.setAttribute("laboratorio", lab);
                RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/cadastro-laboratorio.jsp");
                rd.forward(req, resp);
            } catch (NumberFormatException e) {
                req.setAttribute("erro", "ID do laboratório inválido.");
                listarLaboratorios(req, resp);
            }
        } else if ("excluir".equals(action)) {
            if (!usuarioLogado.isAdmin()) {
                resp.sendRedirect("laboratorio");
                return;
            }
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                service.excluir(id);
            } catch (NumberFormatException e) {
                req.setAttribute("erro", "ID do laboratório inválido.");
            }
            resp.sendRedirect("laboratorio");
        } else if ("detalhes".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                Laboratorio lab = service.buscarPorId(id);

                BolsistaService bolsistaService = new BolsistaService();
                ArrayList<Bolsista> bolsistas = bolsistaService.buscarPorLaboratorio(id);

                req.setAttribute("laboratorio", lab);
                req.setAttribute("bolsistas", bolsistas);

                RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/detalhes-laboratorio.jsp");
                rd.forward(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendRedirect("laboratorio");
            }
        } else {
            listarLaboratorios(req, resp);
        }
    }

    private String validarLaboratorio(Laboratorio lab, String capacidadeStr) {
        if (estaVazio(lab.getNome()) || lab.getNome().length() < 3) {
            return "O nome do laboratório deve ter pelo menos 3 caracteres.";
        }
        if (estaVazio(lab.getAreaPesquisa())) {
            return "A área de pesquisa é obrigatória.";
        }
        if (estaVazio(lab.getCoordenador())) {
            return "O professor coordenador é obrigatório.";
        }
        if (estaVazio(lab.getTituloProjeto()) || lab.getTituloProjeto().length() < 5) {
            return "O título do projeto deve ter pelo menos 5 caracteres.";
        }
        if (!"Ativo".equals(lab.getStatus()) && !"Em Pausa".equals(lab.getStatus()) && !"Concluido".equals(lab.getStatus())) {
            return "Status do laboratório inválido.";
        }
        if (estaVazio(capacidadeStr)) {
            return "A capacidade máxima é obrigatória.";
        }
        try {
            int capacidade = Integer.parseInt(capacidadeStr);
            if (capacidade <= 0) {
                return "A capacidade máxima deve ser maior que zero.";
            }
        } catch (NumberFormatException e) {
            return "A capacidade máxima deve ser um número inteiro.";
        }
        return null;
    }

    private void encaminharFormulario(HttpServletRequest req, HttpServletResponse resp, Laboratorio lab) throws ServletException, IOException {
        req.setAttribute("laboratorio", lab);
        RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/cadastro-laboratorio.jsp");
        rd.forward(req, resp);
    }

    private void listarLaboratorios(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<Laboratorio> lista = service.listarTodos();
        req.setAttribute("listaLaboratorios", lista);
        RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/laboratorios.jsp");
        rd.forward(req, resp);
    }

    private String limpar(String valor) {
        return valor != null ? valor.trim() : "";
    }

    private boolean estaVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
