<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="topbar">
    <div class="topbar-left">
        <span>[SYS: SISBOLSA]</span>
        <div class="topbar-status">
            <span class="status-dot"></span>
            <span>LIVE</span>
        </div>
    </div>
    <div class="topbar-right">
        <span>[OP: ${usuario.nome != null ? usuario.nome.toUpperCase() : 'DESCONHECIDO'} // ROLE: ${usuario.tipoUsuario}]</span>
    </div>
</div>

<div class="sidebar">
    <h2>SisBolsa</h2>
    
    <div class="user-profile-widget">
        <c:choose>
            <c:when test="${not empty usuario.fotoUrl}">
                <img src="${usuario.fotoUrl}" alt="Avatar" class="profile-img" style="width: 40px; height: 40px; border-radius: 50%; object-fit: cover; flex-shrink: 0; border: 2px solid var(--primary-color);">
            </c:when>
            <c:otherwise>
                <div class="profile-placeholder" style="width: 40px; height: 40px; border-radius: 50%; background-color: #e2e8f0; color: var(--text-muted); display: flex; align-items: center; justify-content: center; font-size: 1.1rem; flex-shrink: 0;"><i class="fas fa-user"></i></div>
            </c:otherwise>
        </c:choose>
        <div class="profile-info">
            <span class="profile-name">${usuario.nome}</span>
            <span class="profile-role">
                <c:choose>
                    <c:when test="${usuario.admin}">ADMINISTRADOR</c:when>
                    <c:when test="${usuario.professor}">PROFESSOR</c:when>
                    <c:otherwise>${not empty usuario.cargo ? usuario.cargo.name() : 'BOLSISTA'}</c:otherwise>
                </c:choose>
            </span>
        </div>
    </div>
    
    <ul>
        <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fas fa-home"></i> Dashboard</a></li>
        <c:if test="${usuario.admin || usuario.professor}">
            <li><a href="${pageContext.request.contextPath}/bolsista"><i class="fas fa-user-graduate"></i> ${usuario.admin ? 'Usuários' : 'Bolsistas'}</a></li>
        </c:if>
        <c:choose>
            <c:when test="${usuario.admin || usuario.professor}">
                <li><a href="${pageContext.request.contextPath}/laboratorio"><i class="fas fa-flask"></i> Laboratórios</a></li>
            </c:when>
            <c:when test="${usuario.bolsista && usuario.laboratorioId > 0}">
                <li><a href="${pageContext.request.contextPath}/laboratorio/detalhes?id=${usuario.laboratorioId}"><i class="fas fa-flask"></i> Meu Laboratório</a></li>
            </c:when>
        </c:choose>
        <li><a href="${pageContext.request.contextPath}/projeto"><i class="fas fa-project-diagram"></i> Projetos</a></li>
        <li><a href="${pageContext.request.contextPath}/frequencia"><i class="fas fa-calendar-check"></i> Frequência</a></li>
        <c:if test="${usuario.admin || usuario.professor}">
            <li><a href="${pageContext.request.contextPath}/relatorio"><i class="fas fa-chart-bar"></i> Relatórios</a></li>
        </c:if>
        <li><a href="${pageContext.request.contextPath}/perfil"><i class="fas fa-user-cog"></i> Editar Perfil</a></li>
    </ul>
    <a href="${pageContext.request.contextPath}/logout" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Sair</a>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const currentUrl = window.location.pathname;
        const links = document.querySelectorAll(".sidebar ul li a");
        links.forEach(link => {
            const href = link.getAttribute("href");
            const contextPath = "${pageContext.request.contextPath}";
            let checkHref = href;
            if (contextPath && href.startsWith(contextPath)) {
                checkHref = href.substring(contextPath.length);
            }
            if (currentUrl.endsWith(checkHref) || currentUrl.includes(checkHref)) {
                link.parentElement.classList.add("active");
            }
        });
    });
</script>
