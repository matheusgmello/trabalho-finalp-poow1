package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Bolsista;
import service.BolsistaService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@WebServlet("/bolsista")
public class BolsistaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        if (laboratorioIdStr != null && !laboratorioIdStr.isEmpty()) {
            b.setLaboratorioId(Integer.parseInt(laboratorioIdStr));
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
        String action = req.getParameter("action");
        
        if ("novo".equals(action) || "editar".equals(action)) {
            service.LaboratorioService labService = new service.LaboratorioService();
            req.setAttribute("laboratorios", labService.listarTodos());

            if ("editar".equals(action)) {
                try {
                    int id = Integer.parseInt(req.getParameter("id"));
                    BolsistaService service = new BolsistaService();
                    // Como não temos buscarPorId no BolsistaService, vamos precisar adicionar ou filtrar da lista
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

    private void forwardParaListagem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("WEB-INF/pages/bolsistas.jsp");
        rd.forward(req, resp);
    }
}
