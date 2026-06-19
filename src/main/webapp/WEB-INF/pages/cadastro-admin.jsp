<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro de Administrador - SisBolsa</title>
    <link rel="stylesheet" href="css/cadastro-admin.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="cadastro-container">
        <div class="logo-icon">
            <i class="fas fa-user-shield"></i>
        </div>
        <h1>SisBolsa</h1>
        <h2>Cadastro de Administrador</h2>
        <p class="subtitulo">
            Crie uma conta de administrador do sistema.
            <c:if test="${not empty adminsRestantes}">
                <br><small>Vagas disponíveis: ${adminsRestantes} de 3.</small>
            </c:if>
        </p>

        <p class="legenda-obrigatorio"><span class="asterisco">*</span> Campos obrigatórios</p>

        <form action="cadastro-admin" method="post">
            <div class="form-group">
                <label for="nome">Nome completo <span class="asterisco">*</span></label>
                <input type="text" id="nome" name="nome" placeholder="Seu nome completo" required>
            </div>

            <div class="form-group">
                <label for="email">E-mail <span class="asterisco">*</span></label>
                <input type="email" id="email" name="email" placeholder="admin@exemplo.com" required>
            </div>

            <div class="form-group">
                <label for="senha">Senha <span class="asterisco">*</span></label>
                <input type="password" id="senha" name="senha" placeholder="Mínimo 6 caracteres" required>
            </div>

            <div class="form-group">
                <label for="confirmaSenha">Confirmar senha <span class="asterisco">*</span></label>
                <input type="password" id="confirmaSenha" name="confirmaSenha" placeholder="Repita a senha" required>
            </div>

            <button type="submit" class="btn-cadastrar">
                <i class="fas fa-user-plus"></i> CADASTRAR ADMINISTRADOR
            </button>
        </form>

        <c:if test="${not empty erro}">
            <div class="error-msg">
                <i class="fas fa-exclamation-triangle"></i> ${erro}
            </div>
        </c:if>

        <div class="link-voltar">
            <a href="login"><i class="fas fa-arrow-left"></i> Voltar para o login</a>
        </div>
    </div>

</body>
</html>
