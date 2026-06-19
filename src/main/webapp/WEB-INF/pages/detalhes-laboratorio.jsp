<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                    <span class="info-label">Capacidade e Vagas</span>
                    <span class="info-value">${bolsistas.size()} / ${laboratorio.capacidade} bolsistas</span>
                </div>
            </div>

            <!-- Projetos Section -->
            <div class="projects-section">
                <h2><i class="fas fa-project-diagram"></i> Projetos em Andamento</h2>
                
                <div class="projects-grid">
                    <c:forEach var="proj" items="${laboratorio.projetos}">
                        <div class="project-card">
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
                            
                            <div class="project-members">
                                <h4><i class="fas fa-users-viewfinder"></i> Equipe do Projeto:</h4>
                                <ul class="members-list">
                                    <c:set var="hasMembers" value="false" />
                                    <c:forEach var="b" items="${bolsistas}">
                                        <!-- Verifica se o bolsista pertence a este projeto -->
                                        <c:set var="isMember" value="false" />
                                        <c:set var="bpKey" value="projetosBolsista_${b.id}" />
                                        <c:forEach var="bp" items="${requestScope[bpKey]}">
                                            <c:if test="${bp.id == proj.id}">
                                                <c:set var="isMember" value="true" />
                                            </c:if>
                                        </c:forEach>
                                        
                                        <c:if test="${isMember}">
                                            <c:set var="hasMembers" value="true" />
                                            <li>
                                                <span><strong>${b.nome}</strong> (${not empty b.cargo ? b.cargo.descricao : 'Bolsista'})</span>
                                                <c:if test="${podeGerenciar}">
                                                    <a href="projeto/desvincular?bolsistaId=${b.id}&projetoId=${proj.id}&labId=${laboratorio.id}" 
                                                       class="unlink-member-btn"
                                                       title="Remover do projeto"
                                                       onclick="return confirm('Remover bolsista do projeto?')">
                                                        <i class="fas fa-user-minus"></i>
                                                    </a>
                                                </c:if>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${not hasMembers}">
                                        <li class="empty-members">Nenhum bolsista vinculado a este projeto.</li>
                                    </c:if>
                                </ul>
                            </div>

                            <!-- Formulario para vincular bolsista a este projeto -->
                            <c:if test="${podeGerenciar && not empty bolsistas}">
                                <div class="link-member-section">
                                    <form action="projeto/vincular" method="post" class="link-member-form">
                                        <input type="hidden" name="projetoId" value="${proj.id}">
                                        <input type="hidden" name="labId" value="${laboratorio.id}">
                                        <select name="bolsistaId" required>
                                            <option value="">Vincular bolsista...</option>
                                            <c:forEach var="b" items="${bolsistas}">
                                                <!-- Verifica se ja esta na equipe para nao duplicar no select -->
                                                <c:set var="alreadyMember" value="false" />
                                                <c:set var="bpKey2" value="projetosBolsista_${b.id}" />
                                                <c:forEach var="bp" items="${requestScope[bpKey2]}">
                                                    <c:if test="${bp.id == proj.id}">
                                                        <c:set var="alreadyMember" value="true" />
                                                    </c:if>
                                                </c:forEach>
                                                <c:if test="${not alreadyMember}">
                                                    <option value="${b.id}">${b.nome}</option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                        <button type="submit" class="btn-link-submit"><i class="fas fa-user-plus"></i></button>
                                    </form>
                                </div>
                            </c:if>
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
                                <input type="text" name="nome" id="nomeProjeto" required placeholder="Ex: Robótica Autônoma">
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

            <!-- Bolsistas Section -->
            <h2><i class="fas fa-users"></i> Equipe do Laboratório</h2>
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
</body>
</html>
