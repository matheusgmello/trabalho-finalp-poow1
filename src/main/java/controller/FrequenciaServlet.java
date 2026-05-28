package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Bolsista;
import model.Frequencia;
import service.BolsistaService;
import service.FrequenciaService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/*
 * servlet responsavel pelo crud de frequencia.
 * get: lista registros e carrega formulario de edicao (action=editar|excluir).
 * post: registra novo ou atualiza existente (id > 0).
 * admin pode registrar para qualquer bolsista, editar e excluir qualquer registro.
 * bolsista comum so pode registrar e editar os proprios.
 */
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

        String idStr         = limpar(req.getParameter("id"));
        String dataStr       = limpar(req.getParameter("data"));
        String horasStr      = limpar(req.getParameter("horas"));
        String descricao     = limpar(req.getParameter("descricao"));
        String bolsistaIdStr = limpar(req.getParameter("bolsistaId"));

        String erroValidacao = validarFrequencia(dataStr, horasStr, descricao);
        if (erroValidacao != null) {
            req.setAttribute("erro", erroValidacao);
            carregarPagina(req, usuarioLogado);
            req.getRequestDispatcher("WEB-INF/pages/frequencia.jsp").forward(req, resp);
            return;
        }

        int id = 0;
        if (!estaVazio(idStr)) {
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                req.setAttribute("erro", "ID da frequência inválido.");
                carregarPagina(req, usuarioLogado);
                req.getRequestDispatcher("WEB-INF/pages/frequencia.jsp").forward(req, resp);
                return;
            }
        }

        // fluxo de atualizacao de registro existente
        if (id > 0) {
            Frequencia existente = service.buscarPorId(id);
            if (existente == null) {
                req.setAttribute("erro", "Registro de frequência não encontrado.");
                carregarPagina(req, usuarioLogado);
                req.getRequestDispatcher("WEB-INF/pages/frequencia.jsp").forward(req, resp);
                return;
            }
            if (!usuarioLogado.isAdmin() && existente.getBolsistaId() != usuarioLogado.getId()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            existente.setData(LocalDate.parse(dataStr));
            existente.setHorasTrabalhadas(Double.parseDouble(horasStr));
            existente.setDescricao(descricao);

            if (service.atualizar(existente)) {
                resp.sendRedirect("frequencia");
            } else {
                req.setAttribute("erro", "Erro ao atualizar frequência.");
                carregarPagina(req, usuarioLogado);
                req.getRequestDispatcher("WEB-INF/pages/frequencia.jsp").forward(req, resp);
            }
            return;
        }

        // fluxo de novo registro
        int bolsistaId;
        if (usuarioLogado.isAdmin()) {
            // admin escolhe o bolsista pelo select no formulario
            if (estaVazio(bolsistaIdStr)) {
                req.setAttribute("erro", "Selecione o bolsista para registrar a frequência.");
                carregarPagina(req, usuarioLogado);
                req.getRequestDispatcher("WEB-INF/pages/frequencia.jsp").forward(req, resp);
                return;
            }
            try {
                bolsistaId = Integer.parseInt(bolsistaIdStr);
            } catch (NumberFormatException e) {
                req.setAttribute("erro", "Bolsista inválido.");
                carregarPagina(req, usuarioLogado);
                req.getRequestDispatcher("WEB-INF/pages/frequencia.jsp").forward(req, resp);
                return;
            }
        } else {
            // bolsista comum sempre registra para si mesmo
            bolsistaId = usuarioLogado.getId();
        }

        Frequencia f = new Frequencia();
        f.setBolsistaId(bolsistaId);
        f.setData(LocalDate.parse(dataStr));
        f.setHorasTrabalhadas(Double.parseDouble(horasStr));
        f.setDescricao(descricao);

        if (service.registrar(f)) {
            resp.sendRedirect("frequencia");
        } else {
            req.setAttribute("erro", "Erro ao registrar frequência.");
            carregarPagina(req, usuarioLogado);
            req.getRequestDispatcher("WEB-INF/pages/frequencia.jsp").forward(req, resp);
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

        if ("editar".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                Frequencia f = service.buscarPorId(id);
                if (f == null) {
                    req.setAttribute("erro", "Registro de frequência não encontrado.");
                } else if (!usuarioLogado.isAdmin() && f.getBolsistaId() != usuarioLogado.getId()) {
                    resp.sendRedirect("frequencia");
                    return;
                } else {
                    req.setAttribute("frequenciaEdicao", f);
                }
            } catch (NumberFormatException e) {
                req.setAttribute("erro", "ID da frequência inválido.");
            }
            carregarPagina(req, usuarioLogado);
            req.getRequestDispatcher("WEB-INF/pages/frequencia.jsp").forward(req, resp);
            return;
        }

        if ("excluir".equals(action) && usuarioLogado.isAdmin()) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                service.excluir(id);
            } catch (NumberFormatException e) {
                req.setAttribute("erro", "ID da frequência inválido.");
            }
            resp.sendRedirect("frequencia");
            return;
        }

        carregarPagina(req, usuarioLogado);
        req.getRequestDispatcher("WEB-INF/pages/frequencia.jsp").forward(req, resp);
    }

    // carrega lista de frequencias e, se for admin, a lista de bolsistas para o select
    private void carregarPagina(HttpServletRequest req, Bolsista usuarioLogado) {
        if (usuarioLogado.isAdmin()) {
            req.setAttribute("listaFrequencia", service.listarTodas());
            try {
                BolsistaService bolsistaService = new BolsistaService();
                req.setAttribute("listaBolsistas", bolsistaService.listarTodos());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            req.setAttribute("listaFrequencia", service.listarPorBolsista(usuarioLogado.getId()));
        }
    }

    private String validarFrequencia(String dataStr, String horasStr, String descricao) {
        if (estaVazio(dataStr)) {
            return "A data da frequência é obrigatória.";
        }
        try {
            LocalDate data = LocalDate.parse(dataStr);
            if (data.isAfter(LocalDate.now())) {
                return "A data da frequência não pode ser futura.";
            }
        } catch (Exception e) {
            return "A data da frequência é inválida.";
        }
        if (estaVazio(horasStr)) {
            return "A quantidade de horas é obrigatória.";
        }
        try {
            double horas = Double.parseDouble(horasStr);
            if (horas <= 0) {
                return "A quantidade de horas deve ser maior que zero.";
            }
            if (horas > 24) {
                return "A quantidade de horas não pode ser maior que 24.";
            }
        } catch (NumberFormatException e) {
            return "A quantidade de horas deve ser um número válido.";
        }
        if (descricao.length() > 500) {
            return "A descrição deve ter no máximo 500 caracteres.";
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
