package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Bolsista;
import service.LoginService;

import java.io.IOException;
import java.io.PrintWriter;

/*
 * servlet responsavel pelo login do sistema.
 * autentica o usuario e armazena o objeto bolsista na sessao.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private LoginService loginService = new LoginService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("<html><body>usuario clicou em alguma coisa</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        email = email != null ? email.trim() : "";
        senha = senha != null ? senha.trim() : "";

        Bolsista bolsistaAutenticado = loginService.autenticar(email, senha);

        if (bolsistaAutenticado != null) {
            // salva o usuario na sessao para ser verificado em todas as paginas protegidas
            req.getSession().setAttribute("usuario", bolsistaAutenticado);
            resp.sendRedirect("dashboard");
        } else {
            // credenciais invalidas — volta para o login com mensagem de erro
            req.setAttribute("erro", "USUARIO OU SENHA INCORRETOS");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}
