<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalhes do Laboratório - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/detalhes-laboratorio.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="container">
            <div class="lab-header">
                <h1><i class="fas fa-flask"></i> ${laboratorio.nome}</h1>
                <a href="laboratorio" class="btn-back"><i class="fas fa-arrow-left"></i> Voltar</a>
            </div>

            <div class="lab-info-grid">
                <div class="info-item">
                    <span class="info-label">Área de Pesquisa</span>
                    <span class="info-value">${laboratorio.areaPesquisa}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Status</span>
                    <span class="info-value"><span class="badge status-${laboratorio.status.toLowerCase()}">${laboratorio.status}</span></span>
                </div>
                <div class="info-item info-item-full">
                    <span class="info-label">Título do Projeto</span>
                    <span class="info-value">${laboratorio.tituloProjeto}</span>
                </div>
            </div>

            <h2><i class="fas fa-users"></i> Bolsistas Alocados</h2>
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Curso</th>
                            <th>E-mail</th>
                            <th>Matrícula</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="b" items="${bolsistas}">
                            <tr>
                                <td><strong>${b.nome}</strong></td>
                                <td>${b.curso}</td>
                                <td>${b.email}</td>
                                <td>${b.matricula}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty bolsistas}">
                            <tr>
                                <td colspan="4" class="empty-state">
                                    Nenhum bolsista vinculado a este laboratório.
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
