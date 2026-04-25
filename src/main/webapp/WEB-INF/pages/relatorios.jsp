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
    <link rel="stylesheet" href="css/style.css">
    <style>
        .header {
            margin-bottom: 30px;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }

        .stat-card {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
            text-align: center;
            border-top: 5px solid var(--accent-color);
        }

        .stat-card h3 {
            margin: 0;
            color: #777;
            font-size: 0.9rem;
            text-transform: uppercase;
        }

        .stat-card .value {
            font-size: 2.5rem;
            font-weight: bold;
            color: var(--primary-color);
            margin: 10px 0;
        }

        .report-sections {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
        }

        .report-card {
            background: white;
            padding: 25px;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05);
        }

        .report-card h2 {
            margin-top: 0;
            color: var(--primary-color);
            border-bottom: 2px solid #eee;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th {
            text-align: left;
            padding: 10px;
            background: #f9f9f9;
            color: #666;
        }

        td {
            padding: 12px 10px;
            border-bottom: 1px solid #eee;
        }

        .count-badge {
            background: var(--accent-color);
            color: white;
            padding: 2px 8px;
            border-radius: 10px;
            font-weight: bold;
            font-size: 0.9rem;
        }
    </style>
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
            <div class="stat-card" style="border-top-color: var(--success-color);">
                <h3>Bolsistas Ativos</h3>
                <div class="value">${bolsistasAtivos}</div>
                <p><i class="fas fa-check-circle"></i> Em atividade</p>
            </div>
            <div class="stat-card" style="border-top-color: var(--warning-color);">
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
                            <th style="text-align: right;">Quantidade</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="entry" items="${bolsistasPorCurso}">
                            <tr>
                                <td>${entry.key}</td>
                                <td style="text-align: right;"><span class="count-badge">${entry.value}</span></td>
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
                            <th style="text-align: right;">Quantidade</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="entry" items="${labsPorStatus}">
                            <tr>
                                <td>${entry.key}</td>
                                <td style="text-align: right;"><span class="count-badge" style="background: var(--warning-color);">${entry.value}</span></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</body>
</html>
