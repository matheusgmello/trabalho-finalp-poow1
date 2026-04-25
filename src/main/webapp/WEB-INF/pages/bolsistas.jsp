<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Bolsistas - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="header-actions" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px;">
            <h1>Gerenciar Bolsistas</h1>
            <div style="display: flex; gap: 10px;">
                <a href="bolsista?action=exportar" class="btn-new" style="background-color: var(--accent-color); text-decoration: none; padding: 12px 20px; border-radius: 5px; color: white; font-weight: bold;">
                    <i class="fas fa-file-csv"></i> Exportar CSV
                </a>
                <c:if test="${usuario.admin}">
                    <a href="bolsista?action=novo" class="btn-new" style="background-color: var(--success-color); text-decoration: none; padding: 12px 20px; border-radius: 5px; color: white; font-weight: bold;">
                        <i class="fas fa-plus"></i> Novo Bolsista
                    </a>
                </c:if>
            </div>
        </div>

        <div class="container">
            <!-- Filtros -->
            <div class="search-section" style="display: flex; gap: 15px; margin-bottom: 25px; background: #f9f9f9; padding: 20px; border-radius: 10px; border: 1px solid #eee;">
                <form action="bolsista" method="get" style="display: flex; align-items: center; gap: 10px; flex: 1;">
                    <input type="text" name="buscaNome" placeholder="Pesquisar por nome..." style="padding: 10px; border: 1px solid #ddd; border-radius: 5px; flex: 1;">
                    <button type="submit" style="background-color: var(--accent-color); color: white; border: none; padding: 10px 15px; border-radius: 5px; cursor: pointer;"><i class="fas fa-search"></i></button>
                </form>
                <form action="bolsista" method="get" style="display: flex; align-items: center; gap: 10px; flex: 1;">
                    <input type="text" name="buscaCurso" placeholder="Pesquisar por curso..." style="padding: 10px; border: 1px solid #ddd; border-radius: 5px; flex: 1;">
                    <button type="submit" style="background-color: var(--accent-color); color: white; border: none; padding: 10px 15px; border-radius: 5px; cursor: pointer;"><i class="fas fa-filter"></i></button>
                </form>
                <a href="bolsista" style="background: #666; color: white; text-decoration: none; padding: 10px 15px; border-radius: 5px;"><i class="fas fa-sync"></i></a>
            </div>

            <div class="table-container" style="overflow-x: auto;">
                <table style="width: 100%; border-collapse: collapse;">
                    <thead>
                        <tr style="background-color: var(--primary-color); color: white; text-align: left;">
                            <th style="padding: 15px;">Nome</th>
                            <th style="padding: 15px;">Curso</th>
                            <th style="padding: 15px;">Laboratório</th>
                            <th style="padding: 15px;">Tipo</th>
                            <th style="padding: 15px;">Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="b" items="${listabolsistas}">
                            <tr style="border-bottom: 1px solid #eee;">
                                <td style="padding: 12px 15px;"><strong>${b.nome}</strong></td>
                                <td style="padding: 12px 15px;">${b.curso}</td>
                                <td style="padding: 12px 15px;">${not empty b.nomeLaboratorio ? b.nomeLaboratorio : '---'}</td>
                                <td style="padding: 12px 15px;">
                                    <span class="badge" style="padding: 5px 10px; border-radius: 20px; font-size: 11px; font-weight: bold; background: ${b.admin ? '#e8f4fd' : '#f0f0f0'}; color: ${b.admin ? '#004085' : '#666'};">
                                        ${b.tipoUsuario}
                                    </span>
                                </td>
                                <td style="padding: 12px 15px;">
                                    <c:if test="${usuario.admin || b.id == usuario.id}">
                                        <a href="bolsista?action=editar&id=${b.id}" style="color: var(--accent-color); text-decoration: none; margin-right: 10px;"><i class="fas fa-edit"></i></a>
                                    </c:if>
                                    <c:if test="${usuario.admin && b.id != usuario.id}">
                                        <a href="bolsista?action=excluir&id=${b.id}" style="color: #e74c3c; text-decoration: none;" onclick="return confirm('Excluir este bolsista?')"><i class="fas fa-trash"></i></a>
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
