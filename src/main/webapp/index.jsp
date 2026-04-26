<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - SisBolsa</title>
    <style>
        :root {
            --primary-color: #2c3e50;
            --accent-color: #3498db;
            --bg-color: #f4f7f6;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: var(--bg-color);
        }

        .login-container {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }

        .login-container h1 {
            color: var(--primary-color);
            margin-bottom: 10px;
            font-size: 2rem;
        }

        .login-container h2 {
            color: #777;
            font-size: 1.1rem;
            font-weight: 400;
            margin-bottom: 30px;
        }

        .form-group {
            text-align: left;
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: var(--primary-color);
        }

        input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 8px;
            box-sizing: border-box;
            font-size: 16px;
            transition: border-color 0.3s;
        }

        input:focus {
            outline: none;
            border-color: var(--accent-color);
        }

        .btn-login {
            width: 100%;
            padding: 14px;
            background-color: var(--accent-color);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s;
            margin-top: 10px;
        }

        .btn-login:hover {
            background-color: #2980b9;
        }

        .error-msg {
            color: #e74c3c;
            background-color: #fadbd8;
            padding: 10px;
            border-radius: 5px;
            margin-top: 20px;
            font-size: 14px;
        }

        .logo-icon {
            font-size: 3rem;
            color: var(--accent-color);
            margin-bottom: 20px;
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="login-container">
        <div class="logo-icon">
            <i class="fas fa-graduation-cap"></i>
        </div>
        <h1>SisBolsa</h1>
        <h2>Acesse sua conta</h2>

        <form action="login" method="post">
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
    </div>

</body>
</html>
