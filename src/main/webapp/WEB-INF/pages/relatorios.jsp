<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Relatórios - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css?v=2">
    <link rel="stylesheet" href="css/relatorios.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="header">
            <h1><i class="fas fa-chart-bar"></i> Relatórios Analíticos</h1>
            <p>Visão geral dos dados do sistema</p>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <h3>Total de Bolsistas</h3>
                <div class="value">${totalBolsistas}</div>
                <p><i class="fas fa-user-graduate"></i> Cadastrados</p>
            </div>
            <div class="stat-card stat-card-success">
                <h3>Bolsistas Ativos</h3>
                <div class="value">${bolsistasAtivos}</div>
                <p><i class="fas fa-check-circle"></i> Em atividade</p>
            </div>
            <div class="stat-card stat-card-warning">
                <h3>Total de Laboratórios</h3>
                <div class="value">${totalLabs}</div>
                <p><i class="fas fa-flask"></i> Unidades</p>
            </div>
        </div>

        <div class="report-sections">
            <div class="report-card">
                <h2><i class="fas fa-graduation-cap"></i> Bolsistas por Curso</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Curso</th>
                            <th class="cell-right">Quantidade</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="entry" items="${bolsistasPorCurso}">
                            <tr>
                                <td>${entry.key}</td>
                                <td class="cell-right"><span class="count-badge">${entry.value}</span></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="report-card">
                <h2><i class="fas fa-vial"></i> Laboratórios por Status</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Status</th>
                            <th class="cell-right">Quantidade</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="entry" items="${labsPorStatus}">
                            <tr>
                                <td>${entry.key}</td>
                                <td class="cell-right"><span class="count-badge count-badge-warning">${entry.value}</span></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</body>
</html>
