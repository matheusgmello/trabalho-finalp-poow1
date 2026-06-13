package dev.matheus.cadastroBolsistas.config;

import dev.matheus.cadastroBolsistas.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/*
 * intercepta todas as requisicoes e redireciona para o login se o usuario nao estiver autenticado.
 * rotas publicas (login e cadastro-admin) sao liberadas sem verificacao de sessao.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();

        // libera rotas publicas sem autenticacao
        if (path.equals("/login") || path.startsWith("/cadastro-admin") || path.startsWith("/css/")
                || path.startsWith("/js/") || path.startsWith("/images/")) {
            return true;
        }

        jakarta.servlet.http.HttpSession session = request.getSession(false);
        Usuario usuario = session != null ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}
