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

/*
 * servlet responsavel pelo crud de bolsistas.
 * get: lista, busca, exibe formulario e excluir (action=novo|editar|excluir|exportar).
 * post: salva novo bolsista ou atualiza existente (id > 0).
 * bolsista comum so pode editar o proprio cadastro.
 */
@WebServlet("/bolsista")
public class BolsistaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Bolsista usuarioLogado = (Bolsista) req.getSession().getAttribute("usuario");
        if (usuarioLogado == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        String idStr = limpar(req.getParameter("id"));
        String nome = limpar(req.getParameter("nome"));
        String dataNascimentoStr = limpar(req.getParameter("dataNascimento"));
        String curso = limpar(req.getParameter("curso"));
        String email = limpar(req.getParameter("email"));
        String matricula = limpar(req.getParameter("matricula"));
        String cpf = limpar(req.getParameter("cpf"));
        String telefone = limpar(req.getParameter("telefone"));
        String senha = limpar(req.getParameter("senha"));
        String laboratorioIdStr = limpar(req.getParameter("laboratorioId"));
        String tipoUsuario = limpar(req.getParameter("tipoUsuario"));
        String fotoUrl = limpar(req.getParameter("fotoUrl"));

        int id = 0;
        if (!estaVazio(idStr)) {
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                req.setAttribute("erro", "ID do bolsista inválido.");
                encaminharFormulario(req, resp, null);
                return;
            }
        }

        // bolsista comum so pode editar o proprio registro
        if (!usuarioLogado.isAdmin() && (id == 0 || id != usuarioLogado.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Você não tem permissão para esta ação.");
            return;
        }

        Bolsista b = new Bolsista();
        b.setId(id);
        b.setNome(nome);
        b.setCurso(curso);
        b.setEmail(email);
        b.setMatricula(matricula);
        b.setCpf(cpf);
        b.setTelefone(telefone);
        b.setSenha(senha);
        b.setAtivo(true);
        b.setTipoUsuario(!estaVazio(tipoUsuario) ? tipoUsuario : "BOLSISTA");
        b.setFotoUrl(fotoUrl);

        String erroValidacao = validarBolsista(b, dataNascimentoStr, laboratorioIdStr, usuarioLogado);
        if (erroValidacao != null) {
            req.setAttribute("erro", erroValidacao);
            encaminharFormulario(req, resp, b);
            return;
        }

        b.setDataNascimento(LocalDate.parse(dataNascimentoStr));

        if (!estaVazio(laboratorioIdStr)) {
            int labId = Integer.parseInt(laboratorioIdStr);
            LaboratorioService labService = new LaboratorioService();
            if (b.getId() == 0 && !labService.temVaga(labId)) {
                req.setAttribute("erro", "Laboratório atingiu a capacidade máxima!");
                encaminharFormulario(req, resp, b);
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
                req.setAttribute("erro", "Problemas ao salvar o bolsista.");
                encaminharFormulario(req, resp, b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Algo aconteceu: " + e.getMessage());
            encaminharFormulario(req, resp, b);
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
                    req.setAttribute("erro", "Bolsista inválido para edição.");
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
            } catch (SQLException e) {
                e.printStackTrace();
                req.setAttribute("erro", "Erro ao excluir bolsista.");
            } catch (NumberFormatException e) {
                req.setAttribute("erro", "ID do bolsista inválido.");
            }
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
            req.setAttribute("erro", "Erro ao listar bolsistas: " + e.getMessage());
        }
        forwardParaListagem(req, resp);
    }

    private String validarBolsista(Bolsista b, String dataNascimentoStr, String laboratorioIdStr, Bolsista usuarioLogado) {
        if (estaVazio(b.getNome()) || b.getNome().length() < 3) {
            return "O nome do bolsista deve ter pelo menos 3 caracteres.";
        }
        if (estaVazio(dataNascimentoStr)) {
            return "A data de nascimento é obrigatória.";
        }
        try {
            LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr);
            if (dataNascimento.isAfter(LocalDate.now())) {
                return "A data de nascimento não pode ser futura.";
            }
        } catch (Exception e) {
            return "A data de nascimento é inválida.";
        }
        if (estaVazio(b.getCurso())) {
            return "O curso é obrigatório.";
        }
        if (estaVazio(b.getEmail()) || !b.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return "Informe um e-mail válido.";
        }
        if (estaVazio(b.getMatricula())) {
            return "A matrícula é obrigatória.";
        }
        if (estaVazio(b.getSenha()) || b.getSenha().length() < 6) {
            return "A senha deve ter pelo menos 6 caracteres.";
        }
        if (!"ADMIN".equals(b.getTipoUsuario()) && !"BOLSISTA".equals(b.getTipoUsuario())) {
            return "Tipo de usuário inválido.";
        }
        if (!usuarioLogado.isAdmin() && "ADMIN".equals(b.getTipoUsuario())) {
            return "Apenas administradores podem definir outro administrador.";
        }
        if (!estaVazio(laboratorioIdStr)) {
            try {
                if (Integer.parseInt(laboratorioIdStr) <= 0) {
                    return "Laboratório inválido.";
                }
            } catch (NumberFormatException e) {
                return "Laboratório inválido.";
            }
        }
        return null;
    }

    private void encaminharFormulario(HttpServletRequest req, HttpServletResponse resp, Bolsista b) throws ServletException, IOException {
        LaboratorioService labService = new LaboratorioService();
        req.setAttribute("laboratorios", labService.listarTodos());
        if (b != null) {
            req.setAttribute("bolsista", b);
        }
        RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/cadastro-bolsista.jsp");
        rd.forward(req, resp);
    }

    // gera e envia o arquivo csv com a lista de bolsistas para download
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

    private String limpar(String valor) {
        return valor != null ? valor.trim() : "";
    }

    private boolean estaVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
