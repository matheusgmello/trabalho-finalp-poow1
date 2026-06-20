<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Projetos - SisBolsa</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bolsistas.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/laboratorios.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .membros-list-container {
            display: flex;
            flex-wrap: wrap;
            gap: 6px;
            max-width: 300px;
        }
        .badge-membro {
            background-color: #e0f2fe;
            color: #0369a1;
            padding: 3px 8px;
            border-radius: var(--radius-sm);
            font-size: 0.75rem;
            font-weight: 500;
            border: 1px solid #bae6fd;
            display: inline-flex;
            align-items: center;
            gap: 4px;
        }
        .projeto-desc {
            max-width: 350px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: normal;
            font-size: 0.85rem;
            color: var(--text-muted);
            line-height: 1.4;
        }
        .btn-new-proj {
            background-color: var(--primary-color);
            color: white;
        }
        .btn-new-proj:hover {
            background-color: var(--primary-hover);
        }
    </style>
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="header-actions">
            <h1>Gerenciar Projetos</h1>
            <c:if test="${usuario.admin || usuario.professor}">
                <a href="${pageContext.request.contextPath}/projeto/novo" class="btn-new btn-new-proj">
                    <i class="fas fa-plus"></i> Cadastrar Novo Projeto
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
            <div class="search-section">
                <form action="${pageContext.request.contextPath}/projeto" method="get" class="search-form">
                    <input type="text" name="buscaNome" value="${param.buscaNome}" placeholder="Pesquisar projeto por nome ou descrição..." class="search-input">
                    <c:if test="${not empty param.labId}">
                        <input type="hidden" name="labId" value="${param.labId}">
                    </c:if>
                    <button type="submit" class="search-button"><i class="fas fa-search"></i> Pesquisar</button>
                </form>
                
                <form action="${pageContext.request.contextPath}/projeto" method="get" class="search-form">
                    <c:if test="${not empty param.buscaNome}">
                        <input type="hidden" name="buscaNome" value="${param.buscaNome}">
                    </c:if>
                    <select name="labId" class="search-input" onchange="this.form.submit()">
                        <option value="">Todos os Laboratórios</option>
                        <c:forEach var="l" items="${todosLaboratorios}">
                            <option value="${l.id}" ${param.labId == l.id ? 'selected' : ''}>${l.nome}</option>
                        </c:forEach>
                    </select>
                </form>

                <c:if test="${not empty param.buscaNome || not empty param.labId}">
                    <a href="${pageContext.request.contextPath}/projeto" class="reset-button" title="Limpar todos os filtros"><i class="fas fa-sync"></i></a>
                </c:if>
            </div>

            <h2><i class="fas fa-project-diagram"></i> Listagem de Projetos Ativos</h2>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Projeto</th>
                            <th>Descrição</th>
                            <th>Laboratório</th>
                            <th>Coordenador</th>
                            <th>Bolsistas Vinculados</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${listaProjetos}">
                            <!-- Obtém variáveis dinâmicas do requestScope -->
                            <c:set var="membrosKey" value="membros_${p.id}" />
                            <c:set var="membros" value="${requestScope[membrosKey]}" />
                            <c:set var="coordKey" value="coordenador_${p.id}" />
                            <c:set var="coordenador" value="${requestScope[coordKey]}" />
                            <c:set var="podeGerenciarKey" value="podeGerenciar_${p.id}" />
                            <c:set var="podeGerenciar" value="${requestScope[podeGerenciarKey]}" />

                            <tr>
                                <td>
                                    <strong>${p.nome}</strong>
                                </td>
                                <td>
                                    <div class="projeto-desc" title="${p.descricao}">
                                        ${not empty p.descricao ? p.descricao : 'Sem descrição.'}
                                    </div>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${usuario.bolsista && usuario.laboratorioId != p.laboratorioId}">
                                            <span class="badge status-concluido" style="background-color: #f1f5f9; color: var(--text-muted); border: 1px solid var(--border-grid);">
                                                ${p.nomeLaboratorio}
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/laboratorio/detalhes?id=${p.laboratorioId}" class="badge status-concluido" title="Ver Detalhes do Laboratório">
                                                <i class="fas fa-flask"></i> ${p.nomeLaboratorio}
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span class="text-main" style="font-size: 0.85rem; font-weight: 500;">
                                        <i class="fas fa-user-tie" style="color: #64748b; margin-right: 4px;"></i> ${not empty coordenador ? coordenador : 'Sem coordenador'}
                                    </span>
                                </td>
                                <td>
                                    <div class="membros-list-container">
                                        <c:forEach var="m" items="${membros}">
                                            <span class="badge-membro" title="${m.curso} - ${not empty m.cargo ? m.cargo.descricao : 'Bolsista'}">
                                                <i class="fas fa-user-graduate" style="font-size: 0.7rem;"></i> ${m.nome}
                                            </span>
                                        </c:forEach>
                                        <c:if test="${empty membros}">
                                            <span class="empty-text">Nenhum membro vinculado</span>
                                        </c:if>
                                    </div>
                                </td>
                                <td>
                                    <div class="actions-cell">
                                        <c:choose>
                                            <c:when test="${usuario.bolsista}">
                                                <c:choose>
                                                    <c:when test="${usuario.laboratorioId == p.laboratorioId}">
                                                        <a href="${pageContext.request.contextPath}/projeto/detalhes?id=${p.id}" class="action-link action-link-details" style="color: var(--text-muted); background-color: #f1f5f9; border: 1px solid var(--border-grid);" title="Detalhes do Projeto">
                                                            <i class="fas fa-eye"></i>
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted" title="Sem acesso a detalhes de projetos de outras equipes"><i class="fas fa-lock"></i></span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${pageContext.request.contextPath}/projeto/detalhes?id=${p.id}" class="action-link action-link-details" style="color: var(--text-muted); background-color: #f1f5f9; border: 1px solid var(--border-grid); margin-right: 8px;" title="Detalhes do Projeto">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <c:if test="${podeGerenciar}">
                                                    <a href="${pageContext.request.contextPath}/projeto/editar?id=${p.id}" class="action-link action-link-edit" title="Editar Projeto">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/projeto/desativar?id=${p.id}&labId=${p.laboratorioId}&origem=projeto" 
                                                       class="action-link action-link-delete" 
                                                       title="Remover Projeto"
                                                       onclick="return confirm('Deseja realmente desativar este projeto? Todos os bolsistas serão desvinculados.')">
                                                        <i class="fas fa-trash"></i>
                                                    </a>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listaProjetos}">
                            <tr>
                                <td colspan="6" class="empty-state">
                                    Nenhum projeto encontrado.
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
