<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${laboratorio != null ? 'Editar' : 'Novo'} Laboratório - SisBolsa</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cadastro-laboratorio.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="container">
            <h1><i class="fas fa-flask"></i> ${laboratorio != null ? 'Editar' : 'Cadastrar Novo'} Laboratório</h1>
            
            <p class="legenda-obrigatorio"><span class="asterisco">*</span> Campos obrigatórios</p>

            <c:if test="${not empty erro}">
                <div class="error-msg">
                    <i class="fas fa-exclamation-circle"></i> ${erro}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/laboratorio" method="post" id="formLab">
                <input type="hidden" name="id" value="${laboratorio.id}">
                <div class="form-grid">
                    <div class="form-group form-group-full">
                        <label>Nome do Laboratório <span class="asterisco">*</span></label>
                        <input type="text" name="nome" id="nome" value="${laboratorio.nome}" required minlength="3" placeholder="Ex: Lab de Inteligência Artificial">
                    </div>
                    <div class="form-group">
                        <label>Área de Pesquisa <span class="asterisco">*</span></label>
                        <input type="text" name="areaPesquisa" id="areaPesquisa" value="${laboratorio.areaPesquisa}" required placeholder="Ex: Computação, Química, Engenharia">
                    </div>
                    <div class="form-group">
                        <label>Professor Coordenador <span class="asterisco">*</span></label>
                        <select name="coordenadorId" id="coordenadorId" required>
                            <option value="">Selecione um professor...</option>
                            <c:forEach var="p" items="${professores}">
                                <option value="${p.id}" ${laboratorio.coordenadorId == p.id ? 'selected' : ''}>${p.nome}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Capacidade Máxima <span class="asterisco">*</span></label>
                        <input type="number" name="capacidade" id="capacidade" value="${laboratorio.id > 0 ? laboratorio.capacidade : 10}" required min="1" placeholder="Ex: 10">
                    </div>
                    <div class="form-group">
                        <label>Status <span class="asterisco">*</span></label>
                        <select name="status" id="status" required>
                            <option value="">Selecione o status...</option>
                            <option value="Ativo" ${laboratorio.status == 'Ativo' ? 'selected' : ''}>Ativo</option>
                            <option value="Em Pausa" ${laboratorio.status == 'Em Pausa' ? 'selected' : ''}>Em Pausa</option>
                            <option value="Concluido" ${laboratorio.status == 'Concluido' ? 'selected' : ''}>Concluído</option>
                        </select>
                    </div>
                    <div class="actions">
                        <button type="submit" class="btn btn-submit"><i class="fas fa-save"></i> Salvar Laboratório</button>
                        <a href="${pageContext.request.contextPath}/laboratorio" class="btn btn-cancel">Cancelar</a>
                    </div>
                </div>
            </form>

            <c:if test="${laboratorio != null && laboratorio.id > 0}">
                <div class="secao-projetos" id="secao-projetos">
                    <h2><i class="fas fa-project-diagram"></i> Projetos Cadastrados no Laboratório</h2>
                    
                    <c:if test="${not empty param.erro}">
                        <div class="error-msg">
                            <i class="fas fa-exclamation-circle"></i> ${param.erro}
                        </div>
                    </c:if>

                    <div class="projetos-grid">
                        <c:forEach var="proj" items="${laboratorio.projetos}">
                            <div class="projeto-card">
                                <div class="projeto-info">
                                    <h4>${proj.nome}</h4>
                                    <p>${not empty proj.descricao ? proj.descricao : 'Sem descrição cadastrada.'}</p>
                                </div>
                                <div class="projeto-acoes">
                                    <a href="${pageContext.request.contextPath}/laboratorio/editar?id=${laboratorio.id}&editarProjetoId=${proj.id}#secao-projetos" class="btn-mini btn-mini-edit">
                                        <i class="fas fa-edit"></i> Editar
                                    </a>
                                    <a href="${pageContext.request.contextPath}/projeto/desativar?id=${proj.id}&labId=${laboratorio.id}&origem=editar" class="btn-mini btn-mini-delete" onclick="return confirm('Deseja realmente desativar este projeto?')">
                                        <i class="fas fa-trash"></i> Excluir
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty laboratorio.projetos}">
                            <div class="form-group-full" style="grid-column: span 2; text-align: center; color: #777; padding: 20px;">
                                <p><i class="fas fa-folder-open"></i> Nenhum projeto cadastrado neste laboratório.</p>
                            </div>
                        </c:if>
                    </div>

                    <div class="form-projeto-box">
                        <h3>
                            <i class="fas ${projetoParaEditar != null ? 'fa-edit' : 'fa-plus-circle'}"></i> 
                            ${projetoParaEditar != null ? 'Editar Projeto' : 'Adicionar Novo Projeto'}
                        </h3>
                        <form action="${pageContext.request.contextPath}/projeto/salvar" method="post">
                            <input type="hidden" name="laboratorioId" value="${laboratorio.id}">
                            <input type="hidden" name="origem" value="editar">
                            <c:if test="${projetoParaEditar != null}">
                                <input type="hidden" name="id" value="${projetoParaEditar.id}">
                            </c:if>
                            
                            <div class="form-projeto-grid">
                                <div class="form-group">
                                    <label for="nomeProj">Nome do Projeto <span class="asterisco">*</span></label>
                                    <input type="text" name="nome" id="nomeProj" value="${projetoParaEditar.nome}" required minlength="3" placeholder="Ex: Desenvolvimento App Mobile">
                                </div>
                                <div class="form-group">
                                    <label for="descProj">Descrição do Projeto</label>
                                    <textarea name="descricao" id="descProj" placeholder="Descreva os objetivos, tecnologias e escopo do projeto...">${projetoParaEditar.descricao}</textarea>
                                </div>
                                <div class="proj-actions">
                                    <button type="submit" class="btn-proj-save">
                                        <i class="fas fa-check"></i> ${projetoParaEditar != null ? 'Salvar Alterações' : 'Cadastrar Projeto'}
                                    </button>
                                    <c:if test="${projetoParaEditar != null}">
                                        <a href="${pageContext.request.contextPath}/laboratorio/editar?id=${laboratorio.id}#secao-projetos" class="btn-proj-cancel">
                                            Cancelar
                                        </a>
                                    </c:if>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/validacao-laboratorio.js"></script>
</body>
</html>
