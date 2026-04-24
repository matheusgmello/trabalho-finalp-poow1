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
        String nome = req.getParameter("nome");
        String dataNascimentoStr = req.getParameter("dataNascimento");
        String curso = req.getParameter("curso");
        String email = req.getParameter("email");
        String matricula = req.getParameter("matricula");
        String cpf = req.getParameter("cpf");
        String telefone = req.getParameter("telefone");
        String senha = req.getParameter("senha");

        Bolsista b = new Bolsista();
        b.setNome(nome);
        b.setDataNascimento(LocalDate.parse(dataNascimentoStr));
        b.setCurso(curso);
        b.setEmail(email);
        b.setMatricula(matricula);
        b.setCpf(cpf);
        b.setTelefone(telefone);
        b.setSenha(senha);
        b.setAtivo(true);

        try {
            BolsistaService service = new BolsistaService();
            if (service.inserir(b)) {
                doGet(req, resp);
            } else {
                req.setAttribute("erro", "PROBLEMAS AO SALVAR O BOLSISTA");
                forwardParaListagem(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "ALGO ACONTECEU: " + e.getMessage());
            forwardParaListagem(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        
        if ("novo".equals(action)) {
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
