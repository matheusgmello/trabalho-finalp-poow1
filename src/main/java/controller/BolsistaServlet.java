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
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@WebServlet("/bolsista")
public class BolsistaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Bolsista usuarioLogado = (Bolsista) req.getSession().getAttribute("usuario");
        if (usuarioLogado == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        String idStr = req.getParameter("id");
        String nome = req.getParameter("nome");
        String dataNascimentoStr = req.getParameter("dataNascimento");
        String curso = req.getParameter("curso");
        String email = req.getParameter("email");
        String matricula = req.getParameter("matricula");
        String cpf = req.getParameter("cpf");
        String telefone = req.getParameter("telefone");
        String senha = req.getParameter("senha");
        String laboratorioIdStr = req.getParameter("laboratorioId");
        String tipoUsuario = req.getParameter("tipoUsuario");

        // Regra de Segurança: Apenas ADMIN pode cadastrar/editar outros ou mudar tipo de usuário
        if (!usuarioLogado.isAdmin() && (idStr == null || Integer.parseInt(idStr) != usuarioLogado.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Você não tem permissão para esta ação.");
            return;
        }

        Bolsista b = new Bolsista();
        if (idStr != null && !idStr.isEmpty()) {
            b.setId(Integer.parseInt(idStr));
        }
        b.setNome(nome);
        b.setDataNascimento(LocalDate.parse(dataNascimentoStr));
        b.setCurso(curso);
        b.setEmail(email);
        b.setMatricula(matricula);
        b.setCpf(cpf);
        b.setTelefone(telefone);
        b.setSenha(senha);
        b.setAtivo(true);
        b.setTipoUsuario(tipoUsuario != null ? tipoUsuario : "BOLSISTA");
        
        if (laboratorioIdStr != null && !laboratorioIdStr.isEmpty()) {
            int labId = Integer.parseInt(laboratorioIdStr);
            LaboratorioService labService = new LaboratorioService();
            // Validação de Capacidade (Requisito 1.3/Evolução)
            if (b.getId() == 0 && !labService.temVaga(labId)) {
                req.setAttribute("erro", "Laboratório atingiu a capacidade máxima!");
                doGet(req, resp);
                return;
            }
            b.setLaboratorioId(labId);
        }

        try {
            BolsistaService service = new BolsistaService();
            boolean sucesso;
            if (b.getId() > 0) {
                sucesso = service.atualizar(b);
            } else {
                sucesso = service.inserir(b);
            }

            if (sucesso) {
                resp.sendRedirect("bolsista");
            } else {
                req.setAttribute("erro", "PROBLEMAS AO SALVAR O BOLSISTA");
                doGet(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "ALGO ACONTECEU: " + e.getMessage());
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
        
        if ("exportar".equals(action)) {
            exportarParaCSV(resp);
            return;
        }

        if ("novo".equals(action) || "editar".equals(action)) {
            LaboratorioService labService = new LaboratorioService();
            req.setAttribute("laboratorios", labService.listarTodos());

            if ("editar".equals(action)) {
                try {
                    int id = Integer.parseInt(req.getParameter("id"));
                    // Apenas ADMIN edita qualquer um, BOLSISTA edita só a si mesmo
                    if (!usuarioLogado.isAdmin() && id != usuarioLogado.getId()) {
                        resp.sendRedirect("bolsista");
                        return;
                    }
                    BolsistaService service = new BolsistaService();
                    Bolsista b = service.listarTodos().stream()
                                    .filter(bol -> bol.getId() == id)
                                    .findFirst().orElse(null);
                    req.setAttribute("bolsista", b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/cadastro-bolsista.jsp");
            rd.forward(req, resp);
            return;
        }

        if ("excluir".equals(action) && usuarioLogado.isAdmin()) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                new BolsistaService().excluir(id);
                resp.sendRedirect("bolsista");
                return;
            } catch (SQLException e) { e.printStackTrace(); }
        }

        String buscaNome = req.getParameter("buscaNome");
        String buscaCurso = req.getParameter("buscaCurso");

        try {
            BolsistaService service = new BolsistaService();
            ArrayList<Bolsista> listaBolsistas;

            if (buscaNome != null && !buscaNome.isEmpty()) {
                listaBolsistas = service.buscarPorNome(buscaNome);
            } else if (buscaCurso != null && !buscaCurso.isEmpty()) {
                listaBolsistas = service.buscarPorCurso(buscaCurso);
            } else {
                listaBolsistas = service.listarTodos();
            }

            req.setAttribute("listabolsistas", listaBolsistas);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("erro", "ERRO AO LISTAR BOLSISTAS: " + e.getMessage());
        }
        forwardParaListagem(req, resp);
    }

    private void exportarParaCSV(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/csv");
        resp.setHeader("Content-Disposition", "attachment; filename=bolsistas.csv");
        try (PrintWriter writer = resp.getWriter()) {
            writer.println("ID,Nome,Email,Curso,Laboratorio,Status");
            BolsistaService service = new BolsistaService();
            for (Bolsista b : service.listarTodos()) {
                writer.println(b.getId() + "," + b.getNome() + "," + b.getEmail() + "," + 
                               b.getCurso() + "," + b.getNomeLaboratorio() + "," + (b.isAtivo() ? "Ativo" : "Inativo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void forwardParaListagem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/bolsistas.jsp");
        rd.forward(req, resp);
    }
}
