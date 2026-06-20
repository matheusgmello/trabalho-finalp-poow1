<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalhes do Projeto - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css?v=2">
    <link rel="stylesheet" href="css/detalhes-laboratorio.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .project-details-grid {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 30px;
            margin-top: 30px;
        }
        @media (max-width: 900px) {
            .project-details-grid {
                grid-template-columns: 1fr;
            }
        }
        .desc-box {
            background-color: var(--surface-color);
            border: 1px solid var(--border-grid);
            border-radius: var(--radius-md);
            padding: 24px;
            box-shadow: var(--shadow-sm);
            margin-bottom: 30px;
        }
        .desc-box p {
            margin-top: 10px;
            color: var(--text-main);
            line-height: 1.6;
            font-size: 0.95rem;
        }
        .card-management {
            background-color: var(--surface-color);
            border: 1px solid var(--border-grid);
            border-radius: var(--radius-md);
            padding: 20px;
            box-shadow: var(--shadow-sm);
            height: fit-content;
        }
        .badge-unlink {
            color: var(--danger-color);
            background-color: var(--danger-bg);
            border: 1px solid #fecaca;
            border-radius: var(--radius-sm);
            padding: 4px 8px;
            text-decoration: none;
            font-size: 0.75rem;
            display: inline-flex;
            align-items: center;
            gap: 4px;
            transition: var(--transition-smooth);
        }
        .badge-unlink:hover {
            background-color: #fecaca;
        }
    </style>
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="container">
            <div class="lab-header">
                <h1><i class="fas fa-project-diagram"></i> Projeto: ${projeto.nome}</h1>
                <a href="projeto" class="btn-back"><i class="fas fa-arrow-left"></i> Voltar</a>
            </div>

            <div class="lab-info-grid">
                <div class="info-item">
                    <span class="info-label">Laboratório Responsável</span>
                    <span class="info-value">
                        <a href="laboratorio/detalhes?id=${projeto.laboratorioId}" style="text-decoration: none; color: var(--primary-color);">
                            <i class="fas fa-flask"></i> ${projeto.nomeLaboratorio}
                        </a>
                    </span>
                </div>
                <div class="info-item">
                    <span class="info-label">Professor Coordenador / Gestor</span>
                    <span class="info-value">
                        <i class="fas fa-user-tie" style="color: #64748b; margin-right: 4px;"></i> 
                        ${not empty coordenador ? coordenador : 'Sem coordenador'}
                    </span>
                </div>
            </div>

            <div class="project-details-grid">
                <div>
                    <!-- Descrição Box -->
                    <div class="desc-box">
                        <span class="info-label">Descrição Detalhada do Projeto</span>
                        <p>${not empty projeto.descricao ? projeto.descricao : 'Nenhuma descrição detalhada foi cadastrada para este projeto.'}</p>
                    </div>

                    <!-- Equipe do Projeto -->
                    <h2><i class="fas fa-users-viewfinder"></i> Bolsistas Vinculados a este Projeto</h2>
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Nome</th>
                                    <th>Curso</th>
                                    <th>Cargo</th>
                                    <th>E-mail</th>
                                    <c:if test="${podeGerenciar}">
                                        <th>Desvincular</th>
                                    </c:if>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="m" items="${membros}">
                                    <tr>
                                        <td><strong>${m.nome}</strong></td>
                                        <td>${m.curso}</td>
                                        <td><span class="badge badge-cargo" style="background-color: #eff6ff; color: #1d4ed8; border: 1px solid #dbeafe;">${not empty m.cargo ? m.cargo.descricao : 'Bolsista'}</span></td>
                                        <td>${m.email}</td>
                                        <c:if test="${podeGerenciar}">
                                            <td>
                                                <a href="projeto/desvincular?bolsistaId=${m.id}&projetoId=${projeto.id}&labId=${projeto.laboratorioId}&origem=detalhes-projeto" 
                                                   class="badge-unlink"
                                                   title="Remover bolsista deste projeto"
                                                   onclick="return confirm('Remover ${m.nome} do projeto?')">
                                                    <i class="fas fa-user-minus"></i> Remover
                                                </a>
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty membros}">
                                    <tr>
                                        <td colspan="${podeGerenciar ? 5 : 4}" class="empty-state">
                                            Nenhum bolsista vinculado a este projeto no momento.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Painel de Gerenciamento Lateral -->
                <c:if test="${podeGerenciar}">
                    <div class="card-management">
                        <h3 style="margin-top: 0; font-family: 'Outfit', sans-serif; display: flex; align-items: center; gap: 8px; font-size: 1.15rem;">
                            <i class="fas fa-user-plus" style="color: var(--primary-color);"></i> Vincular Bolsista
                        </h3>
                        <p style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 15px; line-height: 1.4;">
                            Selecione um bolsista ativo do laboratório **${projeto.nomeLaboratorio}** para integrá-lo a este projeto.
                        </p>
                        
                        <form action="projeto/vincular" method="post">
                            <input type="hidden" name="projetoId" value="${projeto.id}">
                            <input type="hidden" name="labId" value="${projeto.laboratorioId}">
                            <input type="hidden" name="origem" value="detalhes-projeto">
                            
                            <div class="form-group" style="margin-bottom: 15px;">
                                <select name="bolsistaId" required style="width: 100%; padding: 10px; border-radius: var(--radius-sm); border: 1px solid var(--border-grid); background-color: var(--surface-color);">
                                    <option value="">Selecione um bolsista...</option>
                                    <c:forEach var="b" items="${bolsistasLab}">
                                        <!-- Verifica se o bolsista já não é membro deste projeto -->
                                        <c:set var="alreadyMember" value="false" />
                                        <c:forEach var="m" items="${membros}">
                                            <c:if test="${m.id == b.id}">
                                                <c:set var="alreadyMember" value="true" />
                                            </c:if>
                                        </c:forEach>
                                        
                                        <c:if test="${not alreadyMember}">
                                            <option value="${b.id}">${b.nome} (${not empty b.cargo ? b.cargo.descricao : 'Bolsista'})</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <button type="submit" class="btn btn-submit" style="width: 100%; justify-content: center; display: inline-flex; gap: 8px; font-size: 0.85rem; padding: 10px;">
                                <i class="fas fa-plus"></i> Vincular ao Projeto
                            </button>
                        </form>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

</body>
</html>
