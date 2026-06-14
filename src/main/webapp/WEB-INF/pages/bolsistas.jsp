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
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/bolsistas.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="header-actions">
            <h1>Gerenciar ${usuario.admin ? 'Usuários' : 'Bolsistas'}</h1>
            <div class="header-buttons">
                <a href="bolsista?action=exportar" class="btn-new btn-export">
                    <i class="fas fa-file-csv"></i> Exportar CSV
                </a>
                <c:if test="${usuario.admin || usuario.professor}">
                    <a href="bolsista?action=novo" class="btn-new btn-create">
                        <i class="fas fa-plus"></i> Novo ${usuario.admin ? 'Usuário' : 'Bolsista'}
                    </a>
                </c:if>
            </div>
        </div>

        <div class="container">
            <div class="search-section">
                <form action="bolsista" method="get" class="search-form">
                    <input type="text" name="buscaNome" placeholder="Pesquisar por nome..." class="search-input">
                    <button type="submit" class="search-button"><i class="fas fa-search"></i></button>
                </form>
                <form action="bolsista" method="get" class="search-form">
                    <input type="text" name="buscaCurso" placeholder="Pesquisar por curso..." class="search-input">
                    <button type="submit" class="search-button"><i class="fas fa-filter"></i></button>
                </form>
                <a href="bolsista" class="reset-button"><i class="fas fa-sync"></i></a>
            </div>

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
                                        <a href="bolsista?action=editar&id=${b.id}&tipo=${b.tipoUsuario}" class="action-link action-link-edit"><i class="fas fa-edit"></i></a>
                                    </c:if>
                                    <c:if test="${(usuario.admin || usuario.professor) && b.id != usuario.id}">
                                        <a href="bolsista?action=excluir&id=${b.id}&tipo=${b.tipoUsuario}" class="action-link action-link-delete" onclick="return confirm('Excluir este ${usuario.admin ? 'usuário' : 'bolsista'}?')"><i class="fas fa-trash"></i></a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
