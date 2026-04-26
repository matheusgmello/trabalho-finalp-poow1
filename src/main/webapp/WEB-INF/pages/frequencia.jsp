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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <h1><i class="fas fa-calendar-check"></i> Registro de Frequência</h1>

        <c:if test="${not empty erro}">
            <div class="error-msg" style="background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; margin-bottom: 20px;">
                <i class="fas fa-exclamation-circle"></i> ${erro}
            </div>
        </c:if>

        <!-- Formulário de Registro (Apenas para Bolsistas comum registrarem o seu) -->
        <div class="container" style="max-width: 600px; margin-left: 0;">
            <h2>Novo Registro</h2>
            <form action="frequencia" method="post" style="display: grid; gap: 15px;">
                <div style="display: flex; gap: 15px;">
                    <div style="flex: 1;">
                        <label>Data</label>
                        <input type="date" name="data" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    <div style="flex: 1;">
                        <label>Horas</label>
                        <input type="number" step="0.5" name="horas" required placeholder="Ex: 4.5" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                </div>
                <div>
                    <label>Descrição das Atividades</label>
                    <textarea name="descricao" rows="3" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;"></textarea>
                </div>
                <button type="submit" style="background-color: var(--success-color); color: white; border: none; padding: 12px; border-radius: 5px; font-weight: bold; cursor: pointer;">
                    <i class="fas fa-save"></i> Registrar Horas
                </button>
            </form>
        </div>

        <!-- Listagem de Registros -->
        <div class="container">
            <h2><i class="fas fa-history"></i> Histórico de Atividades</h2>
            <div class="table-container" style="overflow-x: auto;">
                <table style="width: 100%; border-collapse: collapse;">
                    <thead>
                        <tr style="background-color: var(--primary-color); color: white; text-align: left;">
                            <th style="padding: 12px;">Bolsista</th>
                            <th style="padding: 12px;">Data</th>
                            <th style="padding: 12px;">Horas</th>
                            <th style="padding: 12px;">Atividade</th>
                            <th style="padding: 12px;">Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="f" items="${listaFrequencia}">
                            <tr style="border-bottom: 1px solid #eee;">
                                <td style="padding: 12px;">${f.nomeBolsista}</td>
                                <td style="padding: 12px;">${f.data}</td>
                                <td style="padding: 12px;"><strong>${f.horasTrabalhadas}h</strong></td>
                                <td style="padding: 12px;">${f.descricao}</td>
                                <td style="padding: 12px;">
                                    <c:if test="${usuario.admin}">
                                        <a href="frequencia?action=excluir&id=${f.id}" style="color: #e74c3c;" onclick="return confirm('Excluir registro?')"><i class="fas fa-trash"></i></a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listaFrequencia}">
                            <tr><td colspan="5" style="text-align: center; padding: 20px; color: #999;">Nenhum registro encontrado.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
