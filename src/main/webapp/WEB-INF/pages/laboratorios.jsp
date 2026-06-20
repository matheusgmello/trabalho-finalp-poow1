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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/laboratorios.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="header-actions">
            <h1>Gerenciar Laboratórios</h1>
            <c:if test="${usuario.admin}">
                <a href="${pageContext.request.contextPath}/laboratorio/novo" class="btn-new">
                    <i class="fas fa-plus"></i> Cadastrar Novo Laboratório
                </a>
            </c:if>
        </div>

        <c:if test="${not empty sucesso or not empty param.sucesso}">
            <div class="success-msg" style="margin-bottom: 20px; padding: 12px 20px; background-color: #d4edda; color: #155724; border-left: 5px solid #28a745; border-radius: 4px; font-size: 0.95rem; display: flex; align-items: center; gap: 10px;">
                <i class="fas fa-check-circle"></i> ${not empty sucesso ? sucesso : param.sucesso}
            </div>
        </c:if>
        
        <c:if test="${not empty erro or not empty param.erro}">
            <div class="error-msg" style="margin-bottom: 20px; padding: 12px 20px; background-color: #f8d7da; color: #721c24; border-left: 5px solid #dc3545; border-radius: 4px; font-size: 0.95rem; display: flex; align-items: center; gap: 10px;">
                <i class="fas fa-exclamation-circle"></i> ${not empty erro ? erro : param.erro}
            </div>
        </c:if>

        <div class="container">
            <h2><i class="fas fa-list"></i> Listagem de Laboratórios</h2>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <c:choose>
                                <c:when test="${usuario.bolsista}">
                                    <th>Coordenador</th>
                                    <th>Projetos</th>
                                </c:when>
                                <c:otherwise>
                                    <th>Área de Pesquisa</th>
                                    <th>Coordenador</th>
                                </c:otherwise>
                            </c:choose>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="lab" items="${listaLaboratorios}">
                            <tr>
                                <td><strong>${lab.nome}</strong></td>
                                <c:choose>
                                    <c:when test="${usuario.bolsista}">
                                        <td>${lab.coordenador}</td>
                                        <td>
                                            <c:forEach var="proj" items="${lab.projetos}" varStatus="loop">
                                                <span class="badge-projeto-tag">${proj.nome}</span>
                                            </c:forEach>
                                            <c:if test="${empty lab.projetos}">
                                                <span class="empty-text">Nenhum projeto</span>
                                            </c:if>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>${lab.areaPesquisa}</td>
                                        <td>${lab.coordenador}</td>
                                    </c:otherwise>
                                </c:choose>
                                <td>
                                    <span class="badge status-${lab.status.toLowerCase().replace(' ', '-')}">
                                        ${lab.status}
                                    </span>
                                </td>
                                <td class="actions-cell">
                                    <c:choose>
                                        <c:when test="${usuario.bolsista}">
                                            <c:choose>
                                                <c:when test="${lab.id == usuario.laboratorioId}">
                                                    <a href="${pageContext.request.contextPath}/laboratorio/detalhes?id=${lab.id}" class="btn-icon btn-details" title="Detalhes"><i class="fas fa-eye"></i></a>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted" title="Sem acesso a detalhes de laboratórios de outras equipes"><i class="fas fa-lock"></i></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/laboratorio/detalhes?id=${lab.id}" class="btn-icon btn-details" title="Detalhes"><i class="fas fa-eye"></i></a>
                                            <c:if test="${usuario.admin}">
                                                <a href="${pageContext.request.contextPath}/laboratorio/editar?id=${lab.id}" class="btn-icon btn-edit" title="Editar"><i class="fas fa-edit"></i></a>
                                                <a href="${pageContext.request.contextPath}/laboratorio/excluir?id=${lab.id}" class="btn-icon btn-delete" title="Excluir" onclick="return confirm('Tem certeza que deseja excluir este laboratório?')"><i class="fas fa-trash"></i></a>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
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
