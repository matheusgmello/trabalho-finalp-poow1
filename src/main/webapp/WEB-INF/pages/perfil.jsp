<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Perfil - SisBolsa</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/perfil.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <t:sidebar />

    <div class="main-content">
        <div class="container form-container">
            <h1><i class="fas fa-user-cog"></i> Editar Perfil</h1>

            <div class="profile-header-display">
                <c:choose>
                    <c:when test="${not empty usuario.fotoUrl}">
                        <img src="${usuario.fotoUrl}" alt="Foto de perfil" class="avatar-large">
                    </c:when>
                    <c:otherwise>
                        <div class="avatar-placeholder-large"><i class="fas fa-user"></i></div>
                    </c:otherwise>
                </c:choose>
                <div class="profile-meta">
                    <h2>${usuario.nome}</h2>
                    <span class="badge badge-role">${usuario.tipoUsuario}</span>
                </div>
            </div>

            <c:if test="${not empty erro}">
                <div class="error-msg">
                    <i class="fas fa-exclamation-circle"></i> ${erro}
                </div>
            </c:if>

            <c:if test="${not empty sucesso}">
                <div class="success-msg">
                    <i class="fas fa-check-circle"></i> ${sucesso}
                </div>
            </c:if>

            <form action="perfil" method="post" id="formPerfil">
                <div class="form-grid">
                    <div class="form-group form-group-full">
                        <label>Nome Completo <span class="asterisco">*</span></label>
                        <input type="text" name="nome" id="nome" value="${usuario.nome}" required minlength="3" placeholder="Ex: João da Silva">
                    </div>
                    
                    <div class="form-group">
                        <label>E-mail <span class="asterisco">*</span></label>
                        <input type="email" name="email" id="email" value="${usuario.email}" required placeholder="Ex: email@universidade.edu.br">
                    </div>

                    <div class="form-group">
                        <label>URL da Foto de Perfil</label>
                        <input type="text" name="fotoUrl" id="fotoUrl" value="${usuario.fotoUrl}" placeholder="https://exemplo.com/foto.jpg">
                    </div>

                    <div class="form-group">
                        <label>Nova Senha <span class="asterisco">*</span></label>
                        <input type="password" name="senha" id="senha" value="${usuario.senha}" required minlength="6" placeholder="Mínimo 6 caracteres">
                    </div>

                    <div class="form-group">
                        <label>Confirmar Nova Senha <span class="asterisco">*</span></label>
                        <input type="password" name="confirmaSenha" id="confirmaSenha" value="${usuario.senha}" required minlength="6" placeholder="Confirme a senha">
                    </div>

                    <div class="actions">
                        <button type="submit" class="btn btn-submit"><i class="fas fa-save"></i> Salvar Alterações</button>
                        <a href="dashboard" class="btn btn-cancel">Voltar</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
