<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Laboratórios - SisBolsa</title>
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #34495e;
            --accent-color: #3498db;
            --text-color: #ecf0f1;
            --bg-color: #f4f7f6;
            --sidebar-width: 250px;
            --success-color: #27ae60;
            --danger-color: #e74c3c;
            --warning-color: #f39c12;
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
            background-color: var(--danger-color);
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
        .status-ativo { background: #d4edda; color: #155724; }
        .status-em-pausa { background: #fff3cd; color: #856404; }
        .status-concluido { background: #cce5ff; color: #004085; }

        .actions-cell {
            display: flex;
            gap: 10px;
        }

        .btn-icon {
            padding: 5px 10px;
            border-radius: 3px;
            color: white;
            text-decoration: none;
            font-size: 0.9rem;
        }
        .btn-edit { background-color: var(--accent-color); }
        .btn-delete { background-color: var(--danger-color); }

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
            <li><a href="laboratorio"><i class="fas fa-flask" style="margin-right: 10px;"></i> Laboratórios</a></li>
        </ul>
        <a href="index.jsp" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Sair</a>
    </div>

    <div class="main-content">
        <div class="header-actions">
            <h1>Gerenciar Laboratórios</h1>
            <a href="laboratorio?action=novo" class="btn-new">
                <i class="fas fa-plus"></i> Cadastrar Novo Laboratório
            </a>
        </div>

        <c:if test="${not empty erro}">
            <div class="error-msg">
                <i class="fas fa-exclamation-circle"></i> ${erro}
            </div>
        </c:if>

        <div class="container">
            <h2><i class="fas fa-list"></i> Listagem de Laboratórios</h2>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Área de Pesquisa</th>
                            <th>Título do Projeto</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="lab" items="${listaLaboratorios}">
                            <tr>
                                <td><strong>${lab.nome}</strong></td>
                                <td>${lab.areaPesquisa}</td>
                                <td>${lab.tituloProjeto}</td>
                                <td>
                                    <span class="badge status-${lab.status.toLowerCase().replace(' ', '-')}">
                                        ${lab.status}
                                    </span>
                                </td>
                                <td class="actions-cell">
                                    <a href="laboratorio?action=editar&id=${lab.id}" class="btn-icon btn-edit" title="Editar"><i class="fas fa-edit"></i></a>
                                    <a href="laboratorio?action=excluir&id=${lab.id}" class="btn-icon btn-delete" title="Excluir" onclick="return confirm('Tem certeza que deseja excluir este laboratório?')"><i class="fas fa-trash"></i></a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listaLaboratorios}">
                            <tr>
                                <td colspan="5" style="text-align: center; padding: 30px; color: #999;">
                                    Nenhum laboratório encontrado.
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
