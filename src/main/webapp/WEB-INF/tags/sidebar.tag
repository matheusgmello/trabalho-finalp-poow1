<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="sidebar">
    <h2>SisBolsa</h2>
    <ul>
        <li><a href="dashboard"><i class="fas fa-home"></i> Dashboard</a></li>
        <c:if test="${usuario.admin || usuario.professor}">
            <li><a href="bolsista"><i class="fas fa-user-graduate"></i> ${usuario.admin ? 'Usuários' : 'Bolsistas'}</a></li>
        </c:if>
        <li><a href="laboratorio"><i class="fas fa-flask"></i> Laboratórios</a></li>
        <li><a href="frequencia"><i class="fas fa-calendar-check"></i> Frequência</a></li>
        <li><a href="relatorio"><i class="fas fa-chart-bar"></i> Relatórios</a></li>
        <li><a href="perfil"><i class="fas fa-user-cog"></i> Editar Perfil</a></li>
    </ul>
    <a href="logout" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Sair</a>
</div>
