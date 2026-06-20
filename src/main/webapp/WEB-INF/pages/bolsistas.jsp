<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar ${usuario.admin ? 'Usuários' : 'Bolsistas'} - SisBolsa</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bolsistas.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="header-actions">
            <h1>Gerenciar ${usuario.admin ? 'Usuários' : 'Bolsistas'}</h1>
            <div class="header-buttons">
                <a href="${pageContext.request.contextPath}/bolsista/exportar" class="btn-new btn-export">
                    <i class="fas fa-file-csv"></i> Exportar CSV
                </a>
                <c:if test="${usuario.admin || usuario.professor}">
                    <a href="${pageContext.request.contextPath}/bolsista/novo" class="btn-new btn-create">
                        <i class="fas fa-plus"></i> Novo ${usuario.admin ? 'Usuário' : 'Bolsista'}
                    </a>
                </c:if>
            </div>
        </div>

        <c:if test="${not empty sucesso or not empty param.sucesso}">
            <div class="success-msg" style="margin: 0 auto 20px auto; max-width: 1200px; padding: 12px 20px; background-color: #d4edda; color: #155724; border-left: 5px solid #28a745; border-radius: 4px; font-size: 0.95rem; display: flex; align-items: center; gap: 10px;">
                <i class="fas fa-check-circle"></i> ${not empty sucesso ? sucesso : param.sucesso}
            </div>
        </c:if>
        
        <c:if test="${not empty erro or not empty param.erro}">
            <div class="error-msg" style="margin: 0 auto 20px auto; max-width: 1200px; padding: 12px 20px; background-color: #f8d7da; color: #721c24; border-left: 5px solid #dc3545; border-radius: 4px; font-size: 0.95rem; display: flex; align-items: center; gap: 10px;">
                <i class="fas fa-exclamation-circle"></i> ${not empty erro ? erro : param.erro}
            </div>
        </c:if>

        <div class="container">
            <div class="search-section">
                <form action="${pageContext.request.contextPath}/bolsista" method="get" class="search-form">
                    <input type="text" name="buscaNome" placeholder="Pesquisar por nome..." class="search-input">
                    <button type="submit" class="search-button"><i class="fas fa-search"></i></button>
                </form>
                <form action="${pageContext.request.contextPath}/bolsista" method="get" class="search-form">
                    <input type="text" name="buscaCurso" placeholder="Pesquisar por curso..." class="search-input">
                    <button type="submit" class="search-button"><i class="fas fa-filter"></i></button>
                </form>
                <a href="${pageContext.request.contextPath}/bolsista" class="reset-button"><i class="fas fa-sync"></i></a>
            </div>

            <c:if test="${usuario.admin}">
                <div class="filter-pills">
                    <span style="font-weight: 600; font-size: 0.85rem; color: var(--text-muted); margin-right: 8px;">Filtrar por tipo:</span>
                    <a href="${pageContext.request.contextPath}/bolsista" class="pill-btn ${empty param.tipo ? 'active' : ''}">Todos</a>
                    <a href="${pageContext.request.contextPath}/bolsista?tipo=BOLSISTA" class="pill-btn ${param.tipo == 'BOLSISTA' ? 'active' : ''}">Bolsistas</a>
                    <a href="${pageContext.request.contextPath}/bolsista?tipo=PROFESSOR" class="pill-btn ${param.tipo == 'PROFESSOR' ? 'active' : ''}">Professores</a>
                    <a href="${pageContext.request.contextPath}/bolsista?tipo=ADMIN" class="pill-btn ${param.tipo == 'ADMIN' ? 'active' : ''}">Administradores</a>
                </div>
            </c:if>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Curso</th>
                            <th>Cargo</th>
                            <th>Laboratório</th>
                            <th>Tipo</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="b" items="${listabolsistas}">
                            <tr>
                                <td><strong>${b.nome}</strong></td>
                                <td>${b.curso}</td>
                                <td>${not empty b.cargo ? b.cargo.descricao : '---'}</td>
                                <td>${not empty b.nomeLaboratorio ? b.nomeLaboratorio : '---'}</td>
                                <td>
                                    <span class="badge ${b.admin ? 'badge-admin' : (b.professor ? 'badge-professor' : 'badge-bolsista')}">
                                        ${b.tipoUsuario}
                                    </span>
                                </td>
                                <td>
                                    <c:if test="${usuario.admin || usuario.professor || b.id == usuario.id}">
                                        <a href="${pageContext.request.contextPath}/bolsista/editar?id=${b.id}&tipo=${b.tipoUsuario}" class="action-link action-link-edit"><i class="fas fa-edit"></i></a>
                                    </c:if>
                                    <c:if test="${(usuario.admin || usuario.professor) && b.id != usuario.id}">
                                        <a href="${pageContext.request.contextPath}/bolsista/excluir?id=${b.id}&tipo=${b.tipoUsuario}" class="action-link action-link-delete" onclick="return confirm('Excluir este ${usuario.admin ? 'usuário' : 'bolsista'}?')"><i class="fas fa-trash"></i></a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <c:if test="${not empty totalPaginas and totalPaginas > 1}">
                <div class="pagination" style="display: flex; justify-content: center; gap: 15px; margin-top: 20px; align-items: center;">
                    <c:if test="${paginaAtual > 1}">
                        <a href="${pageContext.request.contextPath}/bolsista?pagina=${paginaAtual - 1}&tipo=${param.tipo}&buscaNome=${param.buscaNome}&buscaCurso=${param.buscaCurso}" class="btn-pagination" style="padding: 8px 16px; background-color: var(--primary-color); color: white; border-radius: 4px; text-decoration: none; font-size: 0.9rem; font-weight: bold; transition: background 0.2s;"><i class="fas fa-chevron-left"></i> Anterior</a>
                    </c:if>
                    <span style="font-size: 0.9rem; color: #555;">Página <strong>${paginaAtual}</strong> de ${totalPaginas}</span>
                    <c:if test="${paginaAtual < totalPaginas}">
                        <a href="${pageContext.request.contextPath}/bolsista?pagina=${paginaAtual + 1}&tipo=${param.tipo}&buscaNome=${param.buscaNome}&buscaCurso=${param.buscaCurso}" class="btn-pagination" style="padding: 8px 16px; background-color: var(--primary-color); color: white; border-radius: 4px; text-decoration: none; font-size: 0.9rem; font-weight: bold; transition: background 0.2s;">Próxima <i class="fas fa-chevron-right"></i></a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
