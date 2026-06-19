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
    <link rel="stylesheet" href="css/style.css?v=2">
    <link rel="stylesheet" href="css/bolsistas.css?v=2">
    <link rel="stylesheet" href="css/laboratorios.css?v=2">
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
                <a href="projeto?action=novo" class="btn-new btn-new-proj">
                    <i class="fas fa-plus"></i> Cadastrar Novo Projeto
                </a>
            </c:if>
        </div>

        <c:if test="${not empty param.erro}">
            <div class="error-msg">
                <i class="fas fa-exclamation-circle"></i> ${param.erro}
            </div>
        </c:if>

        <div class="container">
            <div class="search-section">
                <form action="projeto" method="get" class="search-form">
                    <input type="text" name="buscaNome" value="${param.buscaNome}" placeholder="Pesquisar projeto por nome ou descrição..." class="search-input">
                    <button type="submit" class="search-button"><i class="fas fa-search"></i> Filtrar</button>
                </form>
                <c:if test="${not empty param.buscaNome}">
                    <a href="projeto" class="reset-button"><i class="fas fa-sync"></i> Limpar Filtro</a>
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
                            <c:if test="${usuario.admin || usuario.professor}">
                                <th>Ações</th>
                            </c:if>
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
                                            <a href="laboratorio?action=detalhes&id=${p.laboratorioId}" class="badge status-concluido" title="Ver Detalhes do Laboratório">
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
                                <c:if test="${usuario.admin || usuario.professor}">
                                    <td>
                                        <div class="actions-cell">
                                            <c:choose>
                                                <c:when test="${podeGerenciar}">
                                                    <a href="projeto?action=editar&id=${p.id}" class="action-link action-link-edit" title="Editar Projeto">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <a href="projeto/desativar?id=${p.id}&labId=${p.laboratorioId}&origem=projeto" 
                                                       class="action-link action-link-delete" 
                                                       title="Remover Projeto"
                                                       onclick="return confirm('Deseja realmente desativar este projeto? Todos os bolsistas serão desvinculados.')">
                                                        <i class="fas fa-trash"></i>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted" title="Sem permissão para gerenciar projetos deste laboratório"><i class="fas fa-lock"></i></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listaProjetos}">
                            <tr>
                                <td colspan="${(usuario.admin || usuario.professor) ? 6 : 5}" class="empty-state">
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
