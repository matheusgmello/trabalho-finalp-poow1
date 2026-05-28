package controller;

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

/*
 * servlet responsavel pelo cadastro de administradores.
 * permite criar ate 3 admins no sistema.
 * acessivel pela tela de login, sem necessidade de estar autenticado.
 */
@WebServlet("/cadastro-admin")
public class CadastroAdminServlet extends HttpServlet {

    // limite maximo de administradores permitidos no sistema
    private static final int LIMITE_ADMINS = 3;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BolsistaService service = new BolsistaService();
            int total = service.contarAdmins();

            // bloqueia acesso ao formulario se o limite ja foi atingido
            if (total >= LIMITE_ADMINS) {
                req.setAttribute("erro", "O sistema ja possui o numero maximo de administradores (" + LIMITE_ADMINS + ").");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
                return;
            }

            // passa para a view quantas vagas ainda restam
            req.setAttribute("adminsRestantes", LIMITE_ADMINS - total);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao verificar administradores: " + e.getMessage());
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            return;
        }

        req.getRequestDispatcher("WEB-INF/pages/cadastro-admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nome          = limpar(req.getParameter("nome"));
        String email         = limpar(req.getParameter("email"));
        String senha         = limpar(req.getParameter("senha"));
        String confirmaSenha = limpar(req.getParameter("confirmaSenha"));

        String erro = validar(nome, email, senha, confirmaSenha);
        if (erro != null) {
            req.setAttribute("erro", erro);
            req.getRequestDispatcher("WEB-INF/pages/cadastro-admin.jsp").forward(req, resp);
            return;
        }

        try {
            BolsistaService service = new BolsistaService();

            // revalida o limite no post para evitar cadastros duplicados em requisicoes paralelas
            if (service.contarAdmins() >= LIMITE_ADMINS) {
                req.setAttribute("erro", "O sistema ja possui o numero maximo de administradores (" + LIMITE_ADMINS + ").");
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
            admin.setCurso("Gestao");
            admin.setMatricula("ADM001");

            boolean sucesso = service.inserir(admin);
            if (sucesso) {
                req.setAttribute("sucesso", "Administrador cadastrado com sucesso! Faca o login.");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            } else {
                req.setAttribute("erro", "Nao foi possivel cadastrar o administrador.");
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
            return "Informe um e-mail valido.";
        }
        if (estaVazio(senha) || senha.length() < 6) {
            return "A senha deve ter pelo menos 6 caracteres.";
        }
        if (!senha.equals(confirmaSenha)) {
            return "As senhas nao coincidem.";
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
