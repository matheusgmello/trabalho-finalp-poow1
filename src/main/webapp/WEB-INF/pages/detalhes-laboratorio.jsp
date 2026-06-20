<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalhes do Laboratório - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css?v=2">
    <link rel="stylesheet" href="css/detalhes-laboratorio.css?v=2">
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

            <!-- Abas de Navegação -->
            <div class="tabs-container">
                <button class="tab-btn active" data-tab="visao-geral">
                    <i class="fas fa-chart-pie"></i> Visão Geral
                </button>
                <button class="tab-btn" data-tab="projetos">
                    <i class="fas fa-project-diagram"></i> Projetos (${laboratorio.projetos.size()})
                </button>
                <button class="tab-btn" data-tab="equipe">
                    <i class="fas fa-users"></i> Equipe (${bolsistas.size()})
                </button>
            </div>

            <!-- Aba 1: Visão Geral -->
            <div id="visao-geral" class="tab-content active">
                <div class="lab-info-grid">
                    <div class="info-item">
                        <span class="info-label">Área de Pesquisa</span>
                        <span class="info-value">${laboratorio.areaPesquisa}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Status</span>
                        <span class="info-value">
                            <span class="badge status-${laboratorio.status.toLowerCase().replace(' ', '-')}">
                                ${laboratorio.status}
                            </span>
                        </span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Professor Coordenador</span>
                        <span class="info-value">${not empty laboratorio.coordenador ? laboratorio.coordenador : '---'}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Capacidade Máxima</span>
                        <span class="info-value">${laboratorio.capacidade} bolsistas</span>
                    </div>
                </div>

                <!-- Barra de Ocupação/Capacidade -->
                <c:set var="totalBolsistas" value="${bolsistas.size()}" />
                <c:set var="capacidade" value="${laboratorio.capacidade > 0 ? laboratorio.capacidade : 1}" />
                <c:set var="pct" value="${(totalBolsistas * 100.0) / capacidade}" />

                <c:set var="progressClass" value="success" />
                <c:if test="${pct >= 50 && pct < 85}">
                    <c:set var="progressClass" value="warning" />
                </c:if>
                <c:if test="${pct >= 85}">
                    <c:set var="progressClass" value="danger" />
                </c:if>

                <div class="capacity-card">
                    <div class="capacity-header">
                        <h3><i class="fas fa-users-cog"></i> Ocupação e Vagas</h3>
                        <span class="capacity-stats">
                            ${totalBolsistas} / ${laboratorio.capacidade} Bolsistas (<fmt:formatNumber value="${pct}" maxFractionDigits="1" />%)
                        </span>
                    </div>
                    <div class="progress-bar-container">
                        <div class="progress-bar-fill ${progressClass}" style="width: ${pct > 100.0 ? 100.0 : pct}%;"></div>
                    </div>
                    <p style="font-size: 0.85rem; color: var(--text-muted); margin: 0; margin-top: 5px; line-height: 1.4;">
                        <c:choose>
                            <c:when test="${pct >= 100}">
                                <i class="fas fa-exclamation-triangle" style="color: var(--danger-color);"></i> Limite de capacidade atingido. Não é recomendado vincular novos bolsistas.
                            </c:when>
                            <c:when test="${pct >= 85}">
                                <i class="fas fa-exclamation-circle" style="color: var(--warning-color);"></i> Laboratório próximo da capacidade limite. Restam poucas vagas.
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-check-circle" style="color: var(--success-color);"></i> O laboratório possui vagas disponíveis para novos membros.
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>

            <!-- Aba 2: Projetos -->
            <div id="projetos" class="tab-content">
                <div class="projects-grid">
                    <c:forEach var="proj" items="${laboratorio.projetos}">
                        <div class="project-card">
                            <div>
                                <div class="project-card-header">
                                    <h3>${proj.nome}</h3>
                                    <c:if test="${podeGerenciar}">
                                        <a href="projeto/desativar?id=${proj.id}&labId=${laboratorio.id}" 
                                           class="btn-deactivate-project" 
                                           title="Desativar projeto"
                                           onclick="return confirm('Deseja desativar este projeto?')">
                                            <i class="fas fa-power-off"></i>
                                        </a>
                                    </c:if>
                                </div>
                                <p class="project-desc">${not empty proj.descricao ? proj.descricao : 'Sem descrição disponível.'}</p>
                                
                                <div class="project-members" style="margin-top: 15px;">
                                    <h4><i class="fas fa-user-friends"></i> Equipe do Projeto:</h4>
                                    <div class="project-card-members">
                                        <c:set var="hasMembers" value="false" />
                                        <c:set var="membersCount" value="0" />
                                        <c:forEach var="b" items="${bolsistas}">
                                            <c:set var="isMember" value="false" />
                                            <c:set var="bpKey" value="projetosBolsista_${b.id}" />
                                            <c:forEach var="bp" items="${requestScope[bpKey]}">
                                                <c:if test="${bp.id == proj.id}">
                                                    <c:set var="isMember" value="true" />
                                                </c:if>
                                            </c:forEach>
                                            
                                            <c:if test="${isMember}">
                                                <c:set var="hasMembers" value="true" />
                                                <c:set var="membersCount" value="${membersCount + 1}" />
                                                <span class="project-member-tag" title="${not empty b.cargo ? b.cargo.descricao : 'Bolsista'}">${b.nome}</span>
                                            </c:if>
                                        </c:forEach>
                                        <c:if test="${not hasMembers}">
                                            <span class="project-member-tag empty">Nenhum bolsista vinculado</span>
                                        </c:if>
                                    </div>
                                </div>
                            </div>

                            <div class="project-card-footer">
                                <span style="font-size: 0.8rem; color: var(--text-muted); font-weight: 500;">
                                    <i class="fas fa-users-viewfinder"></i> ${membersCount} integrante(s)
                                </span>
                                <a href="projeto/detalhes?id=${proj.id}" class="btn-view-project">
                                    <i class="fas fa-external-link-alt"></i> Gerenciar Equipe
                                </a>
                            </div>
                        </div>
                    </c:forEach>

                    <c:if test="${empty laboratorio.projetos}">
                        <div class="empty-projects-state">
                            <i class="fas fa-folder-open"></i>
                            <p>Nenhum projeto ativo neste laboratório.</p>
                        </div>
                    </c:if>
                </div>

                <!-- Formulario de Novo Projeto -->
                <c:if test="${podeGerenciar}">
                    <div class="add-project-box">
                        <h3><i class="fas fa-plus-circle"></i> Adicionar Novo Projeto</h3>
                        <form action="projeto/salvar" method="post" class="add-project-form">
                            <input type="hidden" name="laboratorioId" value="${laboratorio.id}">
                            <div class="form-group">
                                <label for="nomeProjeto">Título do Projeto</label>
                                <input type="text" name="nome" id="nomeProjeto" required placeholder="Ex: Inteligência Artificial Aplicada">
                            </div>
                            <div class="form-group">
                                <label for="descProjeto">Descrição do Projeto</label>
                                <textarea name="descricao" id="descProjeto" placeholder="Breve resumo dos objetivos do projeto..."></textarea>
                            </div>
                            <button type="submit" class="btn-project-submit"><i class="fas fa-check"></i> Adicionar Projeto</button>
                        </form>
                    </div>
                </c:if>
            </div>

            <!-- Aba 3: Equipe -->
            <div id="equipe" class="tab-content">
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Nome</th>
                                <th>Curso</th>
                                <th>Cargo</th>
                                <th>Projetos Atuantes</th>
                                <th>E-mail</th>
                                <th>Matrícula</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="b" items="${bolsistas}">
                                <tr>
                                    <td><strong>${b.nome}</strong></td>
                                    <td>${b.curso}</td>
                                    <td><span class="badge badge-cargo">${not empty b.cargo ? b.cargo.descricao : 'Bolsista'}</span></td>
                                    <td>
                                        <c:set var="bpKey3" value="projetosBolsista_${b.id}" />
                                        <c:forEach var="bp" items="${requestScope[bpKey3]}" varStatus="status">
                                            <span class="project-tag">${bp.nome}</span>${not status.last ? ', ' : ''}
                                        </c:forEach>
                                        <c:if test="${empty requestScope[bpKey3]}">
                                            <span class="no-projects">Nenhum</span>
                                        </c:if>
                                    </td>
                                    <td>${b.email}</td>
                                    <td>${b.matricula}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty bolsistas}">
                                <tr>
                                    <td colspan="6" class="empty-state">
                                        Nenhum bolsista vinculado a este laboratório.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    </div>

    <!-- Script para controle das Abas -->
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            const tabs = document.querySelectorAll('.tab-btn');
            const contents = document.querySelectorAll('.tab-content');

            tabs.forEach(tab => {
                tab.addEventListener('click', () => {
                    tabs.forEach(t => t.classList.remove('active'));
                    tab.classList.add('active');

                    contents.forEach(content => content.classList.remove('active'));
                    const target = tab.getAttribute('data-tab');
                    document.getElementById(target).classList.add('active');

                    localStorage.setItem('activeLabTab_' + '${laboratorio.id}', target);
                });
            });

            // Restaura aba anterior se houver
            const savedTab = localStorage.getItem('activeLabTab_' + '${laboratorio.id}');
            if (savedTab) {
                const targetTab = document.querySelector(`.tab-btn[data-tab="${savedTab}"]`);
                if (targetTab) {
                    targetTab.click();
                }
            }
        });
    </script>
</body>
</html>
