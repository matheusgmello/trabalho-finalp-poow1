<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${projeto.id > 0 ? 'Editar' : 'Novo'} Projeto - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css?v=2">
    <link rel="stylesheet" href="css/cadastro-laboratorio.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="container form-container" style="max-width: 800px; margin: 40px auto;">
            <h1>
                <i class="fas fa-project-diagram"></i> 
                ${projeto.id > 0 ? 'Editar Projeto' : 'Cadastrar Novo Projeto'}
            </h1>
            
            <p class="legenda-obrigatorio"><span class="asterisco">*</span> Campos obrigatórios</p>

            <c:if test="${not empty param.erro}">
                <div class="error-msg" style="margin-bottom: 20px;">
                    <i class="fas fa-exclamation-circle"></i> ${param.erro}
                </div>
            </c:if>

            <c:if test="${param.msg == 'lab_sucesso'}">
                <div class="success-msg" style="background-color: #d1fae5; color: #065f46; border: 1px solid #a7f3d0; padding: 15px; border-radius: var(--radius-sm); margin-bottom: 20px; display: flex; align-items: center; gap: 10px;">
                    <i class="fas fa-check-circle" style="font-size: 1.2rem;"></i>
                    <div>
                        <strong>Laboratório cadastrado com sucesso!</strong>
                        <p style="margin: 3px 0 0 0; font-size: 0.85rem;">Cadastre o primeiro projeto do laboratório no formulário abaixo.</p>
                    </div>
                </div>
            </c:if>

            <form action="projeto/salvar" method="post" id="formProjeto">
                <input type="hidden" name="origem" value="projeto">
                <c:if test="${projeto.id > 0}">
                    <input type="hidden" name="id" value="${projeto.id}">
                </c:if>
                
                <div class="form-grid">
                    <div class="form-group form-group-full">
                        <label for="nome">Nome do Projeto <span class="asterisco">*</span></label>
                        <input type="text" name="nome" id="nome" value="${projeto.nome}" required minlength="3" placeholder="Ex: IA Generativa na Educação">
                    </div>
                    
                    <div class="form-group form-group-full">
                        <label for="laboratorioId">Laboratório Responsável <span class="asterisco">*</span></label>
                        <select name="laboratorioId" id="laboratorioId" required>
                            <option value="">Selecione o laboratório...</option>
                            <c:forEach var="lab" items="${laboratorios}">
                                <option value="${lab.id}" ${projeto.laboratorioId == lab.id ? 'selected' : ''}>${lab.nome}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group form-group-full">
                        <label for="descricao">Descrição do Projeto</label>
                        <textarea name="descricao" id="descricao" rows="6" placeholder="Descreva os objetivos, tecnologias envolvidas e o escopo do projeto...">${projeto.descricao}</textarea>
                    </div>

                    <div class="actions" style="margin-top: 20px;">
                        <button type="submit" class="btn btn-submit">
                            <i class="fas fa-save"></i> ${projeto.id > 0 ? 'Salvar Alterações' : 'Salvar Projeto'}
                        </button>
                        <a href="projeto" class="btn btn-cancel">Cancelar</a>
                    </div>
                </div>
            </form>
        </div>
    </div>

</body>
</html>
