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
            <h2>
                <c:choose>
                    <c:when test="${not empty frequenciaEdicao}">
                        Editar Registro — <span class="nome-bolsista-edicao">${frequenciaEdicao.nomeBolsista}</span>
                    </c:when>
                    <c:otherwise>Novo Registro</c:otherwise>
                </c:choose>
            </h2>
            <form action="frequencia" method="post" class="frequency-form">
                <input type="hidden" name="id" value="${frequenciaEdicao.id}">

                <%-- campo de bolsista: select para admin, nome fixo para bolsista comum --%>
                <div class="form-group-full">
                    <label>Bolsista</label>
                    <c:choose>
                        <c:when test="${(usuario.admin or usuario.professor) and empty frequenciaEdicao}">
                            <select name="bolsistaId" required>
                                <option value="">Selecione o bolsista...</option>
                                <c:forEach var="b" items="${listaBolsistas}">
                                    <option value="${b.id}">${b.nome}</option>
                                </c:forEach>
                            </select>
                        </c:when>
                        <c:when test="${(usuario.admin or usuario.professor) and not empty frequenciaEdicao}">
                            <%-- em edicao o bolsista nao muda, apenas exibe o nome --%>
                            <input type="text" value="${frequenciaEdicao.nomeBolsista}" disabled>
                        </c:when>
                        <c:otherwise>
                            <%-- bolsista comum ve apenas o proprio nome, sem possibilidade de troca --%>
                            <input type="text" value="${usuario.nome}" disabled>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="form-row">
                    <div class="form-col">
                        <label>Data</label>
                        <input type="date" name="data" required value="${frequenciaEdicao.data}">
                    </div>
                    <div class="form-col">
                        <label>Horas</label>
                        <input type="number" step="0.5" name="horas" required placeholder="Ex: 4.5" value="${frequenciaEdicao.horasTrabalhadas}">
                    </div>
                </div>
                <div>
                    <label>Descrição das Atividades</label>
                    <textarea name="descricao" rows="3">${frequenciaEdicao.descricao}</textarea>
                </div>
                <div class="form-actions">
                    <button type="submit" class="submit-button">
                        <c:choose>
                            <c:when test="${not empty frequenciaEdicao}"><i class="fas fa-save"></i> Salvar Alterações</c:when>
                            <c:otherwise><i class="fas fa-save"></i> Registrar Horas</c:otherwise>
                        </c:choose>
                    </button>
                    <c:if test="${not empty frequenciaEdicao}">
                        <a href="frequencia" class="cancel-button"><i class="fas fa-times"></i> Cancelar</a>
                    </c:if>
                </div>
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
                                    <c:if test="${usuario.admin or usuario.professor or f.bolsistaId == usuario.id}">
                                        <a href="frequencia?action=editar&id=${f.id}" class="action-edit"><i class="fas fa-pencil-alt"></i></a>
                                    </c:if>
                                    <c:if test="${usuario.admin or usuario.professor}">
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
