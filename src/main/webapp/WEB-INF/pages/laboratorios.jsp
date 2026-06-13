<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Laboratórios - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/laboratorios.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="header-actions">
            <h1>Gerenciar Laboratórios</h1>
            <c:if test="${usuario.admin}">
                <a href="laboratorio?action=novo" class="btn-new">
                    <i class="fas fa-plus"></i> Cadastrar Novo Laboratório
                </a>
            </c:if>
        </div>

        <c:if test="${not empty erro}">
            <div class="error-msg">
                <i class="fas fa-exclamation-circle"></i> ${erro}
            </div>
        </c:if>

        <div class="container">
            <h2><i class="fas fa-list"></i> Listagem de Laboratórios</h2>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Área de Pesquisa</th>
                            <th>Coordenador</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="lab" items="${listaLaboratorios}">
                            <tr>
                                <td><strong>${lab.nome}</strong></td>
                                <td>${lab.areaPesquisa}</td>
                                <td>${lab.coordenador}</td>
                                <td>
                                    <span class="badge status-${lab.status.toLowerCase().replace(' ', '-')}">
                                        ${lab.status}
                                    </span>
                                </td>
                                <td class="actions-cell">
                                    <a href="laboratorio?action=detalhes&id=${lab.id}" class="btn-icon btn-details" title="Detalhes"><i class="fas fa-eye"></i></a>
                                    <c:if test="${usuario.admin}">
                                        <a href="laboratorio?action=editar&id=${lab.id}" class="btn-icon btn-edit" title="Editar"><i class="fas fa-edit"></i></a>
                                        <a href="laboratorio?action=excluir&id=${lab.id}" class="btn-icon btn-delete" title="Excluir" onclick="return confirm('Tem certeza que deseja excluir este laboratório?')"><i class="fas fa-trash"></i></a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listaLaboratorios}">
                            <tr>
                                <td colspan="5" class="empty-state">
                                    Nenhum laboratório encontrado.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</body>
</html>
