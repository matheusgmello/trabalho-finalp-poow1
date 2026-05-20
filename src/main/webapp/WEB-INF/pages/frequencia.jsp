<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Frequência - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/frequencia.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <h1><i class="fas fa-calendar-check"></i> Registro de Frequência</h1>

        <c:if test="${not empty erro}">
            <div class="error-msg">
                <i class="fas fa-exclamation-circle"></i> ${erro}
            </div>
        </c:if>

        <div class="container form-container">
            <h2>Novo Registro</h2>
            <form action="frequencia" method="post" class="frequency-form">
                <div class="form-row">
                    <div class="form-col">
                        <label>Data</label>
                        <input type="date" name="data" required>
                    </div>
                    <div class="form-col">
                        <label>Horas</label>
                        <input type="number" step="0.5" name="horas" required placeholder="Ex: 4.5">
                    </div>
                </div>
                <div>
                    <label>Descrição das Atividades</label>
                    <textarea name="descricao" rows="3"></textarea>
                </div>
                <button type="submit" class="submit-button">
                    <i class="fas fa-save"></i> Registrar Horas
                </button>
            </form>
        </div>

        <div class="container">
            <h2><i class="fas fa-history"></i> Histórico de Atividades</h2>
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Bolsista</th>
                            <th>Data</th>
                            <th>Horas</th>
                            <th>Atividade</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="f" items="${listaFrequencia}">
                            <tr>
                                <td>${f.nomeBolsista}</td>
                                <td>${f.data}</td>
                                <td><strong>${f.horasTrabalhadas}h</strong></td>
                                <td>${f.descricao}</td>
                                <td>
                                    <c:if test="${usuario.admin}">
                                        <a href="frequencia?action=excluir&id=${f.id}" class="action-delete" onclick="return confirm('Excluir registro?')"><i class="fas fa-trash"></i></a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listaFrequencia}">
                            <tr><td colspan="5" class="empty-state">Nenhum registro encontrado.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
