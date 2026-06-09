<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - SisBolsa</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="login-container">
        <div class="logo-icon">
            <i class="fas fa-graduation-cap"></i>
        </div>
        <h1>SisBolsa</h1>
        <h2>Acesse sua conta</h2>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="email">E-mail</label>
                <input value="admin@sisbolsa.com" type="email" placeholder="seu@email.com" name="email" required>
            </div>

            <div class="form-group">
                <label for="senha">Senha</label>
                <input value="teste123" type="password" placeholder="Sua senha" name="senha" required>
            </div>

            <button type="submit" class="btn-login">
                <i class="fas fa-sign-in-alt"></i> ENTRAR
            </button>
        </form>

        <c:if test="${not empty erro}">
            <div class="error-msg">
                <i class="fas fa-exclamation-triangle"></i> ${erro}
            </div>
        </c:if>

        <c:if test="${not empty sucesso}">
            <div class="success-msg">
                <i class="fas fa-check-circle"></i> ${sucesso}
            </div>
        </c:if>

        <div class="link-cadastro-admin">
            <a href="${pageContext.request.contextPath}/cadastro-admin"><i class="fas fa-user-shield"></i> Cadastrar administrador</a>
        </div>
    </div>

</body>
</html>
