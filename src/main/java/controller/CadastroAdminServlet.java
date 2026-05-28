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

@WebServlet("/cadastro-admin")
public class CadastroAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BolsistaService service = new BolsistaService();
            if (service.existeAdmin()) {
                req.setAttribute("erro", "Já existe um administrador cadastrado no sistema.");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao verificar administrador: " + e.getMessage());
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            return;
        }

        req.getRequestDispatcher("WEB-INF/pages/cadastro-admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nome  = limpar(req.getParameter("nome"));
        String email = limpar(req.getParameter("email"));
        String senha = limpar(req.getParameter("senha"));
        String confirmaSenha = limpar(req.getParameter("confirmaSenha"));

        String erro = validar(nome, email, senha, confirmaSenha);
        if (erro != null) {
            req.setAttribute("erro", erro);
            req.getRequestDispatcher("WEB-INF/pages/cadastro-admin.jsp").forward(req, resp);
            return;
        }

        try {
            BolsistaService service = new BolsistaService();

            if (service.existeAdmin()) {
                req.setAttribute("erro", "Já existe um administrador cadastrado no sistema.");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
                return;
            }

            Bolsista admin = new Bolsista();
            admin.setNome(nome);
            admin.setEmail(email);
            admin.setSenha(senha);
            admin.setTipoUsuario("ADMIN");
            admin.setAtivo(true);
            admin.setDataNascimento(LocalDate.of(1990, 1, 1));
            admin.setCurso("Gestão");
            admin.setMatricula("ADM001");

            boolean sucesso = service.inserir(admin);
            if (sucesso) {
                req.setAttribute("sucesso", "Administrador cadastrado com sucesso! Faça o login.");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            } else {
                req.setAttribute("erro", "Não foi possível cadastrar o administrador.");
                req.getRequestDispatcher("WEB-INF/pages/cadastro-admin.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao cadastrar administrador: " + e.getMessage());
            req.getRequestDispatcher("WEB-INF/pages/cadastro-admin.jsp").forward(req, resp);
        }
    }

    private String validar(String nome, String email, String senha, String confirmaSenha) {
        if (estaVazio(nome) || nome.length() < 3) {
            return "O nome deve ter pelo menos 3 caracteres.";
        }
        if (estaVazio(email) || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return "Informe um e-mail válido.";
        }
        if (estaVazio(senha) || senha.length() < 6) {
            return "A senha deve ter pelo menos 6 caracteres.";
        }
        if (!senha.equals(confirmaSenha)) {
            return "As senhas não coincidem.";
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
