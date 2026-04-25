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
    <style>
        .lab-header {
            border-bottom: 2px solid var(--accent-color);
            padding-bottom: 15px;
            margin-bottom: 25px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .lab-info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 30px;
        }

        .info-item {
            display: flex;
            flex-direction: column;
        }

        .info-label {
            font-weight: bold;
            color: var(--secondary-color);
            font-size: 0.9rem;
            text-transform: uppercase;
        }

        .info-value {
            font-size: 1.1rem;
            color: var(--primary-color);
        }

        .table-container { overflow-x: auto; }
        table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        th { background-color: var(--primary-color); color: white; text-align: left; padding: 12px; }
        td { padding: 12px; border-bottom: 1px solid #eee; }

        .btn-back {
            background-color: var(--secondary-color);
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
        }

        .badge {
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }
        .status-ativo { background: #d4edda; color: #155724; }
    </style>
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
                <div class="info-item" style="grid-column: span 2;">
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
                                <td colspan="4" style="text-align: center; padding: 20px; color: #999;">
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
