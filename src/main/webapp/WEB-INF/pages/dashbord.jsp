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
    <link rel="stylesheet" href="css/style.css?v=2">
    <link rel="stylesheet" href="css/dashboard.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <header class="dashboard-header">
            <c:choose>
                <c:when test="${usuario.bolsista}">
                    <h1>Área do Bolsista</h1>
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
                <!-- Atalhos Rápidos -->
                <h2><i class="fas fa-rocket"></i> Atalhos Rápidos</h2>
                <div class="cards-container bolsista-shortcuts">
                    <a href="frequencia" class="card">
                        <i class="fas fa-calendar-check"></i>
                        <h3>Lançar Frequência</h3>
                        <p>Registre suas horas trabalhadas e descreva suas atividades diárias.</p>
                    </a>

                    <a href="laboratorio?action=detalhes&id=${usuario.laboratorioId}" class="card">
                        <i class="fas fa-flask"></i>
                        <h3>Meu Laboratório</h3>
                        <p>Veja detalhes da sua equipe, coordenador e projetos vinculados.</p>
                    </a>

                    <a href="laboratorio" class="card">
                        <i class="fas fa-list-alt"></i>
                        <h3>Visualizar Laboratórios</h3>
                        <p>Consulte a listagem de todos os laboratórios e seus projetos.</p>
                    </a>

                    <a href="perfil" class="card">
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
            </c:when>
            <c:otherwise>
                <!-- Painel Administrativo -->
                <div class="cards-container">
                    <a href="bolsista" class="card">
                        <i class="fas fa-user-graduate"></i>
                        <h3>Gerenciar ${usuario.admin ? 'Usuários' : 'Bolsistas'}</h3>
                        <p>Cadastre, visualize e pesquise ${usuario.admin ? 'usuários' : 'bolsistas acadêmicos'} no sistema.</p>
                    </a>

                    <a href="laboratorio" class="card">
                        <i class="fas fa-flask"></i>
                        <h3>Laboratórios</h3>
                        <p>Gerencie laboratórios, áreas de pesquisa e projetos vinculados.</p>
                    </a>

                    <a href="relatorio" class="card">
                        <i class="fas fa-chart-line"></i>
                        <h3>Relatórios</h3>
                        <p>Visualize estatísticas e relatórios de bolsas vigentes.</p>
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</body>
</html>
