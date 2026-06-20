<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Cadastro de Bolsistas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .card-stat {
            margin-top: 10px;
            font-size: 0.9rem;
            font-weight: bold;
            color: var(--primary-color);
            background-color: rgba(74, 144, 226, 0.1);
            padding: 4px 8px;
            border-radius: 4px;
            display: inline-block;
        }
        .btn-detalhes-link {
            text-decoration: none;
            transition: color 0.2s;
        }
        .btn-detalhes-link:hover {
            color: var(--primary-dark) !important;
        }
    </style>
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <header class="dashboard-header">
            <c:choose>
                <c:when test="${usuario.bolsista}">
                    <h1>Área do Bolsista</h1>
                </c:when>
                <c:when test="${usuario.professor}">
                    <h1>Área do Professor</h1>
                </c:when>
                <c:otherwise>
                    <h1>Painel de Controle</h1>
                </c:otherwise>
            </c:choose>
            <div class="welcome-msg">
                Bem-vindo, <strong>${usuario.nome}</strong>!
            </div>
        </header>

        <c:choose>
            <c:when test="${usuario.bolsista}">
                <!-- Atalhos Rápidos Bolsista -->
                <h2><i class="fas fa-rocket"></i> Atalhos Rápidos</h2>
                <div class="cards-container bolsista-shortcuts">
                    <a href="${pageContext.request.contextPath}/frequencia" class="card">
                        <i class="fas fa-calendar-check"></i>
                        <h3>Lançar Frequência</h3>
                        <p>Registre suas horas trabalhadas e descreva suas atividades diárias.</p>
                    </a>

                    <a href="${pageContext.request.contextPath}/laboratorio/detalhes?id=${usuario.laboratorioId}" class="card">
                        <i class="fas fa-flask"></i>
                        <h3>Meu Laboratório</h3>
                        <p>Veja detalhes da sua equipe, coordenador e projetos vinculados.</p>
                    </a>

                    <a href="${pageContext.request.contextPath}/projeto" class="card">
                        <i class="fas fa-project-diagram"></i>
                        <h3>Visualizar Projetos</h3>
                        <p>Consulte a listagem de todos os projetos ativos no sistema.</p>
                    </a>

                    <a href="${pageContext.request.contextPath}/perfil" class="card">
                        <i class="fas fa-user-cog"></i>
                        <h3>Editar Perfil</h3>
                        <p>Gerencie suas informações cadastrais, senha e foto de perfil.</p>
                    </a>
                </div>

                <!-- Minha Equipe -->
                <div class="equipe-container">
                    <h2><i class="fas fa-users"></i> Minha Equipe (${meuLaboratorio.nome})</h2>
                    <c:if test="${not empty meuLaboratorio.coordenador}">
                        <div class="coordenador-info">
                            <i class="fas fa-user-tie"></i> Coordenador do Laboratório: <strong>${meuLaboratorio.coordenador}</strong>
                        </div>
                    </c:if>
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Nome</th>
                                    <th>E-mail</th>
                                    <th>Função</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="membro" items="${equipe}">
                                    <tr class="${membro.id == usuario.id ? 'highlight-self' : ''}">
                                        <td>
                                            <div class="user-cell">
                                                <c:choose>
                                                    <c:when test="${not empty membro.fotoUrl}">
                                                        <img src="${membro.fotoUrl}" alt="Avatar" class="avatar-small">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="avatar-placeholder"><i class="fas fa-user"></i></div>
                                                    </c:otherwise>
                                                </c:choose>
                                                <div class="user-info-wrapper">
                                                    <strong class="user-name">${membro.nome} ${membro.id == usuario.id ? '(Você)' : ''}</strong>
                                                    <c:if test="${not empty membro.bio}">
                                                        <p class="user-bio-small" title="${membro.bio}">${membro.bio}</p>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </td>
                                        <td>${membro.email}</td>
                                        <td><span class="membro-cargo">${not empty membro.cargo ? membro.cargo.descricao : 'Bolsista'}</span></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty equipe}">
                                    <tr>
                                        <td colspan="3" class="empty-state">
                                            Nenhum outro participante no laboratório.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                         </table>
                    </div>
                </div>

                <!-- Meus Projetos Vinculados -->
                <div class="equipe-container" style="margin-top: 30px;">
                    <h2><i class="fas fa-project-diagram"></i> Meus Projetos Vinculados</h2>
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Projeto</th>
                                    <th>Descrição</th>
                                    <th>Laboratório</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="proj" items="${meusProjetos}">
                                    <tr>
                                        <td><strong>${proj.nome}</strong></td>
                                        <td>${proj.descricao}</td>
                                        <td>${proj.nomeLaboratorio}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/projeto/detalhes?id=${proj.id}" class="btn-detalhes-link" style="color: var(--primary-color); font-weight: bold;"><i class="fas fa-info-circle"></i> Detalhes</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty meusProjetos}">
                                    <tr>
                                        <td colspan="4" class="empty-state">
                                            Você não está vinculado a nenhum projeto no momento.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:when>

            <c:when test="${usuario.professor}">
                <!-- Atalhos Rápidos Professor -->
                <h2><i class="fas fa-rocket"></i> Atalhos Rápidos</h2>
                <div class="cards-container">
                    <a href="${pageContext.request.contextPath}/bolsista" class="card">
                        <i class="fas fa-user-graduate"></i>
                        <h3>Gerenciar Bolsistas</h3>
                        <p>Gerencie os bolsistas vinculados aos laboratórios sob sua coordenação.</p>
                    </a>

                    <a href="${pageContext.request.contextPath}/laboratorio" class="card">
                        <i class="fas fa-flask"></i>
                        <h3>Meus Laboratórios</h3>
                        <p>Visualize os laboratórios que você coordena e seus respectivos projetos.</p>
                    </a>

                    <a href="${pageContext.request.contextPath}/frequencia" class="card">
                        <i class="fas fa-calendar-alt"></i>
                        <h3>Frequências da Equipe</h3>
                        <p>Acompanhe e valide a folha de frequências dos bolsistas sob sua supervisão.</p>
                    </a>

                    <a href="${pageContext.request.contextPath}/perfil" class="card">
                        <i class="fas fa-user-cog"></i>
                        <h3>Editar Perfil</h3>
                        <p>Gerencie suas informações cadastrais, biografia e foto de perfil.</p>
                    </a>
                </div>

                <!-- Meus Laboratórios Coordenados -->
                <div class="equipe-container" style="margin-top: 30px;">
                    <h2><i class="fas fa-university"></i> Meus Laboratórios Coordenados (Total de Bolsistas: ${totalBolsistasCoordenados})</h2>
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Laboratório</th>
                                    <th>Área de Pesquisa</th>
                                    <th>Status</th>
                                    <th>Capacidade Ocupada</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="lab" items="${meusLaboratorios}">
                                    <tr>
                                        <td><strong>${lab.nome}</strong></td>
                                        <td>${lab.areaPesquisa}</td>
                                        <td><span class="badge ${lab.status == 'ATIVO' ? 'badge-success' : 'badge-danger'}">${lab.status}</span></td>
                                        <td>
                                            <strong>${lab.capacidade}</strong> vagas totais
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/laboratorio/detalhes?id=${lab.id}" class="btn-detalhes-link" style="color: var(--primary-color); font-weight: bold; margin-right: 15px;"><i class="fas fa-eye"></i> Detalhes</a>
                                            <a href="${pageContext.request.contextPath}/laboratorio/editar?id=${lab.id}" class="btn-detalhes-link" style="color: #f39c12; font-weight: bold;"><i class="fas fa-edit"></i> Editar</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty meusLaboratorios}">
                                    <tr>
                                        <td colspan="5" class="empty-state">
                                            Você não coordena nenhum laboratório ativo cadastrado.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <!-- Painel Administrativo Admin -->
                <h2><i class="fas fa-rocket"></i> Atalhos Rápidos</h2>
                <div class="cards-container">
                    <a href="${pageContext.request.contextPath}/bolsista" class="card">
                        <i class="fas fa-user-graduate"></i>
                        <h3>Gerenciar Usuários</h3>
                        <p>Cadastre, edite e pesquise bolsistas, professores e administradores.</p>
                        <div class="card-stat">${totalBolsistas} cadastrados</div>
                    </a>

                    <a href="${pageContext.request.contextPath}/laboratorio" class="card">
                        <i class="fas fa-flask"></i>
                        <h3>Laboratórios</h3>
                        <p>Gerencie os laboratórios de pesquisa, coordenadores e vagas.</p>
                        <div class="card-stat">${totalLabs} cadastrados</div>
                    </a>

                    <a href="${pageContext.request.contextPath}/projeto" class="card">
                        <i class="fas fa-project-diagram"></i>
                        <h3>Projetos</h3>
                        <p>Gerencie todos os projetos ativos e o vínculo de bolsistas.</p>
                        <div class="card-stat">${totalProjetos} cadastrados</div>
                    </a>

                    <a href="${pageContext.request.contextPath}/frequencia" class="card">
                        <i class="fas fa-calendar-check"></i>
                        <h3>Frequências</h3>
                        <p>Visualize e gerencie a folha de horas e atividades de todos os bolsistas.</p>
                    </a>

                    <a href="${pageContext.request.contextPath}/relatorio" class="card">
                        <i class="fas fa-chart-line"></i>
                        <h3>Relatórios</h3>
                        <p>Visualize estatísticas e relatórios avançados sobre a plataforma.</p>
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</body>
</html>
