<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Bolsistas - SisBolsa</title>
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #34495e;
            --accent-color: #3498db;
            --text-color: #ecf0f1;
            --bg-color: #f4f7f6;
            --sidebar-width: 250px;
            --success-color: #27ae60;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            display: flex;
            background-color: var(--bg-color);
        }

        /* Sidebar */
        .sidebar {
            width: var(--sidebar-width);
            height: 100vh;
            background-color: var(--primary-color);
            color: var(--text-color);
            position: fixed;
            display: flex;
            flex-direction: column;
            padding: 20px 0;
        }

        .sidebar h2 {
            text-align: center;
            font-size: 1.5rem;
            margin-bottom: 30px;
            color: var(--accent-color);
        }

        .sidebar ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .sidebar ul li {
            padding: 15px 25px;
            transition: background 0.3s;
        }

        .sidebar ul li:hover {
            background-color: var(--secondary-color);
        }

        .sidebar ul li a {
            color: var(--text-color);
            text-decoration: none;
            display: flex;
            align-items: center;
            font-weight: 500;
        }

        .logout-btn {
            margin-top: auto;
            margin-bottom: 20px;
            padding: 15px 25px;
            background-color: #e74c3c;
            text-align: center;
            color: white;
            text-decoration: none;
            font-weight: bold;
            transition: background 0.3s;
        }

        /* Main Content */
        .main-content {
            margin-left: var(--sidebar-width);
            flex: 1;
            padding: 30px;
        }

        .container {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            margin-bottom: 30px;
        }

        .header-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        h1, h2 { color: var(--primary-color); margin: 0; }

        .btn-new {
            background-color: var(--success-color);
            color: white;
            padding: 12px 20px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            display: flex;
            align-items: center;
            gap: 10px;
            transition: background 0.3s;
        }

        .btn-new:hover { background-color: #219150; }

        /* Search Section */
        .search-section {
            display: flex;
            gap: 15px;
            margin-bottom: 25px;
            background: #f9f9f9;
            padding: 20px;
            border-radius: 10px;
            border: 1px solid #eee;
        }

        .search-group {
            display: flex;
            align-items: center;
            gap: 10px;
            flex: 1;
        }

        .search-group input {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            flex: 1;
        }

        .btn-search {
            background-color: var(--accent-color);
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 5px;
            cursor: pointer;
        }

        /* Table Style */
        .table-container { overflow-x: auto; }

        table { width: 100%; border-collapse: collapse; }
        th { background-color: var(--primary-color); color: white; text-align: left; padding: 15px; }
        td { padding: 12px 15px; border-bottom: 1px solid #eee; }
        tr:hover { background-color: #f9f9f9; }

        .badge {
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }
        .badge-active { background: #d4edda; color: #155724; }
        .badge-inactive { background: #f8d7da; color: #721c24; }

        .error-msg {
            background-color: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="sidebar">
        <h2>SisBolsa</h2>
        <ul>
            <li><a href="dashboard"><i class="fas fa-home" style="margin-right: 10px;"></i> Dashboard</a></li>
            <li><a href="bolsista"><i class="fas fa-user-graduate" style="margin-right: 10px;"></i> Bolsistas</a></li>
            <li><a href="#"><i class="fas fa-flask" style="margin-right: 10px;"></i> Laboratórios</a></li>
        </ul>
        <a href="index.jsp" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Sair</a>
    </div>

    <div class="main-content">
        <div class="header-actions">
            <h1>Gerenciar Bolsistas</h1>
            <a href="bolsista?action=novo" class="btn-new">
                <i class="fas fa-plus"></i> Cadastrar Novo Bolsista
            </a>
        </div>

        <c:if test="${not empty erro}">
            <div class="error-msg">
                <i class="fas fa-exclamation-circle"></i> ${erro}
            </div>
        </c:if>

        <!-- Listagem e Busca -->
        <div class="container">
            <h2><i class="fas fa-list"></i> Listagem de Bolsistas</h2>

            <!-- Search Bars -->
            <div class="search-section">
                <form action="bolsista" method="get" class="search-group">
                    <label for="buscaNome">Por Nome:</label>
                    <input type="text" name="buscaNome" id="buscaNome" placeholder="Pesquisar nome...">
                    <button type="submit" class="btn-search"><i class="fas fa-search"></i></button>
                </form>
                
                <form action="bolsista" method="get" class="search-group">
                    <label for="buscaCurso">Por Curso:</label>
                    <input type="text" name="buscaCurso" id="buscaCurso" placeholder="Pesquisar curso...">
                    <button type="submit" class="btn-search"><i class="fas fa-search"></i></button>
                </form>

                <div class="search-group" style="flex: 0;">
                    <a href="bolsista" class="btn-search" style="background: #666; text-decoration: none;"><i class="fas fa-sync"></i> Limpar</a>
                </div>
            </div>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Curso</th>
                            <th>E-mail</th>
                            <th>Matrícula</th>
                            <th>CPF</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="bolsista" items="${listabolsistas}">
                            <tr>
                                <td><strong>${bolsista.nome}</strong></td>
                                <td>${bolsista.curso}</td>
                                <td>${bolsista.email}</td>
                                <td>${bolsista.matricula}</td>
                                <td>${bolsista.cpf}</td>
                                <td>
                                    <span class="badge ${bolsista.ativo ? 'badge-active' : 'badge-inactive'}">
                                        ${bolsista.ativo ? 'Ativo' : 'Inativo'}
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listabolsistas}">
                            <tr>
                                <td colspan="6" style="text-align: center; padding: 30px; color: #999;">
                                    Nenhum bolsista encontrado.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</body>
</html>
