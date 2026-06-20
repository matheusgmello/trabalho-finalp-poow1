<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${bolsista != null ? 'Editar' : 'Novo'} ${usuario.admin ? 'Usuário' : 'Bolsista'} - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css?v=2">
    <link rel="stylesheet" href="css/cadastro-bolsista.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="container form-container">
            <h1><i class="fas fa-user-plus"></i> ${bolsista != null ? 'Editar' : 'Cadastrar Novo'} ${usuario.admin ? 'Usuário' : 'Bolsista'}</h1>

            <p class="legenda-obrigatorio"><span class="asterisco">*</span> Campos obrigatórios</p>

            <c:if test="${not empty erro}">
                <div class="error-msg">
                    <i class="fas fa-exclamation-circle"></i> ${erro}
                </div>
            </c:if>

            <form action="bolsista" method="post" id="formBolsista">
                <input type="hidden" name="id" value="${bolsista.id}">
                <div class="form-grid">
                    <div class="form-group form-group-full">
                        <label>Nome Completo <span class="asterisco">*</span></label>
                        <input type="text" name="nome" id="nome" value="${bolsista.nome}" required minlength="3" placeholder="Ex: João da Silva">
                    </div>
                    <div class="form-group" id="group-dataNascimento">
                        <label>Data de Nascimento <span class="asterisco">*</span></label>
                        <input type="date" name="dataNascimento" id="dataNascimento" value="${bolsista.dataNascimento}" required>
                    </div>
                    <div class="form-group" id="group-curso">
                        <label>Curso <span class="asterisco">*</span></label>
                        <input type="text" name="curso" id="curso" value="${bolsista.curso}" required placeholder="Ex: Ciência da Computação">
                    </div>
                    <div class="form-group">
                        <label>E-mail <span class="asterisco">*</span></label>
                        <input type="email" name="email" id="email" value="${bolsista.email}" required placeholder="Ex: joao@universidade.edu.br">
                    </div>
                    <div class="form-group" id="group-matricula">
                        <label>Matrícula <span class="asterisco">*</span></label>
                        <input type="text" name="matricula" id="matricula" value="${bolsista.matricula}" required placeholder="Ex: 2021001234">
                    </div>
                    <c:choose>
                        <c:when test="${usuario.admin}">
                            <div class="form-group">
                                <label>Tipo de Usuário <span class="asterisco">*</span></label>
                                <select name="tipoUsuario" id="tipoUsuario" required>
                                    <option value="BOLSISTA" ${bolsista.tipoUsuario == 'BOLSISTA' ? 'selected' : ''}>Usuário Bolsista</option>
                                    <option value="PROFESSOR" ${bolsista.tipoUsuario == 'PROFESSOR' ? 'selected' : ''}>Usuário Professor/Coordenador</option>
                                    <option value="ADMIN" ${bolsista.tipoUsuario == 'ADMIN' ? 'selected' : ''}>Superusuário (Administrador)</option>
                                </select>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="tipoUsuario" id="tipoUsuario" value="${not empty bolsista.tipoUsuario ? bolsista.tipoUsuario : 'BOLSISTA'}">
                        </c:otherwise>
                    </c:choose>
                    <div class="form-group" id="group-laboratorio">
                        <label>Laboratório</label>
                        <select name="laboratorioId" id="laboratorioId">
                            <option value="">Selecione um laboratório...</option>
                            <c:forEach var="lab" items="${laboratorios}">
                                <option value="${lab.id}" ${bolsista.laboratorioId == lab.id ? 'selected' : ''}>${lab.nome}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group" id="group-cargo">
                        <label>Cargo no Laboratório</label>
                        <select name="cargo" id="cargo">
                            <option value="">Selecione um cargo...</option>
                            <c:forEach var="c" items="${cargos}">
                                <option value="${c.name()}" ${bolsista.cargo == c ? 'selected' : ''}>${c.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <c:if test="${not empty bolsista}">
                        <div class="form-group">
                            <label>URL da Foto de Perfil</label>
                            <input type="text" name="fotoUrl" value="${bolsista.fotoUrl}" placeholder="https://exemplo.com/foto.jpg">
                        </div>
                        <div class="form-group form-group-full">
                            <label>Biografia / Sobre Mim (Rede Social)</label>
                            <textarea name="bio" id="bio" rows="4" placeholder="Escreva um resumo sobre este usuário...">${bolsista.bio}</textarea>
                        </div>
                    </c:if>
                    <div class="form-group">
                        <label>Senha <c:if test="${bolsista == null}"><span class="asterisco">*</span></c:if></label>
                        <input type="password" name="senha" id="senha" ${bolsista == null ? 'required' : ''} minlength="6" placeholder="${bolsista == null ? 'Mínimo 6 caracteres' : 'Deixe em branco para não alterar'}">
                    </div>
                    <div class="actions">
                        <button type="submit" class="btn btn-submit"><i class="fas fa-save"></i> Salvar</button>
                        <a href="bolsista" class="btn btn-cancel">Cancelar</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <script src="js/validacao-bolsista.js"></script>
</body>
</html>
