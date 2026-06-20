<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Relatórios - SisBolsa</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/relatorios.css?v=2">
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

        <div class="report-sections" style="margin-top: 25px;">
            <div class="report-card">
                <h2><i class="fas fa-clock"></i> Horas Registradas no Mês Corrente</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Bolsista</th>
                            <th class="cell-right">Total de Horas</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${horasBolsistas}">
                            <tr>
                                <td>${item.nome}</td>
                                <td class="cell-right"><span class="count-badge" style="background-color: var(--primary-color);">${item.totalHoras} hrs</span></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty horasBolsistas}">
                            <tr>
                                <td colspan="2" class="empty-state" style="text-align: center; color: #666; padding: 15px;">Nenhuma hora lançada no mês corrente.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <div class="report-card">
                <h2><i class="fas fa-project-diagram"></i> Projetos Ativos por Laboratório</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Laboratório</th>
                            <th class="cell-right">Projetos Ativos</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${projetosPorLab}">
                            <tr>
                                <td>${item.nome}</td>
                                <td class="cell-right"><span class="count-badge count-badge-warning">${item.totalProjetos}</span></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty projetosPorLab}">
                            <tr>
                                <td colspan="2" class="empty-state" style="text-align: center; color: #666; padding: 15px;">Nenhum projeto ativo cadastrado.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="report-sections" style="margin-top: 25px;">
            <div class="report-card">
                <h2><i class="fas fa-briefcase"></i> Bolsistas por Cargo</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Cargo</th>
                            <th class="cell-right">Total de Bolsistas</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${bolsistasPorCargo}">
                            <tr>
                                <td>${item.cargo}</td>
                                <td class="cell-right"><span class="count-badge" style="background-color: #9b59b6; color: white;">${item.totalBolsistas}</span></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty bolsistasPorCargo}">
                            <tr>
                                <td colspan="2" class="empty-state" style="text-align: center; color: #666; padding: 15px;">Nenhum bolsista associado a cargo.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <div class="report-card">
                <h2><i class="fas fa-exclamation-triangle" style="color: #e74c3c;"></i> Alerta de Vagas (Ocupação >= 85%)</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Laboratório</th>
                            <th>Ocupação</th>
                            <th class="cell-right">Bolsistas / Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${labsLimite}">
                            <tr>
                                <td><strong>${item.nome}</strong></td>
                                <td>
                                    <span class="badge badge-danger" style="background-color: #e74c3c; color: white; padding: 3px 8px; border-radius: 4px; font-size: 0.8rem;">
                                        <fmt:formatNumber value="${item.percentualOcupacao}" maxFractionDigits="1"/>%
                                    </span>
                                </td>
                                <td class="cell-right"><strong>${item.totalBolsistas}</strong> / ${item.capacidade}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty labsLimite}">
                            <tr>
                                <td colspan="3" class="empty-state" style="text-align: center; color: #2ecc71; padding: 15px; font-weight: bold;">
                                    <i class="fas fa-check-circle"></i> Todos os laboratórios estão operando com folga (&lt; 85% de ocupação).
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
