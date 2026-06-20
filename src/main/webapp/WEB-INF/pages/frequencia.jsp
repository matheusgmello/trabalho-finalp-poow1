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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/frequencia.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <h1><i class="fas fa-calendar-check"></i> Registro de Frequência</h1>

        <c:if test="${usuario.bolsista}">
            <div class="stats-grid" style="display: flex; gap: 20px; margin-bottom: 20px; flex-wrap: wrap;">
                <div class="stat-card" style="flex: 1; min-width: 200px; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); border-left: 4px solid var(--primary-color);">
                    <h3 style="font-size: 0.9rem; color: #666; margin-bottom: 5px;">Horas Trabalhadas no Mês</h3>
                    <div class="value" style="font-size: 2rem; font-weight: bold; color: var(--primary-color);">${totalHorasMesCorrente} hrs</div>
                    <p style="font-size: 0.8rem; color: #999; margin-top: 5px;"><i class="fas fa-calendar-alt"></i> Mês Corrente</p>
                </div>
                <div class="stat-card" style="flex: 1; min-width: 200px; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); border-left: 4px solid #2ecc71;">
                    <h3 style="font-size: 0.9rem; color: #666; margin-bottom: 5px;">Total Acumulado de Horas</h3>
                    <div class="value" style="font-size: 2rem; font-weight: bold; color: #2ecc71;">${totalHorasAcumulado} hrs</div>
                    <p style="font-size: 0.8rem; color: #999; margin-top: 5px;"><i class="fas fa-history"></i> Histórico Geral</p>
                </div>
            </div>
        </c:if>

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

        <div class="container form-container">
            <h2>
                <c:choose>
                    <c:when test="${not empty frequenciaEdicao}">
                        Editar Registro — <span class="nome-bolsista-edicao">${frequenciaEdicao.nomeBolsista}</span>
                    </c:when>
                    <c:otherwise>Novo Registro</c:otherwise>
                </c:choose>
            </h2>
            <form action="${pageContext.request.contextPath}/frequencia" method="post" class="frequency-form">
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
                        <a href="${pageContext.request.contextPath}/frequencia" class="cancel-button"><i class="fas fa-times"></i> Cancelar</a>
                    </c:if>
                </div>
            </form>
        </div>

        <div class="container">
            <div class="history-header" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 15px;">
                <h2 style="margin: 0;"><i class="fas fa-history"></i> Histórico de Atividades</h2>
                
                <div class="history-actions" style="display: flex; gap: 15px; align-items: center; flex-wrap: wrap;">
                    <c:if test="${usuario.admin or usuario.professor}">
                        <form action="${pageContext.request.contextPath}/frequencia" method="get" class="filter-form" style="display: flex; gap: 10px; align-items: center;">
                            <select name="bolsistaId" style="padding: 8px 12px; border: 1px solid #ccc; border-radius: 4px; font-size: 0.9rem;">
                                <option value="">Todos os Bolsistas</option>
                                <c:forEach var="b" items="${listaBolsistas}">
                                    <option value="${b.id}" ${filtroBolsistaId == b.id ? 'selected' : ''}>${b.nome}</option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="submit-button" style="padding: 8px 15px; border-radius: 4px; font-size: 0.9rem; margin: 0;"><i class="fas fa-filter"></i> Filtrar</button>
                        </form>
                    </c:if>
                    
                    <a href="${pageContext.request.contextPath}/frequencia/exportar?bolsistaId=${filtroBolsistaId}" class="submit-button" style="padding: 8px 15px; border-radius: 4px; font-size: 0.9rem; margin: 0; background-color: #27ae60; text-decoration: none; display: inline-flex; align-items: center; gap: 5px; color: white;">
                        <i class="fas fa-file-csv"></i> Exportar CSV
                    </a>
                </div>
            </div>

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
                                        <a href="${pageContext.request.contextPath}/frequencia/editar?id=${f.id}&bolsistaId=${filtroBolsistaId}" class="action-edit"><i class="fas fa-pencil-alt"></i></a>
                                    </c:if>
                                    <c:if test="${usuario.admin or usuario.professor}">
                                        <a href="${pageContext.request.contextPath}/frequencia/excluir?id=${f.id}" class="action-delete" onclick="return confirm('Excluir registro?')"><i class="fas fa-trash"></i></a>
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
            
            <c:if test="${usuario.admin and not empty totalPaginas and totalPaginas > 1}">
                <div class="pagination" style="display: flex; justify-content: center; gap: 15px; margin-top: 20px; align-items: center;">
                    <c:if test="${paginaAtual > 1}">
                        <a href="${pageContext.request.contextPath}/frequencia?pagina=${paginaAtual - 1}&bolsistaId=${filtroBolsistaId}" class="btn-pagination" style="padding: 8px 16px; background-color: var(--primary-color); color: white; border-radius: 4px; text-decoration: none; font-size: 0.9rem; font-weight: bold; transition: background 0.2s;"><i class="fas fa-chevron-left"></i> Anterior</a>
                    </c:if>
                    <span style="font-size: 0.9rem; color: #555;">Página <strong>${paginaAtual}</strong> de ${totalPaginas}</span>
                    <c:if test="${paginaAtual < totalPaginas}">
                        <a href="${pageContext.request.contextPath}/frequencia?pagina=${paginaAtual + 1}&bolsistaId=${filtroBolsistaId}" class="btn-pagination" style="padding: 8px 16px; background-color: var(--primary-color); color: white; border-radius: 4px; text-decoration: none; font-size: 0.9rem; font-weight: bold; transition: background 0.2s;">Próxima <i class="fas fa-chevron-right"></i></a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
